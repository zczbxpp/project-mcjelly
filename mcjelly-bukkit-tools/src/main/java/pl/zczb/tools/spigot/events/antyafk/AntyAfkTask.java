package pl.zczb.tools.spigot.events.antyafk;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.helpers.GlobalHelper;

import java.util.Map;
import java.util.UUID;

public class AntyAfkTask extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final Map<UUID, PlayerAfkState> afkStateMap;
    private static final int RUNNING_AFK_SECONDS_THRESHOLD = 300;

    public AntyAfkTask(JavaPlugin plugin, Map<UUID, PlayerAfkState> afkStateMap) {
        this.plugin = plugin;
        this.afkStateMap = afkStateMap;
    }


    public void run() {
        long now = System.currentTimeMillis();
        long afkThresholdMillis = 300000L;

        for (Map.Entry<UUID, PlayerAfkState> entry : this.afkStateMap.entrySet()) {
            UUID playerUUID = entry.getKey();
            PlayerAfkState state = entry.getValue();
            Player player = Bukkit.getPlayer(playerUUID);

            if (player == null || !player.isOnline() || player.hasPermission("zczb.root")) {
                continue;
            }

            long timeSinceMouseMove = now - state.lastMouseMoveTime;
            long timeSinceBlockMove = now - state.lastBlockMoveTime;
            boolean isConsideredAfk = (timeSinceMouseMove > afkThresholdMillis && timeSinceBlockMove < 2000L);

            if (isConsideredAfk) {
                if (state.warningCountdown == -1) {
                    state.warningCountdown = 30;
                }
            } else {
                state.warningCountdown = -1;
            }

            if (state.warningCountdown > 0) {
                sendWarning(player, state.warningCountdown);
                state.warningCountdown--;
                continue;
            }
            if (state.warningCountdown == 0) {
                kickPlayer(player);
                state.warningCountdown = -1;
            }
        }
    }

    private void sendWarning(Player player, int timeLeft) {
        player.sendTitle(GlobalHelper.fixColor("&cJesteś tutaj?"), GlobalHelper.fixColor("&7Zostaniesz wyrzucony za &e" + timeLeft + " &7sekund..."), 0, 25, 5);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.2F);
    }

    private void kickPlayer(Player player) {
        Bukkit.getScheduler().runTask((Plugin) this.plugin, () -> player.kickPlayer(GlobalHelper.fixColor("&cZostałeś wyrzucony za bycie nieaktywnym!")));
    }
}


