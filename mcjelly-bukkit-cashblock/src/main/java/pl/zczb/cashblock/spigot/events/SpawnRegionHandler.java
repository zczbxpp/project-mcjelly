package pl.zczb.cashblock.spigot.events;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import pl.zczb.Cashblock;
import pl.zczb.helpers.GlobalHelper;


public class SpawnRegionHandler implements Listener {
    @EventHandler
    public void onMagicznyKamienPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.DRAGON_EGG || event.getBlock().getType() == Material.PLAYER_HEAD) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz stawiać tego przedmiotu!"));
        }
    }


    @EventHandler(ignoreCancelled = true)
    public final void handle(BlockFadeEvent event) {
        if (event.getBlock().getType().equals(Material.FARMLAND))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void handle(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        EntityType type = event.getEntityType();
        if (type != EntityType.IRON_GOLEM && type != EntityType.ARMOR_STAND && type != EntityType.LLAMA && type != EntityType.WITHER_SKELETON && type != EntityType.SKELETON && type != EntityType.ZOMBIE && type != EntityType.SLIME) {
            if(!Cashblock.getCashblockConfig().getSector_name().equals("cashblock_event"))
                event.setCancelled(true);
        }


    }
}


