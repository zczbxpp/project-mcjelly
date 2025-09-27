package pl.zczb.tools.spigot.events;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.zczb.tools.objects.impl.EnderChestsManager;

public class EnderchestHandler implements Listener {
    @EventHandler
    public void xppinteract(PlayerInteractEvent e) {
        Block clicked = e.getClickedBlock();
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (clicked == null || clicked.getType() == Material.AIR)
                return;
            if (clicked.getType() == Material.ENDER_CHEST) {
                e.setCancelled(true);
                if (p.isSneaking())
                    return;
                p.getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
                EnderChestsManager.openEnderGui(p);
            }
        }
    }
}
