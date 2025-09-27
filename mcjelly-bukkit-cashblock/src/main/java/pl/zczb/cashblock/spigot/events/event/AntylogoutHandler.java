package pl.zczb.cashblock.spigot.events.event;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.helpers.TimeUtil;
import pl.zczb.cashblock.objects.Combat;
import pl.zczb.cashblock.objects.impl.CombatManager;
import pl.zczb.helpers.GlobalHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AntylogoutHandler implements Listener {
    private static final Map<UUID, BossBar> bossBars = new HashMap<>();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();


        if (message.startsWith("/spawn") || message.startsWith("/lobby") || message.startsWith("/kit") || message.startsWith("/ec") || message.startsWith("/sklep") || message.startsWith("/kosz") || message.startsWith("/heal")) {
            Combat u = CombatManager.getCombat(player);

            if (u.hasFight()) {
                player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz używać tej komendy podczas walki!"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getDamage() < 0.0D) return;

        final Player d = OtherHelper.getDamager(e);
        if (d == null) return;

        final Player p = (Player) e.getEntity();
        if (p == null) return;

        final Combat u = CombatManager.getCombat(p);
        final Combat u1 = CombatManager.getCombat(d);

        if (u == null || u1 == null) return;

        long endTime = System.currentTimeMillis() + TimeUtil.SECOND.getTime(15);

        u.setLastAttactTime(endTime);
        u.setLastAttactkPlayer(d);

        u1.setLastAttactTime(endTime);
        u1.setLastAttactkPlayer(p);

        startCombatBossBar(p, endTime);
        startCombatBossBar(d, endTime);
    }

    private void startCombatBossBar(Player player, long endTime) {
        BossBar bossBar = bossBars.get(player.getUniqueId());

        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(GlobalHelper.fixColor("&cJestes podczas walki jeszcze przez &415s"),
                    BarColor.RED, BarStyle.SOLID);
            bossBar.addPlayer(player);
            bossBars.put(player.getUniqueId(), bossBar);

            runBossBarTask(player, bossBar);
        }

        bossBar.setTitle(GlobalHelper.fixColor("&cJestes podczas walki jeszcze przez &415s"));
        bossBar.setProgress(1.0);
        bossBar.setVisible(true);

        bossBar.removeFlag(BarFlag.CREATE_FOG);
        bossBar.addFlag(BarFlag.CREATE_FOG);
        player.setMetadata("combatEndTime", new FixedMetadataValue(Cashblock.getInstance(), endTime));
    }

    private void runBossBarTask(Player player, BossBar bossBar) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!bossBars.containsKey(player.getUniqueId())) {
                    cancel();
                    return;
                }

                long endTime = player.hasMetadata("combatEndTime")
                        ? player.getMetadata("combatEndTime").get(0).asLong()
                        : 0L;

                long remainingTime = endTime - System.currentTimeMillis();

                if (remainingTime <= 0) {
                    bossBar.setVisible(false);
                    bossBars.remove(player.getUniqueId());
                    player.removeMetadata("combatEndTime", Cashblock.getInstance());
                    cancel();
                    return;
                }

                double progress = (remainingTime / 1000.0) / 15.0;
                bossBar.setProgress(Math.max(0, Math.min(1.0, progress)));
                bossBar.setTitle(GlobalHelper.fixColor("&cJestes podczas walki jeszcze przez &4" + (remainingTime / 1000) + "s"));
            }
        }.runTaskTimer(Cashblock.getInstance(), 0L, 5L);
    }
}


