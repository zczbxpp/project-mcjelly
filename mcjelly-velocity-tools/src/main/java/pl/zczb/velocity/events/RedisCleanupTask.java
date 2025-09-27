package pl.zczb.velocity.events;

import com.velocitypowered.api.proxy.ProxyServer;
import pl.zczb.Velocity;
import pl.zczb.redis.channels.RedisChannel;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RedisCleanupTask {

    private final ProxyServer proxyServer;

    public RedisCleanupTask(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    public void start() {
        proxyServer.getScheduler().buildTask(Velocity.getInstance(), () -> {
            Set<String> redisPlayers = RedisChannel.INSTANCE.getOnlinePlayers();
            Set<String> onlinePlayers = proxyServer.getAllPlayers().stream()
                    .map(player -> player.getUsername())
                    .collect(Collectors.toSet());


            redisPlayers.stream()
                    .filter(name -> !onlinePlayers.contains(name))
                    .forEach(name -> RedisChannel.INSTANCE.removeOnlinePlayer(name));

        }).repeat(30, TimeUnit.SECONDS).schedule();
    }
}