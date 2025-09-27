package pl.zczb;


import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBase;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.message.LiteMessages;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.configurer.Configurer;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.subs.*;
import pl.zczb.redis.channels.RedisChannel;
import pl.zczb.redis.subscriber.RedisSubscriber;
import pl.zczb.sectors.commands.ChannelCmd;
import pl.zczb.sectors.commands.StopCmd;
import pl.zczb.sectors.events.JoinHandler;
import pl.zczb.sectors.events.QuitHandler;
import pl.zczb.sectors.events.SecureHandler;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.managers.data.Sector;
import pl.zczb.sectors.managers.data.SectorTypeEnum;
import pl.zczb.sectors.packets.subs.SectorCreateAccountSubscriber;
import pl.zczb.sectors.packets.subs.SectorJoinSubscriber;
import pl.zczb.sectors.packets.subs.SectorStatsSubscriber;
import pl.zczb.sectors.packets.subs.SectorTransferSubscriber;
import pl.zczb.sectors.tasks.SectorUpdateTask;
import pl.zczb.tools.config.*;
import pl.zczb.tools.config.serdes.ChangelogSerdes;
import pl.zczb.tools.config.serdes.HelpSerdes;
import pl.zczb.tools.config.serdes.KitSerdes;
import pl.zczb.tools.config.serdes.WarpSerdes;
import pl.zczb.tools.database.codec.ConverterProvider;
import pl.zczb.tools.database.codec.converter.ModelConverter;
import pl.zczb.tools.database.user.UserHandler;
import pl.zczb.tools.database.user.models.*;
import pl.zczb.tools.papi.CorePlaceHolder;
import pl.zczb.tools.spigot.commands.addons.BaseInvalidUsageHandler;
import pl.zczb.tools.spigot.commands.addons.DurationArgument;
import pl.zczb.tools.spigot.commands.addons.UserDataModelResolver;
import pl.zczb.tools.spigot.commands.admin.*;
import pl.zczb.tools.spigot.commands.groups.*;
import pl.zczb.tools.spigot.commands.user.*;
import pl.zczb.tools.spigot.commands.vip.HatCmd;
import pl.zczb.tools.spigot.events.CheckHandler;
import pl.zczb.tools.spigot.events.EnderchestHandler;
import pl.zczb.tools.spigot.events.antyafk.AntyAfkHandler;
import pl.zczb.tools.spigot.events.antyafk.AntyAfkTask;
import pl.zczb.tools.spigot.events.antygrief.ArmorStandHandler;
import pl.zczb.tools.spigot.events.antygrief.RedstoneHandler;
import pl.zczb.tools.spigot.events.autoevents.chatquiz.ChatQuizHandler;
import pl.zczb.tools.spigot.events.autoevents.chatquiz.QuizManager;
import pl.zczb.tools.spigot.events.autoevents.chatquiz.QuizTask;
import pl.zczb.tools.spigot.events.chat.ChatHandler;
import pl.zczb.tools.spigot.events.clear.AutoMessageTask;
import pl.zczb.tools.spigot.events.clear.CheckTask;
import pl.zczb.tools.spigot.events.clear.MapClearTask;
import pl.zczb.tools.spigot.events.spawn.AfkRegionHandler;
import pl.zczb.tools.spigot.events.spawn.ParkourHandler;
import pl.zczb.tools.tops.api.TopManager;
import pl.zczb.tools.tops.impl.TopManagerImpl;
import pl.zczb.tools.tops.impl.TopTask;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public final class Tools extends JavaPlugin implements PluginMessageListener {

    @Getter
    private static Tools instance;

    private MongoClient databaseConnection;

    @Getter
    private UserHandler userHandler;



    private LiteCommands<CommandSender> liteCommands;

    @Getter
    private static SectorConfig sectorConfig;
    @Getter
    private static WarpConfig warpConfig;
    @Getter
    private static RangiConfig rangiConfig;
    @Getter
    private static PomocConfig pomocConfig;
    @Getter
    private static KitConfig kitConfig;
    @Getter
    private static TopConfig topConfig;
    @Getter
    private static ShopConfig shopConfig;
    @Getter
    private static ChangelogConfig changelogConfig;
    @Getter
    private TopManager topManager;
    @Getter
    private QuizManager quizManager;


    public void onEnable() {
        instance = this;
        getLogger().info("Initializing config...");
        changelogConfig = ConfigManager.create(ChangelogConfig.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withSerdesPack(registry -> {
                registry.register(new ChangelogSerdes());
            });
            it.withBindFile(new File(this.getDataFolder(), "changelogs.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
        sectorConfig = ConfigManager.create(SectorConfig.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(getDataFolder(), "sectors.yml"));
            it.saveDefaults();
            it.load(true);
        });

        warpConfig = ConfigManager.create(WarpConfig.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withSerdesPack(registry -> {
                registry.register(new WarpSerdes());
            });

            it.withBindFile(new File(getDataFolder(), "warps.yml"));

            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
        rangiConfig = ConfigManager.create(RangiConfig.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withSerdesPack(registry -> {
                registry.register(new HelpSerdes());
            });

            it.withBindFile(new File(getDataFolder(), "rangi.yml"));

            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
        pomocConfig = ConfigManager.create(PomocConfig.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withSerdesPack(registry -> {
                registry.register(new HelpSerdes());
            });

            it.withBindFile(new File(getDataFolder(), "pomoc.yml"));

            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
        topConfig = ConfigManager.create(TopConfig.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withSerdesPack(registry -> {
                registry.register(new HelpSerdes());
            });

            it.withBindFile(new File(getDataFolder(), "topki.yml"));

            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
        kitConfig = ConfigManager.create(KitConfig.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withSerdesPack(registry -> {
                registry.register(new KitSerdes());
            });

            it.withBindFile(new File(getDataFolder(), "kits.yml"));

            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
        shopConfig = ConfigManager.create(ShopConfig.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withSerdesPack(registry -> {
                registry.register(new HelpSerdes());
            });

            it.withBindFile(new File(getDataFolder(), "shops.yml"));

            it.withRemoveOrphans(true);

            it.saveDefaults();

            it.load(true);
        });

        getLogger().info("Succesfully initialized config!");

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(
                        new ConverterProvider(
                                new ModelConverter(),
                                new UserSynchro.UserSynchroConverter(),
                                new UserKits.UserKitsConverter(),
                                new UserBans.UserBansConverter(),
                                new UserEnderchests.UserEnderchestsConverter()
                        )
                ), MongoClient.getDefaultCodecRegistry());

        MongoDatabase database = (databaseConnection = new MongoClient(
                "localhost",
                new MongoClientOptions.Builder()
                        .codecRegistry(codecRegistry)
                        .build()
        )).getDatabase("database");

        (this.userHandler = new UserHandler(
                this,
                database
        )).initialize();
        getLogger().info("Succesfully initialized mongo!");


        this.quizManager = new QuizManager();

        loadDefinedSectorsFromOkaeriConfig();
        registerCurrentSector();

        initSubs();

        this.topManager = (TopManager) new TopManagerImpl(this);
        getLogger().info("Initializing commands...");


        this.liteCommands = LiteBukkitFactory.builder("mcjelly-commands", (Plugin) this).commands(
                        new BossbarCmd(),
                        new BroadcastCmd(),
                        new ChatCmd(),
                        new EnchantCmd(),
                        new FeedCmd(),
                        new FlyCmd(),
                        new GamemodeCmd(),
                        new InvseeCmd(),
                        new KickCmd(),
                        new NagrodaAdmCmd(),
                        new VanishCmd(),
                        new BanCmds(),
                        new CheckCmds(),
                        new MessageCmds(),
                        new MuteCmds(),
                        new TeleportCmds(),
                        new AfkCmd(),
                        new ChangelogCmd(),
                        new EcCmd(),
                        new GammaCmd(),
                        new HelpCmd(),
                        new HelpopCmd(),
                        new KitCmd(),
                        new LobbyCmd(),
                        new NagrodaCmd(),
                        new RangiCmd(),
                        new RepairCmd(),
                        new ShopCmd(),
                        new SpawnCmd(),
                        new TopCmd(),
                        new TopkiCmd(),
                        new TrashCmd(),
                        new WarpCmd(),
                        new WartaCmd(),
                        new WbCmd(),
                        new HatCmd(),
                        new ChannelCmd(),
                        new StopCmd(),
                        new EcCmd()
                ).argument(UserDataModel.class, new UserDataModelResolver(this.userHandler))
                .argument(Duration.class, new DurationArgument())
                .invalidUsage(new BaseInvalidUsageHandler())
                .message(LiteMessages.MISSING_PERMISSIONS, permissions -> GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie posiadasz permisji do tej komendy! (&#FF0000" + permissions.asJoinedText() + "&#FF3F3F)"))
                .message(LiteMessages.INVALID_NUMBER, number -> GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FPodaj prawidłową liczbę!"))
                .build();

        getLogger().info("Succesfully initialized all commands!");


        getLogger().info("Initializing listeners...");

        registerListeners(
                new JoinHandler(),
                new QuitHandler(),
                new SecureHandler(),
                new ArmorStandHandler(),
                new RedstoneHandler(),
                new ChatHandler(),
                new AfkRegionHandler(),
                new ParkourHandler(),
                new CheckHandler(),
                new EnderchestHandler(),
                new ChatQuizHandler(this.quizManager)
        );


        getLogger().info("Succesfully initialized all listeners!");


        loadRegions();

        (new CorePlaceHolder()).register();

        getServer().getScheduler().runTaskTimerAsynchronously((Plugin) this, (Runnable) new AutoMessageTask(), 0L, 3200L);
        getServer().getScheduler().runTaskTimerAsynchronously((Plugin) this, (Runnable) new MapClearTask(), 0L, 1200L);
        getServer().getScheduler().runTaskTimerAsynchronously((Plugin) this, (Runnable) new SectorUpdateTask(), 0L, 100L);
        getServer().getScheduler().runTaskTimerAsynchronously((Plugin) this, (Runnable) new TopTask(), 10L, 120L);
        getServer().getScheduler().runTaskTimerAsynchronously((Plugin) this, (Runnable) new QuizTask(this.quizManager), 36000L, 36000L);

        getServer().getScheduler().runTaskTimerAsynchronously((Plugin) this, (Runnable) new CheckTask(), 0L, 40L);

        AntyAfkHandler antyAfkHandler = new AntyAfkHandler();
        getServer().getPluginManager().registerEvents((Listener) antyAfkHandler, (Plugin) this);

        (new AntyAfkTask(this, antyAfkHandler.getAfkStateMap()))
                .runTaskTimerAsynchronously((Plugin) this, 0L, 20L);


        RedisChannel.INSTANCE.setOnlineWartaCash(Integer.valueOf((int) this.userHandler.getCountPlayersWarta()));

        Bukkit.getMessenger().registerIncomingPluginChannel((Plugin) this, "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel((Plugin) this, "BungeeCord");
    }


    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
    }


    public void initSubs() {
        getLogger().info("Initializing redis subscribers...");

        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new SectorCreateAccountSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new ChatManageSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new BroadcastSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new BossbarSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new KickSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new SectorJoinSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new HelpopSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new MsgSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new ChatSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new TopSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new MuteSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new BanSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new NagrodaAdmSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new TpaRequestSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new TpaAcceptSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new TpaAcceptAllSubscriber());

        Controller.getInstance().getRedis().subscribe("CH|" + getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new CheckSubscriber());


        Controller.getInstance().getRedis().subscribe("CH|sector-stats", new SectorStatsSubscriber());

        Controller.getInstance().getRedis().subscribe(sectorConfig.getCurrentSector().getSectorName(), new SectorTransferSubscriber());
        Controller.getInstance().getRedis().subscribe(sectorConfig.getCurrentSector().getSectorName(), new STeleportSubscriber());

        getLogger().info("Succesfully initialized all redis subscribers!");
    }


    public void onDisable() {
        this.userHandler.update();
        if (this.databaseConnection != null) {
            this.databaseConnection.close();
        }
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }
        Bukkit.getWorlds().forEach(World::save);
        Bukkit.getScheduler().cancelTasks((Plugin) this);
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, (Plugin) this);
        }
    }


    private void loadRegions() {
        for (World world2 : Bukkit.getWorlds()) {
            world2.setGameRule(GameRule.DO_MOB_SPAWNING, Boolean.valueOf(false));
            world2.setGameRule(GameRule.DO_TRADER_SPAWNING, Boolean.valueOf(false));
            world2.setGameRule(GameRule.DO_PATROL_SPAWNING, Boolean.valueOf(false));

            world2.setGameRule(GameRule.SPAWN_RADIUS, Integer.valueOf(0));
            world2.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, Boolean.valueOf(true));
            world2.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, Boolean.valueOf(false));
        }
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


