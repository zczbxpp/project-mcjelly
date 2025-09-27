package pl.zczb;


import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.event.Subscribe;
import pl.zczb.redis.channels.RedisChannel;


@Plugin(id = "mcjelly-global-controller", name = "mcjelly-global-controller", version = "1.0", authors = {"zczbi"})
public class Velocity {

    private static Velocity instance;
    private Controller core;

    public Velocity() {
        instance = this;

    }

    public static Velocity getInstance() {
        return instance;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        core = new Controller();


    }

    public void onDisable() {
        RedisChannel.INSTANCE.clearOnlinePlayers();
        if (core != null) {
            core.shutdown();
        }
    }
}
