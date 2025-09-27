package pl.zczb.tools.spigot.events.antyafk;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class AntyAfkHandler implements Listener {
    private final Map<UUID, PlayerAfkState> afkStateMap = new ConcurrentHashMap<>();

    public Map<UUID, PlayerAfkState> getAfkStateMap() {
        return this.afkStateMap;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.afkStateMap.put(event.getPlayer().getUniqueId(), new PlayerAfkState());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.afkStateMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        PlayerAfkState state = this.afkStateMap.get(event.getPlayer().getUniqueId());
        if (state == null)
            return;
        Location from = event.getFrom();
        Location to = event.getTo();
        long now = System.currentTimeMillis();

        if (from.getYaw() != to.getYaw() || from.getPitch() != to.getPitch()) {
            state.lastMouseMoveTime = now;
        }

        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ())
            state.lastBlockMoveTime = now;
    }
}


