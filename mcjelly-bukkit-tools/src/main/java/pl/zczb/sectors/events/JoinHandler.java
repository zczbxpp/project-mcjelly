package pl.zczb.sectors.events;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.redis.channels.RedisChannel;
import pl.zczb.redis.packet.Packet;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.packets.SectorCreateAccountPacket;
import pl.zczb.sectors.packets.SectorJoinPacket;
import pl.zczb.sectors.packets.subs.SectorTransferSubscriber;
import pl.zczb.sectors.serialization.SerializationHelper;
import pl.zczb.tools.database.user.UserHandler;
import pl.zczb.tools.database.user.models.UserBans;
import pl.zczb.tools.database.user.models.UserDataModel;
import pl.zczb.tools.objects.impl.WartaManager;
import pl.zczb.tools.spigot.commands.admin.BossbarCmd;
import pl.zczb.tools.spigot.commands.admin.VanishCmd;
import pl.zczb.tools.spigot.commands.groups.MuteCmds;

import java.io.IOException;
import java.util.UUID;

public class JoinHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) throws IOException {

        Player player = event.getPlayer();
        String ipAddress = event.getAddress().getHostAddress();

        if (!player.isOp() && Tools.getSectorConfig().isAdminMode()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, GlobalHelper.fixColor("&cTen serwer jest w trybie administracyjnym."));
            return;
        }

        if (ipAddress == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, GlobalHelper.fixColor("&cWystąpił problem z weryfikacją Twojego adresu IP."));
            return;
        }

        UserHandler userHandler = Tools.getInstance().getUserHandler();
        UserDataModel user = userHandler.getPlayer(player.getUniqueId());

        if (user == null) {
            user = userHandler.createUser(player);
            user.getUserSynchro().leaveFromGame(player);
            if (user == null) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, GlobalHelper.fixColor("&cWystąpił krytyczny błąd podczas tworzenia Twojego profilu."));
                return;
            }

            SectorCreateAccountPacket packet = new SectorCreateAccountPacket(
                    player.getUniqueId().toString(),
                    Tools.getSectorConfig().getCurrentSector().getSectorName(),
                    user.serialize()
            );
            Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), packet);
        }

        UserBans banDetails = user.getUserBans();
        if (banDetails.isBanned()) {
            long banTime = banDetails.getBanTime();
            if (banTime == -1L || System.currentTimeMillis() < banTime) {
                String kickMessage = buildBanKickMessage(banDetails);
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, GlobalHelper.fixColor(kickMessage));
                return;
            } else {
                unbanUser(banDetails);
                userHandler.updateUser(user);
            }
        }

        for (UserDataModel otherUser : userHandler.getUsers()) {
            if (otherUser.getUuid().equals(player.getUniqueId())) continue;

            UserBans otherBanDetails = otherUser.getUserBans();
            if (otherBanDetails.isBanned() && ipAddress.equals(otherBanDetails.getIpAddress())) {
                long otherBanTime = otherBanDetails.getBanTime();
                if (otherBanTime == -1L || System.currentTimeMillis() < otherBanTime) {
                    String kickMessage = GlobalHelper.fixColor(
                            "\n&#980000Zostałeś zbanowany!\n" +
                                    "&#C64949Powód: &#980000Współdzielenie adresu IP z zbanowanym kontem (" + otherUser.getNick() + ").\n" +
                                    "&#C64949Administrator: &#980000" + otherBanDetails.getBanAdmin() + "\n" +
                                    "&#C64949Wygasa: &#980000" + MuteCmds.formatRemainingTime(otherBanTime)
                    );
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, kickMessage);
                    return;
                }
            }
        }

        boolean needsUpdate = false;
        if (banDetails.getIpAddress() == null || !banDetails.getIpAddress().equals(ipAddress)) {
            banDetails.setIpAddress(ipAddress);
            needsUpdate = true;
        }
        if (!user.getNick().equalsIgnoreCase(player.getName())) {
            user.setNick(player.getName());
            needsUpdate = true;
        }
        if (needsUpdate) {
            userHandler.updateUser(user);
        }
    }

    private String buildBanKickMessage(UserBans banDetails) {
        return "\n&#980000Zostałeś zbanowany!\n" +
                "&#C64949Powód: &#980000" + banDetails.getBanMessage() + "\n" +
                "&#C64949Administrator: &#980000" + banDetails.getBanAdmin() + "\n" +
                "&#C64949Wygasa: &#980000" + MuteCmds.formatRemainingTime(banDetails.getBanTime());
    }

    private void unbanUser(UserBans banDetails) {
        banDetails.setBanned(false);
        banDetails.setBanTime(0L);
        banDetails.setBanMessage(null);
        banDetails.setBanAdmin(null);
    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        event.setJoinMessage(null);


        if (VanishCmd.getVanished().contains(player)) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.hasPermission("zczb.helper"))
                    online.hidePlayer(player);
            }
        } else {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.hasPermission("zczb.helper")) {
                    online.showPlayer(player);
                }
            }
        }
        if (Tools.getSectorConfig().isBossbar()) {
            BossbarCmd.bossBar.addPlayer(player);
        }

        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(player);

        if (u.getJoinTime() == 0L) {
            u.setJoinTime(System.currentTimeMillis());
        }
        u.setChangingSector(false);
        if (u.isWarta()) {
            long czasTeraz = System.currentTimeMillis();
            long czasMinelo = czasTeraz - u.getWartaTime();

            int sekundy = (int) (czasMinelo / 1000L);

            double plnDoDodania = (sekundy / 3600) * WartaManager.losujKwote();

            if (plnDoDodania > 0.0D) {
                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "is " + player.getName() + " " + plnDoDodania + " true");
            }
            player.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&fZarobiłeś &e" + plnDoDodania + " zl &fz warty do portfela!"));

            u.setWarta(false);
            u.setWartaTime(0L);

            RedisChannel.INSTANCE.removeOnlineWartaCash(Integer.valueOf(1));
            Tools.getInstance().getUserHandler().updateUser(u);
        }


        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent) new TextComponent(GlobalHelper.fixColor("&fSprawdz najnowsze zmiany i aktualizacje pod &a/zmiany")));
    }





    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new SectorJoinPacket(player.getName(), Tools.getSectorConfig().getCurrentSector().getSectorName()));

        if (SectorTransferSubscriber.pendingTransfers.containsKey(playerUUID)) {
            String userData = SectorTransferSubscriber.pendingTransfers.remove(playerUUID);
            UserDataModel user = UserDataModel.deserialize(userData);
            user.setChangingSector(false);
            SectorManager.applySynchro(player, user);

            event.setSpawnLocation(SerializationHelper.stringToLoc(user.getUserSynchro().getLocation()));
            return;
        }

        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(player);
        SectorManager.applySynchro(player, u);

        Bukkit.getLogger().info(u.getSector());
        if(u.getSector().equalsIgnoreCase("cashblock_event")){
            player.setAllowFlight(false);
            player.setFlying(false);
            event.setSpawnLocation(SerializationHelper.stringToLoc(Tools.getSectorConfig().getSpawn()));
            return;
        }

        Location loc = SerializationHelper.stringToLoc(u.getUserSynchro().getLocation());
        event.setSpawnLocation(loc);

    }
}


