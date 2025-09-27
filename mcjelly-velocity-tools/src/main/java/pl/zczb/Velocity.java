package pl.zczb;



import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;


import lombok.Getter;
import org.slf4j.Logger;
import pl.zczb.velocity.events.JoinQuitHandler;
import pl.zczb.velocity.events.RedisCleanupTask;
import pl.zczb.velocity.events.ReloadCommand;
import pl.zczb.velocity.events.ServerCommand;
import pl.zczb.velocity.helpers.PluginConfig;
import pl.zczb.redis.channels.RedisChannel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "mcjelly-velocity-tools", name = "mcjelly-velocity-tools", version = "1.0", authors = {"zczbi"})
public class Velocity {

    @Getter
    private static Velocity instance;
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;

    @Getter
    private PluginConfig pluginConfig;

    @Inject
    public Velocity(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        instance = this;

        proxy.getCommandManager().register("motdreload", new ReloadCommand());
        proxy.getCommandManager().register("server", new ServerCommand());

    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        proxy.getEventManager().register(this, new JoinQuitHandler());
        logger.info("Odpalono mcjelly-velocity-tools!");
        loadConfig();
        new RedisCleanupTask(proxy).start();
    }

    public void loadConfig() {
        Path configPath = dataDirectory.resolve("config.yml");

        if (!Files.exists(configPath)) {
            try (InputStream in = getClass().getResourceAsStream("/config.yml")) {
                if (in != null) {
                    Files.copy(in, configPath);
                    logger.info("Zapisano domyślny config.yml.");
                } else {
                    logger.warn("Nie znaleziono domyślnego config.yml w zasobach.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.pluginConfig = new PluginConfig(configPath);
    }

    public void onDisable() {
        RedisChannel.INSTANCE.clearOnlinePlayers();
        RedisChannel.INSTANCE.clearOnlineWartaCash();
    }
}
