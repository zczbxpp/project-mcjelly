package pl.zczb.lobby.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.zczb.Lobby;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.lobby.helpers.Items;
import pl.zczb.lobby.queue.QueueInstance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JoinQuitHandler implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player p = event.getPlayer();

        if (!p.isOp() && Lobby.getCfg().getCurrentSector().isAdminMode()) {
            p.kickPlayer(GlobalHelper.fixColor("&cTen serwer jest w trybie administracyjnym"));

            return;
        }
        p.teleport(new Location(Bukkit.getWorlds().get(0), 0.524D, 157.0D, -0.6D));
        p.setHealth(20.0D);
        p.setFoodLevel(20);
        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999999, 2));
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);


        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getInventory().setItem(4, Items.compass());

        Bukkit.getScheduler().runTaskLater((Plugin) Lobby.getInstance(), () -> p.playSound(p.getLocation(), "custom.welcome", 1.0F, 1.0F), 40L);
    }


    private final int VIEW_DISTANCE = 15;
    private final Map<Player, Set<Player>> hiddenPlayers = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other == player)
                continue;
            double distance = player.getLocation().distance(other.getLocation());

            if (distance > 15.0D) {

                if (!((Set) this.hiddenPlayers.computeIfAbsent(player, k -> new HashSet())).contains(other)) {
                    player.hidePlayer(other);
                    ((Set<Player>) this.hiddenPlayers.get(player)).add(other);
                }
                continue;
            }
            if (this.hiddenPlayers.containsKey(player) && ((Set) this.hiddenPlayers.get(player)).contains(other)) {
                player.showPlayer(other);
                ((Set) this.hiddenPlayers.get(player)).remove(other);
            }
        }


        if (Items.isInRegion(player, "portal_cashblock")) {
            QueueInstance queue = Lobby.getInstance().getQueueManager().getQueue("cashblock");
            if (queue != null) {
                queue.addPlayer(player);
            }
        }

        if (Items.isInRegion(player, "portal_igrzyska")) {
            QueueInstance queue = Lobby.getInstance().getQueueManager().getQueue("igrzyska");
            if (queue != null) {
                queue.addPlayer(player);
            }
        }
        if (Items.isInRegion(player, "portal_boxpvp")) {
            QueueInstance queue = Lobby.getInstance().getQueueManager().getQueue("boxpvp");
            if (queue != null) {
                queue.addPlayer(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player p = event.getPlayer();
    }
}


