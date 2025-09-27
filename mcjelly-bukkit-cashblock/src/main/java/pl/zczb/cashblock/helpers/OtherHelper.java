package pl.zczb.cashblock.helpers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.helpers.GlobalHelper;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class OtherHelper {
    public static String formatSecs(long secs) {
        long days = TimeUnit.SECONDS.toDays(secs);
        long hours = TimeUnit.SECONDS.toHours(secs) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.SECONDS.toMinutes(secs) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days);
        long seconds = secs - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days);

        StringBuilder builder = new StringBuilder();
        if (days >= 1L) {
            builder.append(days).append("d");
        }

        if (days != 0L) builder.append(", ");

        if (hours >= 1L) {
            builder.append(hours).append("h");
        }

        if (hours != 0L) builder.append(", ");

        if (minutes >= 1L) {
            builder.append(minutes).append("m");
        }

        if (minutes != 0L) builder.append(", ");

        if (seconds >= 1L) {
            builder.append(seconds).append("s");
        }
        if (seconds == 0L) {
            builder.append("0s");
        }
        return builder.toString();
    }

    public static double round(double value, int decimals) {
        double p = Math.pow(10.0D, decimals);
        return Math.round(value * p) / p;
    }

    public static boolean getChance(double chancePercent) {
        return (ThreadLocalRandom.current().nextDouble() < chancePercent / 100.0D);
    }

    public static Player getDamager(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (damager instanceof Player) {
            return (Player) damager;
        }
        if (damager instanceof Projectile) {
            Projectile p = (Projectile) damager;
            if (p.getShooter() instanceof Player) {
                return (Player) p.getShooter();
            }
        }
        return null;
    }

    private static final Random rand = new Random();

    public static int getRandInt(int min, int max) throws IllegalArgumentException {
        Validate.isTrue((max > min), "Max can't be smaller than min!");
        return rand.nextInt(max - min + 1) + min;
    }

    public static boolean randomTeleport(Player player, int minX, int maxX, int minZ, int maxZ) {
        int randomX = getRandInt(minX, maxX);
        int randomZ = getRandInt(minZ, maxZ);
        int y = player.getWorld().getHighestBlockYAt(randomX, randomZ);
        Location randomLocation = new Location(player.getWorld(), randomX, (y + 1), randomZ);


        randomLocation.getChunk().load(true);

        player.teleport(randomLocation);

        player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Zostałeś przeteleportowany na koordynaty X: " + randomLocation.getBlockX() + " Y: " + randomLocation.getBlockY() + " Z: " + randomLocation.getBlockZ()));

        return true;
    }

    public static boolean hasItem(Player player, String itemName, int amount) {
        int count = 0;
        for (ItemStack invItem : player.getInventory().getContents()) {
            if (invItem != null &&
                    invItem.hasItemMeta()) {
                ItemMeta meta = invItem.getItemMeta();
                if (meta.hasDisplayName()) {

                    String displayName = GlobalHelper.fixColor(meta.getDisplayName());

                    if (displayName.equals(itemName)) {
                        count += invItem.getAmount();
                        if (count >= amount)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasBonusItem(ItemStack item, Material type, String displayName) {
        return (item != null && item.getType() == type && item.hasItemMeta() &&
                GlobalHelper.fixColor(item.getItemMeta().getDisplayName()).equals(GlobalHelper.fixColor(displayName)));
    }

    public static boolean removeItemByName(Player player, String itemName, int amount) {
        int toRemove = amount;
        boolean changed = false;

        String strippedName = GlobalHelper.fixColor(itemName);

        ItemStack[] contents = player.getInventory().getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack invItem = contents[i];
            if (invItem != null) {

                ItemMeta meta = invItem.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {

                    String invName = GlobalHelper.fixColor(meta.getDisplayName());

                    if (invName.equals(strippedName)) {
                        int itemAmount = invItem.getAmount();

                        if (itemAmount <= toRemove) {

                            player.getInventory().setItem(i, null);
                            toRemove -= itemAmount;
                            changed = true;
                        } else {

                            invItem.setAmount(itemAmount - toRemove);
                            player.getInventory().setItem(i, invItem);
                            toRemove = 0;
                            changed = true;

                            break;
                        }
                        if (toRemove <= 0)
                            break;
                    }
                }
            }
        }
        if (changed) {
            player.updateInventory();
        }


        return (toRemove <= 0);
    }

    public static boolean hasItem(Player player, ItemStack item, int amount) {
        int count = 0;
        for (ItemStack invItem : player.getInventory().getContents()) {
            if (invItem != null && invItem.getType() == item.getType() && invItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                count += invItem.getAmount();
                if (count >= amount) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemStack personalizeBrush(ItemStack brushItem, String playerName) {
        ItemStack cloned = brushItem.clone();
        ItemMeta meta = cloned.getItemMeta();

        if (meta != null && meta.hasLore()) {
            List<String> lore = meta.getLore();
            if (lore != null) {


                List<String> updatedLore = (List<String>) lore.stream().map(line -> line.replace("{name}", playerName)).collect(Collectors.toList());
                meta.setLore(updatedLore);
            }
            cloned.setItemMeta(meta);
        }

        return cloned;
    }

    public static void removeItem(Player player, ItemStack item, int amount) {
        for (ItemStack invItem : player.getInventory().getContents()) {
            if (invItem != null && invItem.getType() == item.getType() && invItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                int invAmount = invItem.getAmount();
                if (invAmount > amount) {
                    invItem.setAmount(invAmount - amount);
                    return;
                }
                player.getInventory().removeItem(new ItemStack[]{invItem});
                amount -= invAmount;
                if (amount <= 0) {
                    return;
                }
            }
        }
    }


    private static final MiniMessage mm = MiniMessage.miniMessage();

    @NotNull
    public static Component mm(String input) {
        return mm.deserialize("<italic:false>" + input);
    }


    public static boolean isInRegion(Player player, String regionName) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        return query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()))
                .getRegions()
                .stream()
                .anyMatch(region -> region.getId().equalsIgnoreCase(regionName));
    }


    public static void teleportMaxY(Player player) {
        World world = player.getWorld();
        int x = player.getLocation().getBlockX();
        int z = player.getLocation().getBlockZ();

        int y = world.getHighestBlockYAt(x, z);

        Location safeLocation = new Location(world, x + 0.5D, (y + 1), z + 0.5D);
        if (!safeLocation.getBlock().getType().isAir()) {
            while (!safeLocation.getBlock().getType().isAir() && safeLocation.getY() < world.getMaxHeight()) {
                safeLocation.add(0.0D, 1.0D, 0.0D);
            }
        }

        player.teleport(safeLocation);
    }

    public static String formatNumber(long number) {
        if (number < 1000L)
            return String.valueOf(number);
        if (number < 1000000L)
            return String.format("%.1fk", new Object[]{Double.valueOf(number / 1000.0D)}).replace(".0", "");
        if (number < 1000000000L) {
            return String.format("%.1fmln", new Object[]{Double.valueOf(number / 1000000.0D)}).replace(".0", "");
        }
        return String.format("%.1fmld", new Object[]{Double.valueOf(number / 1.0E9D)}).replace(".0", "");
    }


    public static void syncMinecraftLevelBar(Player player, UserDataModel u) {
        double currentXp = u.getUserLvl().getXp();
        double xpToLevel = u.getUserLvl().getXpToLvl();
        int level = u.getUserLvl().getLvl();

        float progress = 0.0F;
        if (xpToLevel > 0.0D) {
            progress = (float) (currentXp / xpToLevel);
            if (progress > 1.0F) progress = 1.0F;

        }
        player.setLevel(level);
        player.setExp(progress);
    }

    public static boolean isInRadius(Location location, int radius) {
        return (Math.abs(location.getBlockX()) <= radius && Math.abs(location.getBlockZ()) <= radius);
    }


    public static boolean isInRadius(Location location, Location to, int radius) {
        if (!location.getWorld().getUID().equals(to.getWorld().getUID()))
            return false;
        int distanceX = Math.abs(to.getBlockX() - location.getBlockX());
        int distanceZ = Math.abs(to.getBlockZ() - location.getBlockZ());
        return (distanceX <= radius && distanceZ <= radius);
    }

    public static boolean isInRadius(Location location, Location to, int radius, int radiusY) {
        if (!location.getWorld().getUID().equals(to.getWorld().getUID()))
            return false;
        int distanceY = Math.abs(to.getBlockY() - location.getBlockY());
        int distanceX = Math.abs(to.getBlockX() - location.getBlockX());
        int distanceZ = Math.abs(to.getBlockZ() - location.getBlockZ());
        return (distanceX <= radius && distanceZ <= radius && distanceY <= radiusY);
    }
}


