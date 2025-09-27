package pl.zczb.lobby.helpers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.zczb.helpers.GlobalHelper;

public class Items {
    public static ItemStack compass() {
        ItemStack itemstack = new ItemStack(Material.COMPASS);
        ItemMeta im = itemstack.getItemMeta();
        im.setDisplayName(GlobalHelper.fixColor("&#FFB874Wybierz tryb &7(PPM)"));
        itemstack.setItemMeta(im);
        return itemstack;
    }

    public static boolean isInRegion(Player player, String regionName) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        return query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()))
                .getRegions()
                .stream()
                .anyMatch(region -> region.getId().equalsIgnoreCase(regionName));
    }
}


