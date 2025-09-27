package pl.zczb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.message.LiteMessages;
import lombok.Generated;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import pl.zczb.itemshop.commands.*;
import pl.zczb.itemshop.commands.addons.BaseInvalidUsageHandler;
import pl.zczb.itemshop.commands.addons.UserDataModelResolver;
import pl.zczb.itemshop.data.codec.Converter;
import pl.zczb.itemshop.data.codec.ConverterProvider;
import pl.zczb.itemshop.data.codec.converter.ModelConverter;
import pl.zczb.itemshop.data.user.UserHandler;
import pl.zczb.itemshop.data.user.models.UserDataModel;
import pl.zczb.itemshop.data.user.packets.impl.UserCacheSubscriber;
import pl.zczb.itemshop.events.GlobalPlaceHolder;
import pl.zczb.itemshop.events.UserJoinEvent;
import pl.zczb.itemshop.helpers.GlobalHelper;
import pl.zczb.redis.Redis;
import pl.zczb.redis.RedisCredential;
import pl.zczb.redis.subscriber.RedisSubscriber;

import java.util.logging.Level;

public final class Itemshop extends JavaPlugin implements PluginMessageListener {
    public static Itemshop instance;
    private static Redis redis;

    @Generated
    public static Itemshop getInstance() {
        return instance;
    }

    private MongoClient databaseConnection;
    private LiteCommands<CommandSender> liteCommands;
    private UserHandler userHandler;

    @Generated
    public static Redis getRedis() {
        return redis;
    }


    @Generated
    public UserHandler getUserHandler() {
        return this.userHandler;
    }


    public void onEnable() {
        instance = this;

        redis = new Redis(new RedisCredential("127.0.0.1", 6379));


        initSubs();


        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(new CodecRegistry[]{
                CodecRegistries.fromProviders(new CodecProvider[]{(CodecProvider) new ConverterProvider(new Converter[]{(Converter) new ModelConverter()
                })
                }), MongoClient.getDefaultCodecRegistry()
        });


        MongoDatabase database = (this.databaseConnection = new MongoClient("localhost", (new MongoClientOptions.Builder()).codecRegistry(codecRegistry).build())).getDatabase("database");

        (this.userHandler = new UserHandler(this, database)).initialize();

        this.liteCommands = LiteBukkitFactory.builder("mcjelly-commands", this)
                .commands(
                        new ItemshopCmd(),
                        new ItemshopDiscountCmd()
                )
                .argument(UserDataModel.class, new UserDataModelResolver(this.userHandler))
                .invalidUsage(new BaseInvalidUsageHandler())
                .message(LiteMessages.MISSING_PERMISSIONS, permissions -> GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie posiadasz permisji do tej komendy! (&#FF0000" + permissions.asJoinedText() + "&#FF3F3F)"))
                .message(LiteMessages.INVALID_NUMBER, number -> GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FPodaj prawidłową liczbe")).build();


        registerListeners(new Listener[]{(Listener) new UserJoinEvent()});


        Bukkit.getLogger().log(Level.FINE, "Odpalono Itemshop-platform!");
        (new GlobalPlaceHolder()).register();


        Bukkit.getMessenger().registerIncomingPluginChannel((Plugin) this, "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel((Plugin) this, "BungeeCord");
    }


    public void onDisable() {
        this.userHandler.update();
        if (this.databaseConnection != null) {
            this.databaseConnection.close();
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, (Plugin) this);
        }
    }

    public void initSubs() {
        redis.subscribe("CH|itemshop", (RedisSubscriber) new UserCacheSubscriber());
        redis.subscribe("CH|itemshop", (RedisSubscriber) new ItemshopSubscriber());
        redis.subscribe("CH|itemshop", (RedisSubscriber) new ItemshopBuySubscriber());
        redis.subscribe("CH|itemshop", (RedisSubscriber) new ItemshopDiscountSubscriber());
    }

    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, @NotNull byte[] bytes) {
    }
}


