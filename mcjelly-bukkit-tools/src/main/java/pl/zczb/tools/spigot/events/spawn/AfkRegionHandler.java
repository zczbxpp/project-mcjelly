package pl.zczb.tools.spigot.events.spawn;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.helpers.OtherHelper;

import java.util.HashMap;
import java.util.Map;

public class AfkRegionHandler implements Listener {
    private final Map<Player, BossBar> bossBars = new HashMap<>();
    private final Map<Player, BukkitRunnable> afkTimers = new HashMap<>();

    private static final int MAX_TIME = 1200;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        boolean isInAfk = OtherHelper.isInRegion(player, "afk");

        if (isInAfk && !this.bossBars.containsKey(player)) {
            startAfkTimer(player);
        } else if (!isInAfk && this.bossBars.containsKey(player)) {
            resetAfkTimer(player);
        }
    }


    private void startAfkTimer(final Player player) {
        final double percents;
        if (player.hasPermission("zczb.media")) {
            percents = 70.0D;
        } else if (player.hasPermission("zczb.donator")) {
            percents = 70.0D;
        } else if (player.hasPermission("zczb.mvip")) {
            percents = 40.0D;
        } else if (player.hasPermission("zczb.svip")) {
            percents = 30.0D;
        } else if (player.hasPermission("zczb.vip")) {
            percents = 20.0D;
        } else {
            percents = 10.0D;
        }


        final BossBar bossBar = Bukkit.createBossBar(
                GlobalHelper.fixColor("&#45CDF8☁ | &fLosowanie klucza afk z szansą &#45CDF8" + percents + "% &fza &#45CDF820m &8(&#45CDF8&l0%&8)"), BarColor.BLUE, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);


        bossBar.setProgress(1.0D);
        bossBar.addPlayer(player);
        this.bossBars.put(player, bossBar);

        BukkitRunnable task = new BukkitRunnable() {
            int timeLeft = 1200;


            public void run() {
                if (!OtherHelper.isInRegion(player, "afk")) {
                    AfkRegionHandler.this.resetAfkTimer(player);

                    return;
                }
                this.timeLeft -= 2;
                double progress = this.timeLeft / 1200.0D;
                int completed = (int) ((1200 - this.timeLeft) / 1200.0D * 100.0D);

                bossBar.setProgress(progress);


                bossBar.setTitle(GlobalHelper.fixColor("&#45CDF8☁ | &fLosowanie klucza afk z szansą &#45CDF8" + percents + "% &fza &#45CDF8" + OtherHelper.formatSecs(this.timeLeft) + " &8(&#45CDF8&l" + completed + "%&8)"));

                if (this.timeLeft <= 0) {

                    if (OtherHelper.getChance(percents)) {
                        AfkRegionHandler.this.giveReward(player);
                        player.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUdało ci sie wylosować klucz afk!"));
                    } else {
                        player.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie udało się wylosować klucza!"));
                    }

                    this.timeLeft = 1200;
                }
            }
        };
        task.runTaskTimer((Plugin) Tools.getInstance(), 20L, 40L);
        this.afkTimers.put(player, task);
    }

    private void resetAfkTimer(Player player) {
        BossBar bossBar = this.bossBars.remove(player);
        if (bossBar != null) bossBar.removeAll();

        BukkitRunnable task = this.afkTimers.remove(player);
        task.cancel();
    }

    private void giveReward(Player player) {
        Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "getcase give " + player.getName() + " afk 1");
        player.sendMessage(GlobalHelper.fixColor("&8[&b&lAFK&8] &7Otrzymałeś klucz &bafk"));
    }
}


