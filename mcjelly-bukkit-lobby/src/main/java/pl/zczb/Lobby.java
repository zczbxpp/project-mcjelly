package pl.zczb;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.message.LiteMessages;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.configurer.Configurer;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.lobby.config.SectorConfig;
import pl.zczb.lobby.events.*;
import pl.zczb.lobby.queue.QueueManager;
import pl.zczb.lobby.queue.QueueTask;
import pl.zczb.redis.Redis;
import pl.zczb.redis.RedisCredential;
import pl.zczb.sectors.commands.ChannelCmd;
import pl.zczb.sectors.commands.addons.BaseInvalidUsageHandler;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.managers.data.Sector;
import pl.zczb.sectors.managers.data.SectorTypeEnum;
import pl.zczb.sectors.packets.subs.SectorStatsSubscriber;
import pl.zczb.sectors.tasks.SectorUpdateTask;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public final class Lobby extends JavaPlugin implements PluginMessageListener {
    public static Lobby instance;
    private static Redis redis;

    @Generated
    public static Lobby getInstance() {
        return instance;
    }

    private LiteCommands<CommandSender> liteCommands;
    private static SectorConfig sectorConfig;
    private QueueManager queueManager;

    @Generated
    public static Redis getRedis() {
        return redis;
    }


    public static SectorConfig getCfg() {
        return sectorConfig;
    }

    @Generated
    public QueueManager getQueueManager() {
        return this.queueManager;
    }

    public void onEnable() {
        instance = this;

        redis = new Redis(new RedisCredential("127.0.0.1", 6379));


        sectorConfig = (SectorConfig) ConfigManager.create(SectorConfig.class, it -> {
            it.withConfigurer((Configurer) new YamlBukkitConfigurer());

            it.withBindFile(new File(getDataFolder(), "sectors.yml"));
            it.saveDefaults();
            it.load(true);
        });
        loadDefinedSectorsFromOkaeriConfig();
        registerCurrentSector();

        initSubs();



        this.liteCommands = LiteBukkitFactory.builder("mcjelly-commands", this).commands(
                        new ChannelCmd()
                )
                .invalidUsage(new BaseInvalidUsageHandler())
                .message(LiteMessages.MISSING_PERMISSIONS, permissions -> GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie posiadasz permisji do tej komendy! (&#FF0000" + permissions.asJoinedText() + "&#FF3F3F)"))
                .message(LiteMessages.INVALID_NUMBER, number -> GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FPodaj prawidłową liczbe")).build();

        this.queueManager = new QueueManager();

        registerListeners(
                new BlockHandler(),
                new ChatHandler(),
                new JoinQuitHandler(),
                new OthersHandler(queueManager)
//                new ResourcePackHandler()
        );
        Bukkit.getLogger().log(Level.FINE, "Odpalono Lobby-platform!");
        new GlobalPlaceHolder().register();
        getServer().getScheduler().runTaskTimer(this, new QueueTask(queueManager), 20L, 20L);
        this.getServer().getScheduler().runTaskTimerAsynchronously((Plugin) this, (Runnable) new SectorUpdateTask(), 0L, 100L);

//        Bukkit.getScheduler().runTaskTimerAsynchronously(this, (Runnable)new RefreshTask(), 0L, 60L);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks((Plugin) this);
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public void initSubs() {
        redis.subscribe("CH|sector-stats", new SectorStatsSubscriber());
    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, @NotNull byte[] bytes) {

    }

    private void loadDefinedSectorsFromOkaeriConfig() {
        Map<SectorTypeEnum, List<String>> definedSectors = sectorConfig.getDefinedSectors();

        if (definedSectors == null || definedSectors.isEmpty()) {
            getLogger().warning("- Brak zdefiniowanych sektorów w konfiguracji. Żadne sektory nie zostaną zarejestrowane.");

            return;
        }
        definedSectors.forEach((sectorType, sectorNames) -> {
            if (sectorNames == null || sectorNames.isEmpty()) {
                getLogger().warning("- Brak nazw sektorów dla typu: " + sectorType + ". Sekcja zostanie pominięta.");
                return;
            }
            for (String sectorName : sectorNames) {
                boolean initialAdminMode = false;
                SectorManager.createSector(sectorName, sectorType, initialAdminMode);
                getLogger().info("+ Zarejestrowano sektor: " + sectorName + " (Typ: " + sectorType + ", Tryb Admin: " + initialAdminMode + ")");
            }
        });
        getLogger().info("+ Pomyślnie załadowano wszystkie zdefiniowane sektory z konfiguracji.");
    }

    private void registerCurrentSector() {
        SectorConfig.CurrentSectorConfig current = sectorConfig.getCurrentSector();
        if (current == null) {
            getLogger().severe("- Błąd krytyczny: Brak konfiguracji bieżącego sektora! Serwer nie może zostać zarejestrowany.");

            return;
        }
        String sectorName = current.getSectorName();
        SectorTypeEnum sectorType = current.getSectorType();
        boolean adminMode = current.isAdminMode();

        if (SectorManager.isExistsSector(sectorName)) {
            Sector existingSector = SectorManager.getSector(sectorName);
            if (existingSector != null) {
                existingSector.setSectorType(sectorType);
                existingSector.setAdminMode(adminMode);
                existingSector.setOnline(true);
                getLogger().info("+ Zaktualizowano istniejący wpis dla TEGO serwera: " + sectorName + " (Typ: " + sectorType + ", Tryb Admin: " + adminMode + ")");
            }
        } else {
            SectorManager.createSector(sectorName, sectorType, adminMode);
            Sector thisServerSector = SectorManager.getSector(sectorName);
            if (thisServerSector != null) {
                thisServerSector.setOnline(true);
                thisServerSector.setPlayerCount(Bukkit.getOnlinePlayers().size());
            }
            getLogger().info("+ Zarejestrowano TEN serwer jako nowy sektor: " + sectorName + " (Typ: " + sectorType + ", Tryb Admin: " + adminMode + ")");
        }
    }
}


