package pl.zczb.sectors.managers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import pl.zczb.Lobby;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.sectors.managers.data.Sector;
import pl.zczb.sectors.managers.data.SectorTypeEnum;

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
        String excludedSector = Lobby.getCfg().getCurrentSector().getSectorName();

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


    public static void stopSectorPanic() {
        List<Player> playersToTransfer = new ArrayList<>(Bukkit.getOnlinePlayers());


        CompletableFuture<Void> transferFuture = CompletableFuture.allOf(
                playersToTransfer.stream()
                        .map(player -> CompletableFuture.runAsync(() ->
                                Bukkit.getScheduler().runTask(Lobby.getInstance(), () ->
                                        player.kickPlayer(GlobalHelper.fixColor("&cTwój sektor został wyłączony.\n&cWejdz ponownie!"))
                                )
                        ))
                        .toArray(CompletableFuture[]::new)
        );

        transferFuture.thenRun(() -> {
            System.out.println("Wszyscy gracze zostali zkickowani, zamykam serwer...");
            Bukkit.getScheduler().runTask(Lobby.getInstance(), Bukkit::shutdown);
        }).join();
    }


    public static void sendToServer(Player player, String sector) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(sector);

        player.sendPluginMessage((Plugin) Lobby.getInstance(), "BungeeCord", output.toByteArray());
    }
}


