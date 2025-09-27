package pl.zczb.cashblock.helpers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RepairHelper {
    private static Material[] getRepairableItems() {
        return repairable;
    }

    private static boolean containsRepairable(Material[] repairable, Material material) {
        for (Material mat : repairable) {
            if (material == mat)
                return true;
        }
        return false;
    }

    private static int repairArmor(Player player) {
        int in = 0;
        for (ItemStack i : player.getInventory().getArmorContents()) {
            if (i != null && containsRepairable(getRepairableItems(), i.getType()) && i.getDurability() != (new ItemStack(i.getType())).getDurability()) {
                i.setDurability((short) 0);
                in++;
            }
        }
        return in;
    }

    public static int repairAll(Player player) {
        int in = repairArmor(player);

        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null && containsRepairable(getRepairableItems(), i.getType()) && i.getDurability() != (new ItemStack(i.getType())).getDurability()) {
                i.setDurability((short) 0);
                in++;
            }
        }
        return in;
    }

    private static final Material[] repairable = new Material[]{Material.NETHERITE_PICKAXE, Material.NETHERITE_SWORD, Material.NETHERITE_SHOVEL, Material.NETHERITE_AXE, Material.NETHERITE_HOE, Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS, Material.DIAMOND_PICKAXE, Material.DIAMOND_SWORD, Material.DIAMOND_SHOVEL, Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.IRON_PICKAXE, Material.IRON_SWORD, Material.IRON_SHOVEL, Material.IRON_AXE, Material.IRON_HOE, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.GOLDEN_PICKAXE, Material.GOLDEN_SWORD, Material.GOLDEN_SHOVEL, Material.GOLDEN_AXE, Material.GOLDEN_HOE, Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS, Material.STONE_PICKAXE, Material.STONE_SWORD, Material.STONE_SHOVEL, Material.STONE_AXE, Material.STONE_HOE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.WOODEN_PICKAXE, Material.WOODEN_SWORD, Material.WOODEN_SHOVEL, Material.WOODEN_AXE, Material.WOODEN_HOE, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.FLINT_AND_STEEL, Material.SHEARS, Material.BOW, Material.FISHING_ROD, Material.ANVIL};
}


