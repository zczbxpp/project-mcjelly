package pl.zczb.cashblock.spigot.events.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.boss.BossHandler;
import pl.zczb.cashblock.objects.Combat;
import pl.zczb.cashblock.objects.impl.CombatManager;
import pl.zczb.helpers.GlobalHelper;

import java.util.*;

public class EventHandler implements Listener {
    private final Map<UUID, Long> safeFall = new HashMap<>();


    @org.bukkit.event.EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getY() >= 60.0D && event.getTo().getY() < 60.0D) {
            this.safeFall.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis() + 5000L));
        }
    }

    @org.bukkit.event.EventHandler
    public void onPlayerFallIntoVoid(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getY() < 0) {
            Location location = new Location(Bukkit.getWorld("world"), 0.0D, 70.0D, 0.0D);
            player.teleport(location);
            player.setFallDistance(0);
        }
    }

    @org.bukkit.event.EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;
        Player player = (Player) event.getEntity();
        Long safeUntil = this.safeFall.get(player.getUniqueId());

        if (safeUntil != null && System.currentTimeMillis() < safeUntil.longValue()) {
            event.setCancelled(true);
            this.safeFall.remove(player.getUniqueId());
        }
    }

    @org.bukkit.event.EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Location location = new Location(Bukkit.getWorld("world"), 0.0D, 70.0D, 0.0D);
        event.setRespawnLocation(location);

        if (this.savedBlazeItems.containsKey(uuid)) {
            List<ItemStack> items = this.savedBlazeItems.get(uuid);

            Bukkit.getScheduler().runTaskLater((Plugin) Cashblock.getInstance(), () -> {
                for (ItemStack item : items) {
                    player.getInventory().addItem(new ItemStack[]{item});
                }
                this.savedBlazeItems.remove(uuid);
            }, 5L);
        }
    }


    private final Map<UUID, List<ItemStack>> savedBlazeItems = new HashMap<>();

    @org.bukkit.event.EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();

        event.setDeathMessage(null);

        List<ItemStack> blazeItems = new ArrayList<>();
        Iterator<ItemStack> it = event.getDrops().iterator();

        while (it.hasNext()) {
            ItemStack item = it.next();
            if (item != null && (item.getType() == Material.BLAZE_ROD || (item.getType() == Material.DIAMOND_PICKAXE && ((ItemMeta) Objects.<ItemMeta>requireNonNull(item.getItemMeta())).getDisplayName().equals(GlobalHelper.fixColor("&#FCFF00✦ &#FFD900&lPiekielny młot &#FFA500&l20x20 &#FCFF00✦"))))) {
                blazeItems.add(item.clone());
                it.remove();
            }
        }

        if (!blazeItems.isEmpty()) {
            this.savedBlazeItems.put(uuid, blazeItems);
        }

        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            event.setDeathMessage(GlobalHelper.fixColor(" &#FF0000" + killer.getName() + " &#FF5050zabił &#FF0000" + player.getName()));
        }
    }


    @org.bukkit.event.EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        p.setInvulnerable(false);

        Combat combat = CombatManager.getCombat(p);
        if (combat == null) {
            CombatManager.CreateCombat(p);
        }

        for (BossHandler.BossType type : BossHandler.BossType.values()) {
            if (BossHandler.getBossHealth(type) > 0) {
                ((BossBar) BossHandler.getBossBars().get(type)).addPlayer(p);
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player p = event.getPlayer();

        for (BossHandler.BossType type : BossHandler.BossType.values())
            ((BossBar) BossHandler.getBossBars().get(type)).removePlayer(p);
    }
}


