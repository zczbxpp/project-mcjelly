package pl.zczb.sectors.managers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.redis.packet.Packet;
import pl.zczb.sectors.managers.data.Sector;
import pl.zczb.sectors.managers.data.SectorTypeEnum;
import pl.zczb.sectors.packets.SectorTransferPacket;
import pl.zczb.sectors.serialization.SerializationHelper;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SectorManager {
    private static final Map<SectorTypeEnum, Map<String, Sector>> sectorsByType = Collections.synchronizedMap(new HashMap<>());

    static {
        for (SectorTypeEnum type : SectorTypeEnum.values()) {
            sectorsByType.put(type, Collections.synchronizedMap(new HashMap<>()));
        }
    }

    public static void createSector(String sectorName, SectorTypeEnum sectorType, boolean isAdminMode) {
        Sector sector = new Sector(sectorName, sectorType, isAdminMode);
        ((Map<String, Sector>) sectorsByType.get(sectorType)).put(sectorName, sector);
    }

    public static void removeSector(String sectorName, SectorTypeEnum sectorType) {
        if (sectorsByType.containsKey(sectorType)) {
            ((Map) sectorsByType.get(sectorType)).remove(sectorName);
        }
    }

    private static double getServerTps() {
        return (MinecraftServer.getServer()).recentTps[0];
    }

    public static Sector getSector(String sectorName) {
        for (Map<String, Sector> typeSectors : sectorsByType.values()) {
            if (typeSectors.containsKey(sectorName)) {
                return typeSectors.get(sectorName);
            }
        }
        return null;
    }

    public static Map<String, Sector> getAllSectors() {
        Map<String, Sector> allSectors = new HashMap<>();
        Objects.requireNonNull(allSectors);
        sectorsByType.values().forEach(allSectors::putAll);
        return Collections.unmodifiableMap(allSectors);
    }

    public static Map<String, Sector> getSectorsByType(SectorTypeEnum sectorType) {
        return Collections.unmodifiableMap(sectorsByType.getOrDefault(sectorType, new HashMap<>()));
    }

    public static int getTotalPlayersInMode(SectorTypeEnum sectorType) {
        return sectorsByType.getOrDefault(sectorType, new HashMap<>())
                .values()
                .stream()
                .filter(Sector::isOnline)
                .mapToInt(Sector::getPlayerCount)
                .sum();
    }

    public static int getPlayersInSector(String sectorName) {
        Sector sector = getSector(sectorName);
        return (sector != null && sector.isOnline()) ? sector.getPlayerCount() : 0;
    }

    public static Sector getBestSector(String sectorPrefix) {
        return sectorsByType.values().stream()
                .flatMap(map -> map.values().stream())
                .filter(sector -> sector.getSectorName().startsWith(sectorPrefix))
                .filter(Sector::isOnline)
                .filter(sector -> !sector.getSectorName().endsWith("_event"))
                .min(Comparator.<Sector>comparingInt(Sector::getPlayerCount)
                        .thenComparingDouble(Sector::getTps))
                .orElse(getSector(sectorPrefix + "_1"));
    }

    public static Sector getBestSectorExcludingCurrent(String sectorPrefix) {
        String excludedSector = Tools.getSectorConfig().getCurrentSector().getSectorName();

        return sectorsByType.values().stream()
                .flatMap(map -> map.values().stream())
                .filter(sector -> sector.getSectorName().startsWith(sectorPrefix))
                .filter(sector -> !sector.getSectorName().equals(excludedSector))
                .filter(Sector::isOnline)
                .filter(sector -> !sector.isAdminMode())
                .filter(sector -> !sector.getSectorName().endsWith("_event"))
                .min(Comparator.<Sector>comparingInt(Sector::getPlayerCount)
                        .thenComparingDouble(Sector::getTps))
                .orElse(getSector(sectorPrefix + "_1"));
    }


    public static boolean isExistsSector(String sectorName) {
        return (getSector(sectorName) != null);
    }



    public static void stopSector() {
        List<Player> playersToTransfer = new ArrayList<>(Bukkit.getOnlinePlayers());


        CompletableFuture<Void> transferFuture = CompletableFuture.allOf(
                playersToTransfer.stream()
                        .map(player -> CompletableFuture.runAsync(() -> {
                            teleportToLocationSector(player, getBestSectorExcludingCurrent(Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase()).getSectorName(),new Location(Bukkit.getWorld("world"),0,121,0));
                            player.sendTitle(GlobalHelper.fixColor("&4Przeniesiono cię!"), GlobalHelper.fixColor("&CKanał na którym byłeś został wyłączony!"), 0, 60, 0);
                        }))
                        .toArray(CompletableFuture[]::new)
        );
        transferFuture.thenRun(() -> {
            System.out.println("Wszyscy gracze zostali przeniesieni, zamykam serwer...");
            Bukkit.shutdown();
        }).join();
    }


    public static void stopSectorPanic() {
        List<Player> playersToTransfer = new ArrayList<>(Bukkit.getOnlinePlayers());


        CompletableFuture<Void> transferFuture = CompletableFuture.allOf(
                playersToTransfer.stream()
                        .map(player -> CompletableFuture.runAsync(() ->
                                Bukkit.getScheduler().runTask(Tools.getInstance(), () ->
                                        player.kickPlayer(GlobalHelper.fixColor("&cTwój sektor został wyłączony.\n&cWejdz ponownie!"))
                                )
                        ))
                        .toArray(CompletableFuture[]::new)
        );

        transferFuture.thenRun(() -> {
            System.out.println("Wszyscy gracze zostali zkickowani, zamykam serwer...");
            Bukkit.getScheduler().runTask(Tools.getInstance(), Bukkit::shutdown);
        }).join();
    }

    public static void teleportToPlayer(Player player, UserDataModel target) {
        SectorTransferPacket packet = new SectorTransferPacket();

        UserDataModel user = Tools.getInstance().getUserHandler().getPlayer(player);
        user.setChangingSector(true);

        try {
            user.getUserSynchro().save(player);
        } catch (Exception ignored) {}

        packet.setUserData(user.serialize());
        packet.setTargetName(target.getNick());
        //user.getUserSynchro().save(player);

        Controller.getInstance().getRedis().publish(target.getSector(), (Packet) packet);
        sendToServer(player, target.getSector());

    }

    public static void teleportToLocationSector(Player player, String sectorName, Location loc) {

        SectorTransferPacket packet = new SectorTransferPacket();

        UserDataModel user = Tools.getInstance().getUserHandler().getPlayer(player);
        user.setChangingSector(true);
        try {
            user.getUserSynchro().save(player);
        } catch (Exception ignored) {}

        packet.setUserData(user.serialize());
        packet.setTargetLocation(SerializationHelper.locToString(loc));
        //user.getUserSynchro().save(player);

        Controller.getInstance().getRedis().publish(sectorName, packet);
        sendToServer(player, sectorName);

    }

    public static void transfer(Player player, String sectorName) {
        SectorTransferPacket packet = new SectorTransferPacket();
        UserDataModel user = Tools.getInstance().getUserHandler().getPlayer(player);
        user.setChangingSector(true);

        try {
            user.getUserSynchro().save(player);
        } catch (Exception ignored) {}

        packet.setUserData(user.serialize());


        Controller.getInstance().getRedis().publish(sectorName, packet);
        sendToServer(player, sectorName);

    }


    public static void sendToServer(Player player, String sector) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(sector);

        player.sendPluginMessage((Plugin) Tools.getInstance(), "BungeeCord", output.toByteArray());
    }

    public static void applySynchro(Player player, UserDataModel user) {
        try {
            SerializationHelper.applyPlayerItems((Inventory) player.getInventory(), user.getUserSynchro().getInventory());

            AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealthAttr != null) {
                maxHealthAttr.setBaseValue(user.getUserSynchro().getMaxHealth());
            }

            boolean respawn = (user.getUserSynchro().getHealth() <= 0.0D);
            if (respawn) {
                user.getUserSynchro().setHealth(user.getUserSynchro().getMaxHealth());
            }

            player.setHealth(Math.min(user.getUserSynchro().getHealth(), user.getUserSynchro().getMaxHealth()));
            player.setFoodLevel(user.getUserSynchro().getFoodLevel());
            player.setLevel(user.getUserSynchro().getLevel());
            player.setExp(user.getUserSynchro().getExp());
            player.setGameMode(GameMode.valueOf(user.getUserSynchro().getGameMode()));
            player.setAllowFlight(user.getUserSynchro().isAllowFly());
            player.setFlying(user.getUserSynchro().isFly());

            Bukkit.getScheduler().runTaskLater((Plugin) Tools.getInstance(), () -> player.getInventory().setHeldItemSlot(user.getUserSynchro().getHeldSlot()), 2L);


            player.setFireTicks(user.getUserSynchro().getFireTicks());
            player.setFallDistance(user.getUserSynchro().getFallDistance());
            player.setGliding(user.getUserSynchro().isGliding());
            player.setSwimming(user.getUserSynchro().isSwimming());

            if (user.getUserSynchro().isGlowing() && player.hasPermission("zczb.helper") && !player.hasPotionEffect(PotionEffectType.GLOWING)) {
                player.setGlowing(true);
            }

            Tools.getInstance().getUserHandler().cacheUser(player.getUniqueId(), user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


