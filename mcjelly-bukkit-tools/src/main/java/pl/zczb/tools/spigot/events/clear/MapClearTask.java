package pl.zczb.tools.spigot.events.clear;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;

public class MapClearTask
        extends BukkitRunnable {
    public void run() {
        int i = 0;
        Bukkit.getScheduler().runTaskLater((Plugin) Tools.getInstance(), new Runnable() {
            public void run() {
                Bukkit.broadcastMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FCzyszczenie itemów nastąpi za &#FF000010 &#FF3F3Fsekund"));
            }
        }, (i * 20));
        i += 5;

        Bukkit.getScheduler().runTaskLater((Plugin) Tools.getInstance(), new Runnable() {
            public void run() {
                Bukkit.broadcastMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FCzyszczenie itemów nastąpi za &#FF00005 &#FF3F3Fsekund"));
            }
        }, (i * 20));
        i++;

        Bukkit.getScheduler().runTaskLater((Plugin) Tools.getInstance(), new Runnable() {
            public void run() {
                Bukkit.broadcastMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FCzyszczenie itemów nastąpi za &#FF00004 &#FF3F3Fsekundy"));
            }
        }, (i * 20));
        i++;

        Bukkit.getScheduler().runTaskLater((Plugin) Tools.getInstance(), new Runnable() {
            public void run() {
                Bukkit.broadcastMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FCzyszczenie itemów nastąpi za &#FF00003 &#FF3F3Fsekundy"));
            }
        }, (i * 20));
        i++;

        Bukkit.getScheduler().runTaskLater((Plugin) Tools.getInstance(), new Runnable() {
            public void run() {
                Bukkit.broadcastMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FCzyszczenie itemów nastąpi za &#FF00002 &#FF3F3Fsekundy"));
            }
        }, (i * 20));
        i++;

        Bukkit.getScheduler().runTaskLater((Plugin) Tools.getInstance(), new Runnable() {
            public void run() {
                Bukkit.broadcastMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FCzyszczenie itemów nastąpi za &#FF00001 &#FF3F3Fsekunde"));
            }
        }, (i * 20));
        i++;

        Bukkit.getScheduler().runTaskLater((Plugin) Tools.getInstance(), new Runnable() {
            public void run() {
                World w = Bukkit.getWorld("world");
                for (Entity entity : w.getEntities()) {
                    if (entity instanceof org.bukkit.entity.Item) {
                        entity.remove();
                    }
                }
                Bukkit.broadcastMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FWszystkie itemy z ziemi zostały wyczyszczone!"));
            }
        }, (i * 20));
    }
}


