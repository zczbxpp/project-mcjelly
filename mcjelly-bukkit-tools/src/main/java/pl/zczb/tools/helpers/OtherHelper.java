package pl.zczb.tools.helpers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


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

    public static Component mm(String input) {
        return MiniMessage.miniMessage().deserialize("<italic:false>" + fixColorToMiniMessage(input));
    }

    public static String fixColorToMiniMessage(String input) {
        if (input == null) return "";

        return input
                .replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&l", "<bold>")
                .replace("&n", "<underlined>")
                .replace("&o", "<italic>")
                .replace("&m", "<strikethrough>")
                .replace("&r", "<reset>");
    }

    public static String formatMillis(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        long milliseconds = millis % 1000L;

        StringBuilder builder = new StringBuilder();

        if (days > 0L) builder.append(days).append("d, ");
        if (hours > 0L || builder.length() > 0) builder.append(hours).append("h, ");
        if (minutes > 0L || builder.length() > 0) builder.append(minutes).append("m, ");
        if (seconds > 0L || builder.length() > 0) builder.append(seconds).append("s, ");

        builder.append(milliseconds).append("ms");

        return builder.toString();
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

    public static boolean isInRegion(Player player, String regionName) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        return query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()))
                .getRegions()
                .stream()
                .anyMatch(region -> region.getId().equalsIgnoreCase(regionName));
    }
}


