package pl.zczb.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import pl.zczb.Velocity;
import pl.zczb.redis.channels.RedisChannel;

import java.util.Set;

public class JoinQuitHandler {
    @Subscribe
    public void onPlayerJoin(LoginEvent event) {
        String playerName = event.getPlayer().getUsername();
        Set<String> onlinePlayers = RedisChannel.INSTANCE.getOnlinePlayers();
        if (!onlinePlayers.contains(playerName))
            RedisChannel.INSTANCE.addOnlinePlayer(playerName);
    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        String playerName = event.getPlayer().getUsername();
        RedisChannel.INSTANCE.removeOnlinePlayer(playerName);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        RedisChannel.INSTANCE.clearOnlinePlayers();
        RedisChannel.INSTANCE.clearOnlineWartaCash();
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        ServerPing.Builder ping = event.getPing().asBuilder();


        int onlineWartaCash = RedisChannel.INSTANCE.getOnlineWartaCash();
        int onlinePlayers = event.getPing().getPlayers().map(ServerPing.Players::getOnline).orElse(0);
        int totalOnline = onlinePlayers + onlineWartaCash;
        int maxPlayers = totalOnline + 1;
        String motd = Velocity.getInstance().getPluginConfig().getMotd();
        ping.description(LegacyComponentSerializer.legacySection().deserialize(motd));

        ping.onlinePlayers(totalOnline);
        ping.maximumPlayers(maxPlayers);
        ping.version(new ServerPing.Version(1337,"§7" + totalOnline + "§8/§7" + maxPlayers));

        event.setPing(ping.build());
    }

}
