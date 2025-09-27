package pl.zczb.packets.subs;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.spigot.commands.admin.TurboKasaCmd;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.TurboKasaUserPacket;
import pl.zczb.redis.subscriber.RedisSubscriber;

public final class TurboKasaUserSubscriber extends RedisSubscriber<TurboKasaUserPacket> {
    public TurboKasaUserSubscriber() {
        super(TurboKasaUserPacket.class, "CH|cashblock_tryb");
    }

    public void onPacketReceived(TurboKasaUserPacket packet) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(packet.getUserName());
        if (u != null) {
            u.setTurboDrop(true);
            u.setTurboDropTime(packet.getTime());
            Player p = Bukkit.getPlayer(packet.getUserName());
            if (p != null && p.isOnline()) {
                long remainingTime = (packet.getTime() - System.currentTimeMillis()) / 1000L;
                String timeShow = OtherHelper.formatSecs(remainingTime);

                BossBar bar = (BossBar) TurboKasaCmd.bossbars.get(p.getName());
                if (bar != null) {
                    bar.setTitle(
                            GlobalHelper.fixColor("&#FF0000☀ | &#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(twój) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow));

                    return;
                }
                BossBar bossBar = Bukkit.createBossBar(GlobalHelper.fixColor("&#FF0000☀ | &#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(twój) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow), BarColor.RED, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);
                bossBar.addPlayer(p);
                TurboKasaCmd.bossbars.put(p.getName(), bossBar);
            }
        }
    }
}


