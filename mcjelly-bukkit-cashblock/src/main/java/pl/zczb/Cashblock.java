package pl.zczb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pl.zczb.cashblock.boss.BossHandler;
import pl.zczb.cashblock.brush.BrushHandler;
import pl.zczb.cashblock.brush.BrushPluginConfiguration;
import pl.zczb.cashblock.config.CashblockConfig;
import pl.zczb.cashblock.database.codec.ConverterProvider;
import pl.zczb.cashblock.database.codec.converter.ModelConverter;
import pl.zczb.cashblock.database.codec.converter.ZbyszekConverter;
import pl.zczb.cashblock.database.user.UserHandler;
import pl.zczb.cashblock.database.user.models.*;
import pl.zczb.cashblock.database.zbyszek.ZbyszekHandler;
import pl.zczb.cashblock.helpers.DataUtil;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.objects.impl.CustomBlockManager;
import pl.zczb.cashblock.objects.impl.PetManager;
import pl.zczb.cashblock.papi.PlaceHolders;
import pl.zczb.cashblock.spigot.commands.addons.BaseInvalidUsageHandler;
import pl.zczb.cashblock.spigot.commands.addons.UserDataModelResolver;
import pl.zczb.cashblock.spigot.commands.admin.*;
import pl.zczb.cashblock.spigot.commands.user.*;
import pl.zczb.cashblock.spigot.events.*;
import pl.zczb.cashblock.spigot.events.event.AntylogoutHandler;
import pl.zczb.cashblock.spigot.events.event.EventHandler;
import pl.zczb.cashblock.spigot.events.gorasiana.GoraSianaManager;
import pl.zczb.cashblock.tops.api.TopManager;
import pl.zczb.cashblock.tops.impl.TopManagerImpl;
import pl.zczb.cashblock.tops.impl.TopTask;
import pl.zczb.itemshop.data.ItemshopRegistry;
import pl.zczb.itemshop.data.ShopItem;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.subs.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public final class Cashblock extends JavaPlugin {

    @Getter
    private static Cashblock instance;

    private MongoClient databaseConnection;

    @Getter
    private UserHandler userHandler;

    @Getter
    private ZbyszekHandler zbyszekHandler;

    private LiteCommands<CommandSender> liteCommands;

    private final BrushPluginConfiguration configuration = new BrushPluginConfiguration();
    @Getter
    private PetManager petHandler;

    @Getter
    private GoraSianaManager goraSianaManager;
    @Getter
    private static CashblockConfig cashblockConfig;

    @Getter
    private TopManager topManager;
    @Getter
    private CustomBlockManager customBlockManager;

    @Override
    public void onEnable() {

        instance = this;

        getLogger().info("Initializing mongo...");
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(
                        new ConverterProvider(
                                new ModelConverter(),
                                new ZbyszekConverter(),
                                new UserLvl.UserLvlConverter(),
                                new UserPets.UserPetsConverter(),
                                new UserHomes.UserHomesConverter(),
                                new UserPrestiz.UserPrestizConverter()
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

        (this.zbyszekHandler = new ZbyszekHandler(
                this,
                database
        )).initialize();
        getLogger().info("Succesfully initialized mongo!");
        saveDefaultConfig();
        this.configuration.loadConfiguration(getConfig());


        cashblockConfig = ConfigManager.create(CashblockConfig.class, it -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit(), new SerdesCommons());
            it.withBindFile(new File(this.getDataFolder(), "cashblock.yml"));
            it.saveDefaults();
            it.load(true);
        });

        this.topManager = new TopManagerImpl(this);
        this.goraSianaManager = new GoraSianaManager(this);
        this.petHandler = new PetManager();
        this.customBlockManager = new CustomBlockManager();

        initSubs();

        getLogger().info("Initializing commands...");
        this.liteCommands = LiteBukkitFactory.builder("mcjelly-commands", this).commands(
                        new DropCmd(),
                        new BossCmd(),
                        new ShopLvlCmd(),
                        new TopkiCmd(),
                        new GoraSianaCmd(this.goraSianaManager),
                        new PrestizCmd(),
                        new ZbyszekCmd(),
                        new WithdrawCmd(),
                        new TestBrushCmd(this.configuration),
                        new RtpCmd(),
                        new PetsCmd(this.petHandler),
                        new LvlCmd(),
                        new KowalCmd(this.configuration),
                        new CxCmd(),
                        new AutoCxCmd(),
                        new GiveBrushCmd(this.configuration),
                        new AiCmd(this.configuration, this.petHandler),
                        new TurboKasaCmd(),
                        new GiveKasaCmd(),
                        new PodlozCmd(customBlockManager)
                )
                .argument(UserDataModel.class, new UserDataModelResolver(this.userHandler))
                .invalidUsage(new BaseInvalidUsageHandler())
                .message(LiteMessages.MISSING_PERMISSIONS, permissions -> GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie posiadasz permisji do tej komendy! (&#FF0000" + permissions.asJoinedText() + "&#FF3F3F)"))
                .message(LiteMessages.INVALID_NUMBER, number -> GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FPodaj prawidłową liczbe")).build();

        getLogger().info("Succesfully initialized all commands!");


        getLogger().info("Initializing listeners...");

        registerListeners(
                new JoinHandler(petHandler),
                new QuitHandler(petHandler),
                new EconomyPickaxeHandler(customBlockManager),
                new BrushHandler(configuration,customBlockManager),
                new SpawnRegionHandler(),
                new InteractHandler(),
                new GoraSianaHandler(this, goraSianaManager),
                new BoostsHandler()
        );
        if (cashblockConfig.getSector_name().equals("cashblock_event")) {
            registerListeners(
                    new AntylogoutHandler(),
                    new EventHandler(),
                    new BossHandler()
            );
        }


        getLogger().info("Succesfully initialized all listeners!");


        zbyszekHandler.createUser("piniata");
        zbyszekHandler.createUser("turbokasa");
        zbyszekHandler.createUser("deszczkluczy");

        new PlaceHolders().register();

        this.getServer().getScheduler().runTaskTimerAsynchronously((Plugin) this, (Runnable) new RefreshTask(), 0L, 40L);
        this.getServer().getScheduler().runTaskTimerAsynchronously((Plugin) this, (Runnable) new TopTask(), 10L, 120L);
        registerItemshop();
    }

    public void initSubs() {
        getLogger().info("Initializing redis subscribers...");

        Controller.getInstance().getRedis().subscribe("CH|cashblock_tryb", new GameCreateAccountSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|cashblock_tryb", new EarnVplnSubscriber());

        Controller.getInstance().getRedis().subscribe("CH|cashblock_tryb", new ManageKasaSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|cashblock_tryb", new TurboKasaUserSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|cashblock_tryb", new TurboKasaSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|cashblock_tryb", new ZbyszekSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|cashblock_tryb", new DonateBroadcastSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|cashblock_tryb", new TopTrybSubscriber());
        Controller.getInstance().getRedis().subscribe("CH|cashblock_tryb", new BossSubscriber());


        getLogger().info("Succesfully initialized all redis subscribers!");
    }

    @Override
    public void onDisable() {
        userHandler.update();
        if (databaseConnection != null) {
            databaseConnection.close();
        }
        Bukkit.getScheduler().cancelTasks((Plugin) this);
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }


    private void registerItemshop() {

        ItemStack vip = ItemBuilder.from(Material.IRON_HELMET)
                .name(Component.text(GlobalHelper.fixColor("&7Ranga &eVIP")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Po zakupie nadaje tobie range")),
                        Component.text(GlobalHelper.fixColor(" &4UWAGA: &CTO NIE JEST VOUCHER")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();

        ItemStack svip = ItemBuilder.from(Material.GOLDEN_HELMET)
                .name(Component.text(GlobalHelper.fixColor("&7Ranga &6SVIP")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Po zakupie nadaje tobie range")),
                        Component.text(GlobalHelper.fixColor(" &4UWAGA: &CTO NIE JEST VOUCHER")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();

        ItemStack mvip = ItemBuilder.from(Material.DIAMOND_HELMET)
                .name(Component.text(GlobalHelper.fixColor("&7Ranga &bMVIP")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Po zakupie nadaje tobie range")),
                        Component.text(GlobalHelper.fixColor(" &4UWAGA: &CTO NIE JEST VOUCHER")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();
        ItemStack jelly = ItemBuilder.from(Material.NETHERITE_HELMET)
                .name(Component.text(GlobalHelper.fixColor("&7Ranga &aJELLY")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Po zakupie nadaje tobie range")),
                        Component.text(GlobalHelper.fixColor(" &4UWAGA: &CTO NIE JEST VOUCHER")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();

        ItemStack zwyklaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &8Zwykłej")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();
        ItemMeta zwyklaMeta = zwyklaItem.getItemMeta();
        zwyklaMeta.setCustomModelData(1001);
        zwyklaItem.setItemMeta(zwyklaMeta);

        GuiItem zwykla = new GuiItem(zwyklaItem);


        ItemStack rzadkaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &3Rzadkiej")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();

        ItemMeta rzadkaMeta = rzadkaItem.getItemMeta();
        rzadkaMeta.setCustomModelData(1002);
        rzadkaItem.setItemMeta(rzadkaMeta);

        GuiItem rzadka = new GuiItem(rzadkaItem);


        ItemStack epickaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &5Epickiej")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();

        ItemMeta epickaMeta = epickaItem.getItemMeta();
        epickaMeta.setCustomModelData(1003);
        epickaItem.setItemMeta(epickaMeta);

        GuiItem epicka = new GuiItem(epickaItem);


        ItemStack legendarnaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &6Legendarnej")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();

        ItemMeta legendarnaMeta = legendarnaItem.getItemMeta();
        legendarnaMeta.setCustomModelData(1004);
        legendarnaItem.setItemMeta(legendarnaMeta);

        GuiItem legendarna = new GuiItem(legendarnaItem);


        ItemStack brushItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &eBrush")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();

        ItemMeta brushMeta = brushItem.getItemMeta();
        brushMeta.setCustomModelData(1008);
        brushItem.setItemMeta(brushMeta);

        GuiItem brush = new GuiItem(brushItem);

        ItemStack petyItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &2Petów")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();
        ItemMeta petyMeta = petyItem.getItemMeta();
        petyMeta.setCustomModelData(1006);
        petyItem.setItemMeta(petyMeta);

        GuiItem pety = new GuiItem(petyItem);


        ItemStack gigaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &4GigaBox")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();
        ItemMeta gigaMeta = gigaItem.getItemMeta();
        gigaMeta.setCustomModelData(1005);
        gigaItem.setItemMeta(gigaMeta);

        GuiItem giga = new GuiItem(gigaItem);


        ItemStack magicznykamien = ItemBuilder.from(Material.DRAGON_EGG)
                .glow()
                .name(Component.text(GlobalHelper.fixColor("&d&lMagiczny kamień")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Kamień potrzebny jest do ulepszenia brusha")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&4UWAGA: &cJest to najcenniejszy przedmiot na serwerze")),
                        Component.text(GlobalHelper.fixColor("&cBrusha możesz ulepszyć pod komendą /kowal")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();

        ItemStack turbo1h = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&bTurboKasa &8(&f1h&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Po zakupie otrzymasz turbokase")),
                        Component.text(GlobalHelper.fixColor(" &7na 1 godzine")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();

        ItemStack turbo30 = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&bTurboKasa &8(&f30 min&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Po zakupie otrzymasz turbokase")),
                        Component.text(GlobalHelper.fixColor(" &7na 30min")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .build();


        ItemStack tof = ItemBuilder.from(Material.PAINTING)
                .name(Component.text(GlobalHelper.fixColor("&6&lTower Of Fortune")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7")),
                        Component.text(GlobalHelper.fixColor(" ")),
                        Component.text(GlobalHelper.fixColor(""))
                ).build();

        ItemStack itemStack = configuration.findByName("mlot").getItemStack().clone();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            meta.setCustomModelData(1001);
            itemStack.setItemMeta(meta);
        }


        List<ShopItem> shopItems = Arrays.asList(
                new ShopItem(vip, 5.0, 10,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "luckperms user " + player.getName() + " parent set vip")),
                new ShopItem(svip, 10.0, 11,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "luckperms user " + player.getName() + " parent set svip")),
                new ShopItem(mvip, 20.0, 12,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "luckperms user " + player.getName() + " parent set mvip")),
                new ShopItem(jelly, 50.0, 13,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "luckperms user " + player.getName() + " parent set donator")),
                new ShopItem(zwykla.getItemStack(), 0.5, 15,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + player.getName() + " zwykla 1")),
                new ShopItem(rzadka.getItemStack(), 1.0, 16,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + player.getName() + " rzadka 1")),
                new ShopItem(epicka.getItemStack(), 2.0, 24,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + player.getName() + " epicka 1")),
                new ShopItem(legendarna.getItemStack(), 4.0, 25,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + player.getName() + " legendarna 1")),
                new ShopItem(brush.getItemStack(), 40.0, 33,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + player.getName() + " brush 1")),
                new ShopItem(pety.getItemStack(), 4.0, 34,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + player.getName() + " pet 1")),
                new ShopItem(giga.getItemStack(), 20.0, 43,
                        player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + player.getName() + " gigabox 1")),
                new ShopItem(magicznykamien, 5.0, 30,
                        player -> player.getInventory().addItem(magicznykamien)),
                new ShopItem(configuration.findByName("3x3").getItemStack(), 30.0, 19,
                        player -> player.getInventory().addItem(OtherHelper.personalizeBrush(configuration.findByName("3x3").getItemStack(), player.getName()))),
                new ShopItem(configuration.findByName("5x5").getItemStack(), 50.0, 20,
                        player -> player.getInventory().addItem(OtherHelper.personalizeBrush(configuration.findByName("5x5").getItemStack(), player.getName()))),
                new ShopItem(configuration.findByName("7x7").getItemStack(), 70.0, 21,
                        player -> player.getInventory().addItem(OtherHelper.personalizeBrush(configuration.findByName("7x7").getItemStack(), player.getName()))),
                new ShopItem(configuration.findByName("9x9").getItemStack(), 90.0, 22,
                        player -> player.getInventory().addItem(OtherHelper.personalizeBrush(configuration.findByName("9x9").getItemStack(), player.getName()))),
                new ShopItem(turbo1h, 6.0, 28,
                        player -> {
                            final long eventtime = DataUtil.parseDateDiff("1h", true);
                            UserDataModel u = getUserHandler().getPlayer(player);
                            u.setTurboDropTime(eventtime);
                            u.setTurboDrop(true);

                            final long remainingTime = (eventtime - System.currentTimeMillis()) / 1000;
                            String timeShow = OtherHelper.formatSecs(remainingTime);


                            BossBar bar = TurboKasaCmd.bossbars.get(player.getName());
                            if (bar != null) {
                                bar.setTitle(
                                        GlobalHelper.fixColor("&#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(twój) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow)
                                );
                                return;
                            }
                            BossBar bossBar = Bukkit.createBossBar(GlobalHelper.fixColor("&#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(twój) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow), BarColor.RED, BarStyle.SOLID);
                            bossBar.addPlayer(player);
                            TurboKasaCmd.bossbars.put(player.getName(), bossBar);
                        }),
                new ShopItem(turbo30, 3, 29,
                        player -> {

                            final long eventtime = DataUtil.parseDateDiff("30min", true);
                            UserDataModel u = getUserHandler().getPlayer(player);
                            u.setTurboDropTime(eventtime);
                            u.setTurboDrop(true);

                            final long remainingTime = (eventtime - System.currentTimeMillis()) / 1000;
                            String timeShow = OtherHelper.formatSecs(remainingTime);


                            BossBar bar = TurboKasaCmd.bossbars.get(player.getName());
                            if (bar != null) {
                                bar.setTitle(
                                        GlobalHelper.fixColor("&#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(twój) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow)
                                );
                                return;
                            }
                            BossBar bossBar = Bukkit.createBossBar(GlobalHelper.fixColor("&#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(twój) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow), BarColor.RED, BarStyle.SOLID);
                            bossBar.addPlayer(player);
                            TurboKasaCmd.bossbars.put(player.getName(), bossBar);

                        }),
                new ShopItem(itemStack, 150.0, 31,
                        player -> player.getInventory().addItem(OtherHelper.personalizeBrush(itemStack, player.getName())))


//
//                new ShopItem(tof, 20.0,37,
//                        player -> player.getInventory().addItem(GlobalHelper.personalizeBrush(tof, player.getName()))


        );


        shopItems.forEach(ItemshopRegistry::registerItem);

    }
}
