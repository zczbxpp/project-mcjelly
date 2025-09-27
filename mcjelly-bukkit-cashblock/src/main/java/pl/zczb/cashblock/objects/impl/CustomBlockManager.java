package pl.zczb.cashblock.objects.impl;

import org.bukkit.Location;
import pl.zczb.cashblock.objects.CustomBlock;

import java.util.HashSet;
import java.util.Set;

public class CustomBlockManager {

    private final Set<CustomBlock> cashBlocks = new HashSet<>();


    public boolean addCashBlock(CustomBlock cashBlock) {
        return cashBlocks.add(cashBlock);
    }

    public CustomBlock getCashBlock(Location location) {
        for (CustomBlock cb : cashBlocks) {
            if (cb.getLocation().equals(location)) {
                return cb;
            }
        }
        return null;
    }

    public boolean removeCashBlock(Location location) {
        CustomBlock blockToRemove = null;
        for (CustomBlock cb : cashBlocks) {
            if (cb.getLocation().equals(location)) {
                blockToRemove = cb;
                break;
            }
        }
        if (blockToRemove != null) {
            return cashBlocks.remove(blockToRemove);
        }
        return false;
    }

    public Set<CustomBlock> getCashBlocks() {
        return new HashSet<>(cashBlocks);
    }
}