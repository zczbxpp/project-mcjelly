package pl.zczb.cashblock.spigot.events;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import pl.zczb.Cashblock;
import pl.zczb.Controller;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.objects.CustomBlock;
import pl.zczb.cashblock.objects.impl.CustomBlockManager;
import pl.zczb.cashblock.objects.impl.PickaxeManager;
import pl.zczb.cashblock.objects.impl.TurboManager;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.cashblock.helpers.ItemBuilder;
import pl.zczb.packets.EarnVplnPacket;

import java.text.DecimalFormat;
import java.util.Map;

public class EconomyPickaxeHandler implements Listener {
    static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private final CustomBlockManager cashBlockManager;

    public EconomyPickaxeHandler(CustomBlockManager cashBlockManager) {
        this.cashBlockManager = cashBlockManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block brokenBlock = event.getBlock();
        Location blockLocation = brokenBlock.getLocation();

        CustomBlock cashBlock = cashBlockManager.getCashBlock(blockLocation);

        if (cashBlock != null) {
            cashBlockManager.removeCashBlock(blockLocation);

            double amount = cashBlock.getAmount();
            UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);

            p.sendTitle(GlobalHelper.fixColor("\uE822 Wykopałeś &8(&a" + decimalFormat.format(amount) + " vPLN&8) &f\uE822"),
                    GlobalHelper.fixColor("&fStan konta &8(&e/portfel&8) &fzarabiaj &6&lWIĘCEJ &f/rangi &8(&3+5 exp.&8)"));

            u.addPln(amount);
            p.playSound(p.getLocation(), "custom.launch1", 0.25f, 1.0f);
            Controller.getInstance().getRedis().publish("CH|cashblock_tryb", new EarnVplnPacket(p.getName(), decimalFormat.format(amount)));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    private void onKilofUseInteract(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player p = event.getPlayer();

        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);


        ItemStack itemStack = p.getInventory().getItemInMainHand();
        if (PickaxeManager.isPersonalPickaxe(itemStack)) {
            if (!PickaxeManager.isOwner(itemStack, p)) {
                event.setCancelled(true);
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie jesteś właścicielem kilofa!"));
                return;
            }
            if (OtherHelper.getChance((double) PickaxeManager.getUpgradeLevel(itemStack, PickaxeManager.UpgradeType.EXPLOSION) / 1000)) {
                launchFakeTNT(p, event.getBlock().getLocation());
                p.sendMessage(GlobalHelper.fixColor("&8[&6&l!&8] &7Użyto umiejętności &4Eksplozji"));

            } else if (OtherHelper.getChance((double) PickaxeManager.getUpgradeLevel(itemStack, PickaxeManager.UpgradeType.PYROMANIAC) / 1000)) {
                p.sendMessage(GlobalHelper.fixColor("&8[&6&l!&8] &7Użyto umiejętności &cPiromana"));
                BlockFace face = p.getFacing();
                Location origin = event.getBlock().getLocation();
                World world = p.getWorld();

                for (int i = 1; i <= 7; i++) {
                    Location loc = origin.clone().add(face.getModX() * i, 0, face.getModZ() * i);
                    Block block = world.getBlockAt(loc);

                    world.spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 0.5, 0.5), 10, 0.1, 0.1, 0.1, 0);

                    if (!block.getType().isAir()) {
                        block.setType(Material.AIR);
                    }
                }

            }

        }

        Block brokenBlock = event.getBlock();
        Location blockLocation = brokenBlock.getLocation();

        CustomBlock cashBlock = cashBlockManager.getCashBlock(blockLocation);

        if (cashBlock != null) {
            cashBlockManager.removeCashBlock(blockLocation);

            double amount = cashBlock.getAmount();

            p.sendTitle(GlobalHelper.fixColor("\uE822 Wykopałeś &8(&a" + decimalFormat.format(amount) + " vPLN&8) &f\uE822"),
                    GlobalHelper.fixColor("&fStan konta &8(&e/portfel&8) &fzarabiaj &6&lWIĘCEJ &f/rangi &8(&3+5 exp.&8)"));

            u.addPln(amount);
            p.playSound(p.getLocation(), "custom.launch1", 0.25f, 1.0f);
            Controller.getInstance().getRedis().publish("CH|cashblock_tryb", new EarnVplnPacket(p.getName(), decimalFormat.format(amount)));
        }

        double reward = getReward(u);

        if (OtherHelper.getChance(0.1)) {
            ItemStack shard = new ItemBuilder(Material.AMETHYST_SHARD)
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
            Map<Integer, ItemStack> leftover = p.getInventory().addItem(shard);


            if (!leftover.isEmpty()) {
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cMasz pełne eq odłamek wypadł na ziemie"));
                for (ItemStack item : leftover.values()) {
                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                }
            } else {
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&fGratulacje &7trafiłeś na &d&lOdłamek kosmosu"));
            }

        }


        u.addStone(1);
        if (reward == 0) {
            addXp(p, 0.6);
            return;
        }

        reward *= u.getMnoznik();
        if (u.isTurboDrop() || TurboManager.isOnTurboDrop()) reward *= 2;

        double xpToAdd = reward * 50;
        addXp(p, xpToAdd);


        p.sendTitle(GlobalHelper.fixColor("\uE822 Wykopałeś &8(&a" + decimalFormat.format(reward) + " vPLN&8) &f\uE822"),
                GlobalHelper.fixColor("&fStan konta &8(&e/portfel&8) &fzarabiaj &6&lWIĘCEJ &f/rangi &8(&3+" + decimalFormat.format(xpToAdd) + " exp.&8)"));


        u.addPln(reward);
        p.playSound(p.getLocation(), "custom.launch1", 0.25f, 1.0f);
        Controller.getInstance().getRedis().publish("CH|cashblock_tryb", new EarnVplnPacket(p.getName(), decimalFormat.format(reward)));

    }

    public void launchFakeTNT(Player player, Location startLoc) {
        World world = player.getWorld();

        ArmorStand tntStand = (ArmorStand) world.spawn(startLoc.clone().add(0, 1, 0), ArmorStand.class);
        tntStand.setVisible(false);
        tntStand.setGravity(true);
        tntStand.setMarker(true);
        tntStand.setSmall(true);
        tntStand.setInvulnerable(true);
        tntStand.setBasePlate(false);
        tntStand.setCanPickupItems(false);
        tntStand.setSilent(true);
        tntStand.setArms(false);
        tntStand.setHelmet(new ItemStack(Material.TNT));


        Vector direction = player.getLocation().getDirection().normalize();
        direction.setY(0.3);
        tntStand.setVelocity(direction.multiply(0.5));

        Bukkit.getScheduler().runTaskLater(Cashblock.getInstance(), () -> {
            Location explosionLoc = tntStand.getLocation();

            world.spawnParticle(Particle.EXPLOSION_LARGE, explosionLoc, 1);
            world.playSound(explosionLoc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);


            int radius = 2;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Location blockLoc = explosionLoc.clone().add(x, y, z);
                        Block block = blockLoc.getBlock();

                        if (block.getType().isSolid() && block.getType() != Material.BEDROCK) {
                            block.breakNaturally();
                        }
                    }
                }
            }

            tntStand.remove();

        }, 30L);
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (!tntStand.isValid() || ticks >= 30) {
                    cancel();
                    return;
                }

                world.spawnParticle(Particle.SMOKE_NORMAL, tntStand.getLocation(), 2, 0.05, 0.05, 0.05, 0.01);
                ticks += 2;
            }
        }.runTaskTimer(Cashblock.getInstance(), 0L, 2L);
    }


    private double getReward(UserDataModel u) {
        double baseChance, maxVirtualBalance = 20.0D;
        double currentBalance = u.getPln();

        if (currentBalance >= maxVirtualBalance) {
            return 0.0D;
        }


        if (currentBalance < 1.0D) {
            baseChance = 1.0D;
        } else if (currentBalance < 2.0D) {
            baseChance = 0.7D;
        } else if (currentBalance < 4.0D) {
            baseChance = 0.6D;
        } else if (currentBalance < 5.0D) {
            baseChance = 0.5D;
        } else if (currentBalance < 6.0D) {
            baseChance = 0.4D;
        } else if (currentBalance < 7.0D) {
            baseChance = 0.4D;
        } else if (currentBalance < 8.0D) {
            baseChance = 0.3D;
        } else if (currentBalance < 9.0D) {
            baseChance = 0.2D;
        } else if (currentBalance < 10.0D) {
            baseChance = 0.2D;
        } else if (currentBalance < 15.0D) {
            baseChance = 0.2D;
        } else if (currentBalance < 16.0D) {
            baseChance = 0.1D;
        } else if (currentBalance < 18.5D) {
            baseChance = 0.1D;
        } else if (currentBalance < 19.0D) {
            baseChance = 0.05D;
        } else {
            baseChance = 0.025D;
        }

        double[] rewards = {0.01D, 0.05D, 0.1D};
        double[] weights = {0.95D, 0.04D, 0.01D};


        for (int i = rewards.length - 1; i >= 0; i--) {
            double chance = baseChance * weights[i];
            if (OtherHelper.getChance(chance)) {
                return rewards[i];
            }
        }

        return 0.0D;
    }


    public double addXp(Player p, double baseXp) {
        double finalXp;
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);

        int lvl = u.getUserLvl().getLvl();
        double xpToNext = 500.0D + Math.pow(lvl, 2.0D) * 50.0D;
        u.getUserLvl().setXpToLvl((int) xpToNext);


        if (u.getUserPrestiz().getMoreExp() > 0) {
            finalXp = baseXp * u.getUserPrestiz().getMoreExp() * 1.25D;
        } else {
            finalXp = baseXp;
        }
        u.getUserLvl().addXp(finalXp);

        if (u.getUserLvl().getXp() >= u.getUserLvl().getXpToLvl()) {
            u.getUserLvl().addLvl(1);
            u.getUserLvl().setXp(0.0D);
            p.sendTitle(GlobalHelper.fixColor("&6&lLVL UP"), GlobalHelper.fixColor("&FAwansowałeś na poziom &e" + u.getUserLvl().getLvl()));
        }

        if (u.getUserPrestiz().isAutoPrestiz()) {
            int prestiz = u.getUserPrestiz().getPrestiz();
            int wymaganyPoziom = (prestiz + 1) * 5;
            if (u.getUserLvl().getLvl() >= wymaganyPoziom) {
                u.getUserLvl().setLvl(1);
                u.getUserPrestiz().addPrestiz(1);
                u.getUserPrestiz().addPunktyPrestizu(1);
                p.sendTitle(GlobalHelper.fixColor("&#A68EEAAAutoPrestiż"), GlobalHelper.fixColor("&FAwansowałeś na poziom &#A68EEAA" + u.getUserPrestiz().getPrestiz()));
            }
        }
        OtherHelper.syncMinecraftLevelBar(p, u);
        return finalXp;
    }
}


