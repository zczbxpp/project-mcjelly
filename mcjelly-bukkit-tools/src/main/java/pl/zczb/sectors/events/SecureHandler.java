package pl.zczb.sectors.events;

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


public class SecureHandler implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerDropItemEvent event) {
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer());
        if (cachedUser.isChangingSector()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZmieniasz już sektor, jeśli uważasz że to błąd zgłoś to administratorowi lub wejdz ponownie!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer());

        if (cachedUser.isChangingSector()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZmieniasz już sektor, jeśli uważasz że to błąd zgłoś to administratorowi lub wejdz ponownie!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerCommandPreprocessEvent event) {
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer());
        if (cachedUser.isChangingSector()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZmieniasz już sektor, jeśli uważasz że to błąd zgłoś to administratorowi lub wejdz ponownie!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(InventoryClickEvent event) {
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer((Player) event.getWhoClicked());
        if (cachedUser.isChangingSector()) {
            event.getWhoClicked().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZmieniasz już sektor, jeśli uważasz że to błąd zgłoś to administratorowi lub wejdz ponownie!"));
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerTeleportEvent event) {
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer());
        if (cachedUser.isChangingSector()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZmieniasz już sektor, jeśli uważasz że to błąd zgłoś to administratorowi lub wejdz ponownie!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerInteractEntityEvent event) {
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer());
        if (cachedUser.isChangingSector()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZmieniasz już sektor, jeśli uważasz że to błąd zgłoś to administratorowi lub wejdz ponownie!"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(HangingPlaceEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(event.getPlayer());
        if (cachedUser.isChangingSector()) {
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZmieniasz już sektor, jeśli uważasz że to błąd zgłoś to administratorowi lub wejdz ponownie!"));
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
            UserDataModel cachedUser = Tools.getInstance().getUserHandler().getPlayer(player);
            if (cachedUser.isChangingSector()) {
                event.getRemover().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZmieniasz już sektor, jeśli uważasz że to błąd zgłoś to administratorowi lub wejdz ponownie!"));
                event.setCancelled(true);
            }
        }
    }
}


