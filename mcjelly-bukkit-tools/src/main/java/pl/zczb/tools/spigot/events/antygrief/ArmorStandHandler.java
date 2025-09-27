package pl.zczb.tools.spigot.events.antygrief;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.zczb.helpers.GlobalHelper;

public class ArmorStandHandler implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void handle(BlockDispenseEvent event) {
        if (event.getItem().getType().equals(Material.ARMOR_STAND)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void handle(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && event.getItem().getType().equals(Material.ARMOR_STAND) && event.getClickedBlock() != null) {
            if (event.getPlayer().hasCooldown(Material.ARMOR_STAND)) {
                event.setCancelled(true);
                return;
            }
            int i = 0;
            for (Entity entity : event.getClickedBlock().getLocation().getChunk().getEntities()) {
                if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                    i++;
                    entity.setGravity(false);
                }
            }
            if (i >= 5) {
                event.getPlayer().setCooldown(Material.ARMOR_STAND, 100);
                event.setCancelled(true);
                event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FOsiągnieto limit stojaków w tym miejscu! Postaw dalej lub usuń inny stojak"));
            }
        }
    }
}


