package pl.zczb.tools.spigot.events.spawn;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.sectors.serialization.SerializationHelper;
import pl.zczb.tools.database.user.models.UserDataModel;
import pl.zczb.tools.helpers.OtherHelper;

import java.util.HashMap;
import java.util.Map;

public class ParkourHandler implements Listener {
    private final Map<Player, Long> parkourTimers = new HashMap<>();
    private final Map<Player, BukkitRunnable> parkourTasks = new HashMap<>();

    private final Location startLocation = SerializationHelper.stringToLoc(Tools.getSectorConfig().getParkour_start());
    private final Location endLocation = SerializationHelper.stringToLoc(Tools.getSectorConfig().getParkour_end());
    private final int minY = Tools.getSectorConfig().getParkour_y();


    @EventHandler
    public void onPressurePlateStep(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL)
            return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
            return;
        Player player = event.getPlayer();
        if (!OtherHelper.isInRegion(player, "spawn") || this.parkourTimers.containsKey(player))
            return;
        this.parkourTimers.put(player, Long.valueOf(System.currentTimeMillis()));
        player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Rozpocząłeś parkour na spawnie! Powodzenia!"));

        startActionBarTimer(player);
    }

    @EventHandler
    public void onPlayerFall(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!OtherHelper.isInRegion(player, "spawn") || player.getLocation().getY() >= this.minY || !this.parkourTimers.containsKey(player))
            return;
        stopActionBarTimer(player);
        this.parkourTimers.remove(player);
        this.startLocation.setYaw(43.0F);
        player.teleport(this.startLocation);
        player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FSpadłeś! Cofamy cię na start parkoura."));
    }

    @EventHandler
    public void onParkourComplete(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!OtherHelper.isInRegion(player, "spawn") || !this.parkourTimers.containsKey(player))
            return;
        player.setAllowFlight(false);
        player.setFlying(false);
        if (player.getLocation().distanceSquared(this.endLocation) > 2.5D) {
            return;
        }
        stopActionBarTimer(player);
        long timeTaken = System.currentTimeMillis() - ((Long) this.parkourTimers.remove(player)).longValue();
        String formattedTime = OtherHelper.formatMillis(timeTaken);

        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(player.getName());
        long bestTime = u.getParkourTime();

        if (bestTime == 0L || timeTaken < bestTime) {
            u.setParkourTime(timeTaken);
            player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Nowy rekord! Ukończyłeś parkour na spawnie w " + formattedTime + "!"));
        } else {
            player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Ukończyłeś parkour na spawnie w " + formattedTime + ", ale nie pobiłeś swojego rekordu (" + OtherHelper.formatMillis(bestTime) + ")."));
        }
    }


    private void startActionBarTimer(final Player player) {
        BukkitRunnable task = new BukkitRunnable() {
            public void run() {
                if (!ParkourHandler.this.parkourTimers.containsKey(player)) {
                    cancel();

                    return;
                }
                long elapsedTimeMs = System.currentTimeMillis() - ((Long) ParkourHandler.this.parkourTimers.get(player)).longValue();
                String formattedTime = OtherHelper.formatMillis(elapsedTimeMs);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent) new TextComponent(GlobalHelper.fixColor("&eTwój czas parkour &6" + formattedTime)));
            }
        };

        task.runTaskTimer((Plugin) Tools.getInstance(), 0L, 3L);
        this.parkourTasks.put(player, task);
    }

    private void stopActionBarTimer(Player player) {
        if (this.parkourTasks.containsKey(player)) {
            ((BukkitRunnable) this.parkourTasks.get(player)).cancel();
            this.parkourTasks.remove(player);
        }
    }
}


