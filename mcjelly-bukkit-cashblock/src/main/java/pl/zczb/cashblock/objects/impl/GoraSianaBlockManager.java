package pl.zczb.cashblock.objects.impl;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class GoraSianaBlockManager {
    private static final Map<Location, Double> specialBlocks = new HashMap<>();

    public static void addBlock(Location loc, double value) {
        specialBlocks.put(loc.getBlock().getLocation(), Double.valueOf(value));
    }

    public static boolean isSpecialBlock(Location loc) {
        return specialBlocks.containsKey(loc.getBlock().getLocation());
    }

    public static double getValue(Location loc) {
        return ((Double) specialBlocks.getOrDefault(loc.getBlock().getLocation(), Double.valueOf(0.0D))).doubleValue();
    }

    public static void removeBlock(Location loc) {
        specialBlocks.remove(loc.getBlock().getLocation());
    }

    public static void clearAll() {
        specialBlocks.clear();
    }
}


