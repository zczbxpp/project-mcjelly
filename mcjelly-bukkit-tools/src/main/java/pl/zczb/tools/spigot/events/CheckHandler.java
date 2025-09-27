package pl.zczb.tools.spigot.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.database.user.models.UserDataModel;


public class CheckHandler implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerDropItemEvent event) {
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer().getUniqueId());
        if (cachedUser.isSprawdzany()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FJesteś sprawdzany nie możesz tego robić!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer().getUniqueId());

        if (cachedUser.isSprawdzany()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FJesteś sprawdzany nie możesz tego robić!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerCommandPreprocessEvent event) {
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer().getUniqueId());
        if (cachedUser.isSprawdzany() && !event.getMessage().equals("przyznajesie")) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FJesteś sprawdzany nie możesz tego robić!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(InventoryClickEvent event) {
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getWhoClicked().getUniqueId());
        if (cachedUser.isSprawdzany()) {
            event.getWhoClicked().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FJesteś sprawdzany nie możesz tego robić!"));
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerTeleportEvent event) {
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer().getUniqueId());
        if (cachedUser.isSprawdzany()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FJesteś sprawdzany nie możesz tego robić!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerInteractEntityEvent event) {
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer().getUniqueId());
        if (cachedUser.isSprawdzany()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FJesteś sprawdzany nie możesz tego robić!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(HangingPlaceEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer().getUniqueId());
        if (cachedUser.isSprawdzany()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FJesteś sprawdzany nie możesz tego robić!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(HangingBreakByEntityEvent event) {
        if (event.getRemover() == null) {
            return;
        }
        boolean isProjectile = event.getRemover().getType().equals(EntityType.ITEM_FRAME);
        if (isProjectile || event.getRemover().getType().equals(EntityType.PLAYER)) {
            Player player = null;
            if (isProjectile) {
                Projectile projectile = (Projectile) event.getRemover();
                if (projectile.getShooter() instanceof Player) {
                    player = (Player) projectile.getShooter();
                }
            } else {

                player = (Player) event.getRemover();
            }
            if (player == null) {
                return;
            }
            UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(player.getUniqueId());
            if (cachedUser.isSprawdzany()) {
                event.getRemover().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FJesteś sprawdzany nie możesz tego robić!"));
                event.setCancelled(true);
            }
        }
    }
}


