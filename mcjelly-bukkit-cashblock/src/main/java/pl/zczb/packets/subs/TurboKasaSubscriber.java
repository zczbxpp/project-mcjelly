package pl.zczb.packets.subs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.objects.impl.TurboManager;
import pl.zczb.cashblock.spigot.commands.admin.TurboKasaCmd;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.TurboKasaPacket;
import pl.zczb.redis.subscriber.RedisSubscriber;

public final class TurboKasaSubscriber extends RedisSubscriber<TurboKasaPacket> {
    public TurboKasaSubscriber() {
        super(TurboKasaPacket.class, "CH|cashblock_tryb");
    }

    public void onPacketReceived(TurboKasaPacket packet) {
        TurboManager.setTurbodrop_admin(packet.getAdmin());
        TurboManager.setTurbodrop_time(Long.valueOf(packet.getTime()));
        TurboManager.getTurbodrop().put(Integer.valueOf(1), "");


        long remainingTime = (packet.getTime() - System.currentTimeMillis()) / 1000L;
        String timeShow = OtherHelper.formatSecs(remainingTime);

        for (Player p : Bukkit.getOnlinePlayers()) {
            TurboKasaCmd.bossBar.setTitle(GlobalHelper.fixColor("&#FF0000☀ | &#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(serwer) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow));
            TurboKasaCmd.bossBar.addPlayer(p);
        }

        Bukkit.broadcastMessage(GlobalHelper.fixColor("&8[&#FFDB0E&l!&8] &7Administrator &#FFDB0E" + packet.getAdmin() + " &7aktywował &#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2"));
    }
}


