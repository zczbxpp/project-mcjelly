package pl.zczb.packets.subs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.DonateBroadcastPacket;
import pl.zczb.redis.subscriber.RedisSubscriber;

public class DonateBroadcastSubscriber extends RedisSubscriber<DonateBroadcastPacket> {
    public DonateBroadcastSubscriber() {
        super(DonateBroadcastPacket.class, "CH|cashblock_tryb");
    }


    public void onPacketReceived(DonateBroadcastPacket packet) {
        for (Player p : Bukkit.getOnlinePlayers())
            p.sendTitle(GlobalHelper.fixColor("&6&lWsparcie serwera &f#Dziekujemy"), GlobalHelper.fixColor("&7Gracz &f" + packet.getPlayerName() + " &7wsparł serwer doładowując &65vPLN &7do Skrzyni donejtów!"));
    }
}


