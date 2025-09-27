package pl.zczb.cashblock.boss;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.zczb.Cashblock;
import pl.zczb.Controller;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.BossPacket;
import pl.zczb.redis.packet.Packet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BossHandler implements Listener {

    public enum BossType {
        GOLEM("&#397FFF&lL&#3483FC&lo&#2F88F8&ld&#298CF5&lo&#2490F2&lw&#1F95EF&ly &#159EE8&lg&#10A2E5&lo&#0AA6E2&ll&#05ABDE&le&#00AFDB&lm", BarColor.BLUE, "&#397FFF"),
        PINIATA("&#FFFC83&lP&#EEF998&li&#DEF6AC&ln&#CDF4C1&li&#BCF1D6&la&#ACEEEA&lt&#9BEBFF&la", BarColor.YELLOW, "&#FFFC83");

        @Getter
        private final String displayName;
        @Getter
        private final BarColor barColor;
        @Getter
        private final String colorString;

        BossType(String displayName, BarColor barColor, String colorString) {
            this.displayName = displayName;
            this.barColor = barColor;
            this.colorString = colorString;
        }
    }

    @Getter
    private static final Map<BossType, Integer> bossHealths = new EnumMap<>(BossType.class);
    @Getter
    private static final Map<BossType, HashMap<UUID, BossDamage>> bossDamages = new EnumMap<>(BossType.class);
    @Getter
    private static final Map<BossType, BossBar> bossBars = new EnumMap<>(BossType.class);
    @Getter
    private static final Map<BossType, Integer> bossMaxHealths = new EnumMap<>(BossType.class);

    @Setter
    @Getter
    private static boolean isLama = false;

    @Getter
    @Setter
    private static boolean isGolem = false;


    static {
        for (BossType type : BossType.values()) {
            bossHealths.put(type, 0);
            bossDamages.put(type, new HashMap<>());
            bossBars.put(type, Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID));
        }
    }

    public static void spawnGolemBoss(int health) {
        setupBoss(BossType.GOLEM, health);
    }

    public static void spawnLamaBoss(Llama lama, int health) {
        setupBoss(BossType.PINIATA, health);
    }

    private static void setupBoss(BossType type, int health) {
        bossHealths.put(type, health);
        BossBar bar = bossBars.get(type);
        bar.setTitle(GlobalHelper.fixColor(type.getColorString() + "☃ | &fBoss "  + type.getColorString() + type.getDisplayName() + " &fposiada " + type.getColorString() + ((Integer)bossHealths.get(type)).intValue()+ "❤"));
        bar.setColor(type.getBarColor());
        bar.setProgress(1.0D);
        bossMaxHealths.put(type, health);
        for (Player player : Bukkit.getOnlinePlayers()) {
            bar.addPlayer(player);
        }
    }

    public static int getBossHealth(BossType type) {
        return bossHealths.getOrDefault(type, 0);
    }

    public static HashMap<UUID, BossDamage> getBossDamages(BossType type) {
        return bossDamages.getOrDefault(type, new HashMap<>());
    }

    public static List<BossDamage> getSortedDamageList(BossType type) {
        List<BossDamage> list = new ArrayList<>(getBossDamages(type).values());
        list.sort(Comparator.comparingInt(BossDamage::getDamage).reversed());
        return list;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        for (BossType type : BossType.values()) {
            bossDamages.get(type).remove(uuid);
        }
    }

    @EventHandler
    public void handleDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager)) {
            return;
        }

        if (MythicBukkit.inst().getMobManager().isMythicMob(event.getEntity())) {
            Optional<ActiveMob> maybeMob = MythicBukkit.inst().getMobManager().getActiveMob(event.getEntity().getUniqueId());
            if (maybeMob.isEmpty()) {
                return;
            }
            ActiveMob mob = maybeMob.get();
            String mobType = mob.getType().getInternalName();

            if (!mobType.equalsIgnoreCase("golem_prismarine_gm_rain")) {
                return;
            }
            event.setDamage(0.0D);
            BossType type = BossType.GOLEM;

            int health = getBossHealth(type);
            if (health <= 0) {
                return;
            }
            bossHealths.put(type, --health);
            BossDamage damage = bossDamages.get(type).computeIfAbsent(damager.getUniqueId(), BossDamage::new);
            damage.addHP();

            MythicBukkit.inst().getAPIHelper().taunt(mob.getEntity().getBukkitEntity(), damager);

            if (health <= 0) {
                finishBossAbstract(type, mob.getEntity());
            } else {
                updateBar(type);
            }

            if (health % 20 == 0) {
                applyGolemEffects(mob.getEntity().getBukkitEntity());
            }
            return;
        }

        if (event.getEntity() instanceof Llama llama) {
            LivingEntity entity = (LivingEntity) event.getEntity();

            double currentHealth = entity.getHealth();
            double maxHealth = Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();

            double newHealth = currentHealth + 10.0D;
            if (newHealth > maxHealth) {
                newHealth = maxHealth;
            }
            entity.setHealth(newHealth);

            event.setDamage(0.0D);
            BossType type = BossType.PINIATA;

            int health = getBossHealth(type);
            if (health <= 0) {
                return;
            }
            bossHealths.put(type, --health);
            BossDamage damage = bossDamages.get(type).computeIfAbsent(damager.getUniqueId(), BossDamage::new);
            damage.addHP();

            llama.setTarget(damager);

            giveLlamaKeyReward(damager);
            giveLlamaShardReward(damager);

            if (health <= 0) {
                finishBoss(type, llama);
            } else {
                updateBar(type);
            }

            if (health % 20 == 0) {
                applyLlamaEffects(llama);
            }
        }
    }

    private void applyGolemEffects(Entity bossEntity) {
        try {
            int randInt = OtherHelper.getRandInt(1, 3);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (bossEntity.getWorld().equals(p.getWorld()) && OtherHelper.isInRadius(bossEntity.getLocation(), p.getLocation(), 5, 5)) {
                    if (randInt == 1) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 80, 2));
                    } else if (randInt == 2) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                    } else if (randInt == 3) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 80, 5));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void giveLlamaKeyReward(Player damager) {
        if (OtherHelper.getChance(0.2D)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + damager.getName() + " legendarna 1");
            damager.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aTrafiłeś na legendarny klucz!"));
        } else if (OtherHelper.getChance(0.4D)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + damager.getName() + " epicka 1");
            damager.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aTrafiłeś na epicki klucz!"));
        } else if (OtherHelper.getChance(0.8D)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + damager.getName() + " rzadka 1");
            damager.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aTrafiłeś na rzadki klucz!"));
        }
    }

    private void giveLlamaShardReward(Player damager) {
        if (OtherHelper.getChance(1.0D)) {
            ItemStack shard = ItemBuilder.from(Material.AMETHYST_SHARD)
                    .setName(GlobalHelper.fixColor("&b&lOdłamek kosmosu"))
                    .setLore(
                            GlobalHelper.fixColor(""),
                            GlobalHelper.fixColor(" &7Jest to odłamek który możesz wpłacić &f/zbyszek"),
                            GlobalHelper.fixColor(" &7oraz możesz ulepszyć nim kilof &aPPM &7trzymajac go!"),
                            GlobalHelper.fixColor("")
                    )
                    .glow()
                    .build();
            damager.getInventory().addItem(shard);
            damager.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aTrafiłeś na odłamek!"));
        }
    }

    private void applyLlamaEffects(Llama llama) {
        try {
            int randInt = OtherHelper.getRandInt(1, 3);
            if (randInt == 1) {
                llama.setGlowing(false);
                llama.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 30, 5));
                llama.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30, 4));
                Bukkit.getScheduler().runTaskLater(Cashblock.getInstance(), () -> llama.setGlowing(true), 40L);
            } else if (randInt == 2) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (llama.getWorld().equals(p.getWorld()) && OtherHelper.isInRadius(llama.getLocation(), p.getLocation(), 5, 5)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                    }
                }
            } else if (randInt == 3) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (llama.getWorld().equals(p.getWorld()) && OtherHelper.isInRadius(llama.getLocation(), p.getLocation(), 5, 5)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 5));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void updateBar(BossType type) {
        BossBar bar = bossBars.get(type);
        int currentHp = bossHealths.getOrDefault(type, 0);
        int maxHp = bossMaxHealths.getOrDefault(type, 1);

        double progress = Math.max(0.0D, Math.min(1.0D, (double) currentHp / maxHp));

        bar.setProgress(progress);
        bar.setTitle(GlobalHelper.fixColor(type.getColorString() + "☃ | &fBoss "  + type.getColorString() + type.getDisplayName() + " &fposiada " + type.getColorString() + currentHp+ "❤"));
    }

    public static void finishBoss(BossType type, LivingEntity boss) {
        bossBars.get(type).removeAll();

        List<BossDamage> list = getSortedDamageList(type);

        if (list.size() > 0) giveReward(list.get(0), 2, type);
        if (list.size() > 1) giveReward(list.get(1), 1, type);
        if (list.size() > 2) giveReward(list.get(2), 0, type);

        boss.remove();
        bossDamages.get(type).clear();

        for (Player po : Bukkit.getOnlinePlayers()) {
            po.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cBoss został pokonany!"));
        }

        Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new BossPacket("die_lama", 0));
    }

    public static void finishBossAbstract(BossType type, AbstractEntity boss) {
        bossBars.get(type).removeAll();

        List<BossDamage> list = getSortedDamageList(type);

        if (list.size() > 0) giveReward(list.get(0), 2, type);
        if (list.size() > 1) giveReward(list.get(1), 1, type);
        if (list.size() > 2) giveReward(list.get(2), 0, type);

        boss.remove();
        bossDamages.get(type).clear();

        for (Player po : Bukkit.getOnlinePlayers()) {
            po.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cBoss został pokonany!"));
        }
        Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new BossPacket("die_golem", 0));
    }

    private static void giveReward(BossDamage bd, int money, BossType type) {
        Player p = Bukkit.getPlayer(bd.getUniqueId());
        if (p == null) {
            return;
        }
        p.sendMessage(GlobalHelper.fixColor("&6Z bossa otrzymałes:"));

        giveLegendaryOrGigaboxKey(p);
        giveMagicStoneReward(p);
        giveAccessoryRewards(p);

        if (type != BossType.PINIATA && money > 0) {
            p.sendMessage(GlobalHelper.fixColor("&7- &e" + money + " zł do portfela"));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "is " + p.getName() + " " + money + " true");
        }

        giveCosmicShardReward(p);
        giveVoucherRewards(p);
    }

    private static void giveLegendaryOrGigaboxKey(Player p) {
        if (OtherHelper.getChance(50.0D)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + p.getName() + " legendarna 2");
            p.sendMessage(GlobalHelper.fixColor("&7- &e2x Legendarne klucze"));
        } else if (OtherHelper.getChance(0.5D)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + p.getName() + " gigabox 1");
            p.sendMessage(GlobalHelper.fixColor("&7- &e1x Klucz do Gigaboxa"));
        }
    }

    private static void giveMagicStoneReward(Player p) {
        if (OtherHelper.getChance(50.0D)) {
            ItemStack reward = ItemBuilder.from(Material.DRAGON_EGG)
                    .amount(1)
                    .glow()
                    .name(Component.text(GlobalHelper.fixColor("&d&lMagiczny kamień")))
                    .lore(
                            Component.text(""),
                            Component.text(GlobalHelper.fixColor(" &7Kamień potrzebny jest do ulepszenia brusha")),
                            Component.text(""),
                            Component.text(GlobalHelper.fixColor("&4UWAGA: &cJest to najcenniejszy przedmiot na serwerze")),
                            Component.text(GlobalHelper.fixColor("&cBrusha możesz ulepszyć pod komendą /kowal")),
                            Component.text("")
                    )
                    .build();
            p.sendMessage(GlobalHelper.fixColor("&7- &e1x Magiczny kamien"));
            p.getInventory().addItem(reward);
        }
    }

    private static void giveAccessoryRewards(Player p) {
        ItemStack glasses = ItemBuilder.from(Material.TURTLE_HELMET)
                .name(Component.text(GlobalHelper.fixColor("&b&lOkularki swagu")))
                .lore(
                        Component.text(""),
                        Component.text(GlobalHelper.fixColor(" &7Ten przedmiot zwieksza drop kasy o &b+x0.25")),
                        Component.text(""),
                        Component.text(GlobalHelper.fixColor("&4UWAGA: &cMusisz zalożyć ten przedmiot")),
                        Component.text("")
                )
                .build();

        ItemStack lornetka = ItemBuilder.from(Material.SPYGLASS)
                .name(Component.text(GlobalHelper.fixColor("&a&lLornetka")))
                .lore(
                        Component.text(""),
                        Component.text(GlobalHelper.fixColor(" &7Ten przedmiot zwieksza drop kasy o &a+x0.25")),
                        Component.text(""),
                        Component.text(GlobalHelper.fixColor("&4UWAGA: &cMusisz trzymać ten przedmiot")),
                        Component.text(GlobalHelper.fixColor("&cw lewej ręce!")),
                        Component.text("")
                )
                .build();

        if (OtherHelper.getChance(20.0D)) {
            p.sendMessage(GlobalHelper.fixColor("&7- &e1x lornetka"));
            p.getInventory().addItem(lornetka);
        } else if (OtherHelper.getChance(15.0D)) {
            p.sendMessage(GlobalHelper.fixColor("&7- &e1x Okularki swagu"));
            p.getInventory().addItem(glasses);
        }
    }

    private static void giveCosmicShardReward(Player p) {
        if (OtherHelper.getChance(25.0D)) {
            ItemStack shard = new pl.zczb.cashblock.helpers.ItemBuilder(Material.AMETHYST_SHARD)
                    .setName(GlobalHelper.fixColor("&b&lOdłamek kosmosu"))
                    .setLore(
                            GlobalHelper.fixColor(""),
                            GlobalHelper.fixColor(" &7Jest to odłamek który możesz wpłacić &f/zbyszek"),
                            GlobalHelper.fixColor(" &7oraz możesz ulepszyć nim kilof &aPPM &7trzymajac go!"),
                            GlobalHelper.fixColor("")
                    )
                    .addEnchant(Enchantment.LUCK, 1)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                    .toItemStack();
            p.getInventory().addItem(shard);
        }
    }

    private static void giveVoucherRewards(Player p) {
        if (OtherHelper.getChance(8.0D)) {
            ItemStack turbo30 = ItemBuilder.from(Material.PAPER)
                    .name(Component.text(GlobalHelper.fixColor("&7Voucher na &bTurboKase &8(&f30 min&8)")))
                    .lore(
                            Component.text(""),
                            Component.text(GlobalHelper.fixColor(" &7Jest to voucher ktory po użyciu")),
                            Component.text(GlobalHelper.fixColor(" &7nadaje turbokase")),
                            Component.text(""),
                            Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                    )
                    .build();
            p.sendMessage(GlobalHelper.fixColor("&7- &e1x Voucher na turbokase"));
            p.getInventory().addItem(turbo30);
        } else if (OtherHelper.getChance(4.0D)) {
            ItemStack fly = ItemBuilder.from(Material.PAPER)
                    .name(Component.text(GlobalHelper.fixColor("&7Voucher na &fFly &8(&f3 dni&8)")))
                    .lore(
                            Component.text(""),
                            Component.text(GlobalHelper.fixColor(" &7Jest to voucher ktory po użyciu")),
                            Component.text(GlobalHelper.fixColor(" &7nadaje możliwość latania na &f3 dni")),
                            Component.text(""),
                            Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                    )
                    .build();
            p.sendMessage(GlobalHelper.fixColor("&7- &e1x Voucher na fly"));
            p.getInventory().addItem(fly);
        } else if (OtherHelper.getChance(2.0D)) {
            ItemStack svipItem = ItemBuilder.from(Material.PAPER)
                    .name(Component.text(GlobalHelper.fixColor("&7Voucher na &6SVIP &8(&f7 dni&8)")))
                    .lore(
                            Component.text(""),
                            Component.text(GlobalHelper.fixColor(" &7Jest to voucher ktory po użyciu")),
                            Component.text(GlobalHelper.fixColor(" &7nadaje rangę na &f7 dni")),
                            Component.text(""),
                            Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                    )
                    .build();
            svipItem.setItemMeta(applyCustomModelData(svipItem.getItemMeta(), 1002));
            p.sendMessage(GlobalHelper.fixColor("&7- &e1x Voucher na svipa"));
            p.getInventory().addItem(svipItem);
        } else if (OtherHelper.getChance(0.5D)) {
            ItemStack mvipItem = ItemBuilder.from(Material.PAPER)
                    .name(Component.text(GlobalHelper.fixColor("&7Voucher na &bMVIP &8(&f7 dni&8)")))
                    .lore(
                            Component.text(""),
                            Component.text(GlobalHelper.fixColor(" &7Jest to voucher ktory po użyciu")),
                            Component.text(GlobalHelper.fixColor(" &7nadaje rangę na &f7 dni")),
                            Component.text(""),
                            Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                    )
                    .build();
            mvipItem.setItemMeta(applyCustomModelData(mvipItem.getItemMeta(), 1003));
            p.sendMessage(GlobalHelper.fixColor("&7- &e1x Voucher na mvipa"));
            p.getInventory().addItem(mvipItem);
        }
    }

    private static ItemMeta applyCustomModelData(ItemMeta meta, int data) {
        if (meta != null) {
            meta.setCustomModelData(data);
        }
        return meta;
    }
}