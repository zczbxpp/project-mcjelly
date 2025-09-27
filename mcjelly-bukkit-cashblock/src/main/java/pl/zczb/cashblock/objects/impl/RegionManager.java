package pl.zczb.cashblock.objects.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;

public class RegionManager {

    private static com.sk89q.worldguard.protection.managers.RegionManager getRegionManager(org.bukkit.World bukkitWorld) {
        World wgWorld = BukkitAdapter.adapt(bukkitWorld);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        return container.get(wgWorld);
    }

    public static void allowBlockBreak(String regionName, String worldName) {
        setBlockBreakFlag(regionName, worldName, StateFlag.State.ALLOW);
    }

    public static void denyBlockBreak(String regionName, String worldName) {
        setBlockBreakFlag(regionName, worldName, StateFlag.State.DENY);
    }

    private static void setBlockBreakFlag(String regionName, String worldName, StateFlag.State state) {
        org.bukkit.World bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld == null) {
            Bukkit.getLogger().warning("[RegionFlagUtil] Świat '" + worldName + "' nie został znaleziony.");
            return;
        }

        com.sk89q.worldguard.protection.managers.RegionManager regionManager = getRegionManager(bukkitWorld);
        if (regionManager == null) {
            Bukkit.getLogger().warning("[RegionFlagUtil] RegionManager dla świata '" + worldName + "' to null.");
            return;
        }

        ProtectedRegion region = regionManager.getRegion(regionName);
        if (region == null) {
            Bukkit.getLogger().warning("[RegionFlagUtil] Region '" + regionName + "' nie istnieje.");
            return;
        }

        region.setFlag(Flags.BLOCK_BREAK, state);
        Bukkit.getLogger().info("[RegionFlagUtil] Ustawiono BLOCK_BREAK na " + state + " w regionie '" + regionName + "'.");
    }
}
