package pl.zczb.packets.subs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.MutePacket;
import pl.zczb.redis.packet.Packet;
import pl.zczb.redis.subscriber.RedisSubscriber;
import pl.zczb.tools.database.user.models.UserBans;
import pl.zczb.tools.database.user.models.UserDataModel;

import pl.zczb.tools.spigot.commands.groups.MuteCmds;

public final class MuteSubscriber extends RedisSubscriber<MutePacket> {

    public MuteSubscriber() {
        super(MutePacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
    }

    @Override
    public void onPacketReceived(MutePacket packet) {
        UserDataModel data = Tools.getInstance().getUserHandler().getPlayer(packet.getNick());
        if (data == null) {
            return;
        }

        UserBans bans = data.getUserBans();
        Player player = Bukkit.getPlayer(packet.getNick());

        switch (packet.getType().toLowerCase()) {
            case "mute":
                bans.setMuted(true);
                bans.setMuteTime(packet.getCzas());
                bans.setMuteMessage(packet.getPowod());
                bans.setMuteAdmin(packet.getAdmin());

                if (player != null && player.isOnline()) {
                    player.sendMessage(GlobalHelper.fixColor("&8[&#980000&l!&8] &#C64949Zostałeś wyciszony przez &#980000" + packet.getAdmin() + " &#C64949powód &#980000" + packet.getPowod()));
                }
                break;

            case "unmute":
                bans.setMuted(false);
                bans.setMuteMessage(null);
                bans.setMuteTime(null);
                bans.setMuteAdmin(null);

                if (player != null && player.isOnline()) {
                    player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Zostałeś odciszony przez &2" + packet.getAdmin()));
                }
                break;

            default:
                System.out.println("Otrzymano nieznany typ pakietu MutePacket: " + packet.getType());
                break;
        }
        Tools.getInstance().getUserHandler().updateUser(data);
    }
}