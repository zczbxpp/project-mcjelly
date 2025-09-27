package pl.zczb.tools.spigot.events.antygrief;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import pl.zczb.helpers.GlobalHelper;

public class RedstoneHandler implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void handle(BlockPlaceEvent event) {
        if (event.getItemInHand() != null && event.getItemInHand().getType().equals(Material.REDSTONE)) {
            Material type = event.getBlock().getRelative(BlockFace.DOWN).getType();
            if (Tag.TRAPDOORS.isTagged(type) || Tag.WOODEN_TRAPDOORS.isTagged( type)) {
                event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz postawić redstone w tym miejscu!"));
                event.setCancelled(true);
            }
            type = event.getBlock().getRelative(BlockFace.EAST).getType();
            if (Tag.TRAPDOORS.isTagged( type) || Tag.WOODEN_TRAPDOORS.isTagged( type)) {
                event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz postawić redstone w tym miejscu!"));
                event.setCancelled(true);
            }
            type = event.getBlock().getRelative(BlockFace.WEST).getType();
            if (Tag.TRAPDOORS.isTagged( type) || Tag.WOODEN_TRAPDOORS.isTagged( type)) {
                event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz postawić redstone w tym miejscu!"));
                event.setCancelled(true);
            }
            type = event.getBlock().getRelative(BlockFace.NORTH).getType();
            if (Tag.TRAPDOORS.isTagged( type) || Tag.WOODEN_TRAPDOORS.isTagged( type)) {
                event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz postawić redstone w tym miejscu!"));
                event.setCancelled(true);
            }
            type = event.getBlock().getRelative(BlockFace.SOUTH).getType();
            if (Tag.TRAPDOORS.isTagged( type) || Tag.WOODEN_TRAPDOORS.isTagged( type)) {
                event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz postawić redstone w tym miejscu!"));
                event.setCancelled(true);
            }
        }
    }
}


