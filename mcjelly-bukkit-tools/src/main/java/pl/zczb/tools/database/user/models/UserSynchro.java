package pl.zczb.tools.database.user.models;

import lombok.Data;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.sectors.packets.SectorCreateAccountPacket;
import pl.zczb.sectors.serialization.EffectSerializationHelper;
import pl.zczb.sectors.serialization.SerializationHelper;
import pl.zczb.tools.database.codec.CodecHelper;
import pl.zczb.tools.database.codec.Converter;

import java.io.IOException;
import java.util.Objects;

@Data
public class UserSynchro {

    private byte[] inventory;
    private String location;
    private double maxHealth;
    private double health;
    private int foodLevel;
    private float exp;
    private int level;
    private String gameMode;
    private String effects;
    private boolean allowFly;
    private boolean fly;
    private int heldSlot;
    private int fireTicks;
    private float fallDistance;
    private boolean gliding;
    private boolean glowing;
    private boolean swimming;
    private boolean vanish;
    private long lastSave;

    private UserSynchro(byte[] inventory, double maxHealth,
                        double health, int foodLevel, float exp, int level, String gameMode, String effects, boolean allowFly, boolean fly, int heldSlot, int fireTicks,
                        float fallDistance, boolean gliding, boolean glowing, boolean swimming, boolean vanish, long lastSave,String location) {
        this.inventory = inventory;
        this.maxHealth = maxHealth;
        this.health = health;
        this.foodLevel = foodLevel;
        this.exp = exp;
        this.level = level;
        this.gameMode = gameMode;
        this.effects = effects;
        this.allowFly = allowFly;
        this.fly = fly;
        this.heldSlot = heldSlot;
        this.fireTicks = fireTicks;
        this.fallDistance = fallDistance;
        this.gliding = gliding;
        this.glowing = glowing;
        this.swimming = swimming;
        this.vanish = vanish;
        this.lastSave = lastSave;
        this.location = location;

    }

    public static UserSynchro createDefault() {
        return new UserSynchro(null, 0, 0, 0, 0, 0, null, null, false, false, 0, 0, 0, false, false, false, false, 0L,null);
    }

    public void save(Player p) throws IOException {
        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);
        if (u == null) {
            Bukkit.getLogger().severe("Dane uzytkownika '" + p.getName() + "' przepadly!");
            return;
        }

        this.inventory = SerializationHelper.serializeInventoryToBytes(p.getInventory());
        this.location = SerializationHelper.locToString(p.getLocation());
        try {
            this.maxHealth = Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
        } catch (Exception ex) {
            this.maxHealth = 20.0D;
        }

        this.health = p.getHealth();
        this.foodLevel = p.getFoodLevel();
        this.level = p.getLevel();
        this.exp = p.getExp();
        this.gameMode = p.getGameMode().toString();
        this.allowFly = p.getAllowFlight();
        this.fly = p.isFlying() && p.getAllowFlight();
        this.heldSlot = p.getInventory().getHeldItemSlot();
        this.fireTicks = p.getFireTicks();
        this.fallDistance = p.getFallDistance();
        this.gliding = p.isGliding();
        this.glowing = p.isGlowing() && !p.hasPotionEffect(PotionEffectType.GLOWING);
        this.swimming = p.isSwimming();
        this.vanish = false;
        this.effects = EffectSerializationHelper.serializeEffects(p.getActivePotionEffects().stream().toList());
        this.lastSave = System.currentTimeMillis();

        //Bukkit.getLogger().info("saved");
    }

    public void leaveFromGame(Player p) throws IOException {
        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);
        if (u == null) {
            Bukkit.getLogger().severe("Dane uzytkownika '" + p.getName() + "' przepadly!");
            return;
        }
        this.inventory = SerializationHelper.serializeInventoryToBytes(p.getInventory());

        if(u.getSector().equalsIgnoreCase("cashblock_event")){
            this.location = SerializationHelper.locToString(new Location(Bukkit.getWorld("wolrd"),0,121,0));
        } else {
            this.location = SerializationHelper.locToString(p.getLocation());
        }

        try {
            this.maxHealth = Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
        } catch (Exception ex) {
            this.maxHealth = 20.0D;
        }

        this.health = p.getHealth();
        this.foodLevel = p.getFoodLevel();
        this.level = p.getLevel();
        this.exp = p.getExp();
        this.gameMode = p.getGameMode().toString();
        this.allowFly = p.getAllowFlight();
        this.fly = p.isFlying() && p.getAllowFlight();
        this.heldSlot = p.getInventory().getHeldItemSlot();
        this.fireTicks = p.getFireTicks();
        this.fallDistance = p.getFallDistance();
        this.gliding = p.isGliding();
        this.glowing = p.isGlowing() && !p.hasPotionEffect(PotionEffectType.GLOWING);
        this.swimming = p.isSwimming();
        this.vanish = false;
        this.effects = EffectSerializationHelper.serializeEffects(p.getActivePotionEffects().stream().toList());
        this.lastSave = System.currentTimeMillis();


        Tools.getInstance().getUserHandler().updateUser(u);
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new SectorCreateAccountPacket(p.getUniqueId().toString(), Tools.getSectorConfig().getCurrentSector().getSectorName(), u.serialize()));

    }


    public static class UserSynchroConverter implements Converter<UserSynchro> {

        @Override
        public Document encode(UserSynchro userSynchro) {
            Document document = new Document();
            document.put("inventory", userSynchro.inventory);
            document.put("maxHealth", userSynchro.maxHealth);
            document.put("health", userSynchro.health);
            document.put("foodLevel", userSynchro.foodLevel);
            document.put("exp", (double) userSynchro.exp);
            document.put("level", userSynchro.level);
            document.put("gameMode", userSynchro.gameMode);
            document.put("effects", userSynchro.effects);
            document.put("allowFly", userSynchro.allowFly);
            document.put("fly", userSynchro.fly);
            document.put("heldSlot", userSynchro.heldSlot);
            document.put("fireTicks", userSynchro.fireTicks);
            document.put("fallDistance", (double) userSynchro.fallDistance);
            document.put("gliding", userSynchro.gliding);
            document.put("glowing", userSynchro.glowing);
            document.put("swimming", userSynchro.swimming);
            document.put("vanish", userSynchro.vanish);
            document.put("lastSave", userSynchro.lastSave);
            document.put("location", userSynchro.location);
            return document;
        }

        @Override
        public UserSynchro decode(Document document, CodecHelper helper) {
            return new UserSynchro(
                    ((org.bson.types.Binary) document.get("inventory")).getData(),
                    document.getDouble("maxHealth"),
                    document.getDouble("health"),
                    document.getInteger("foodLevel"),
                    safeGetFloat(document, "exp"),
                    document.getInteger("level"),
                    document.getString("gameMode"),
                    document.getString("effects"),
                    document.getBoolean("allowFly"),
                    document.getBoolean("fly"),
                    document.getInteger("heldSlot"),
                    document.getInteger("fireTicks"),
                    safeGetFloat(document, "fallDistance"),
                    document.getBoolean("gliding"),
                    document.getBoolean("glowing"),
                    document.getBoolean("swimming"),
                    document.getBoolean("vanish"),
                    document.getLong("lastSave"),
                    document.getString("location")
            );
        }

        @Override
        public Class<UserSynchro> getConvertedClass() {
            return UserSynchro.class;
        }

        private float safeGetFloat(Document doc, String key) {
            Object value = doc.get(key);
            if (value instanceof Double) return ((Double) value).floatValue();
            if (value instanceof Integer) return ((Integer) value).floatValue();
            return 0.0f;
        }
    }
}

