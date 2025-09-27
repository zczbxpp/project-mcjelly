package pl.zczb.cashblock.brush;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.zczb.helpers.GlobalHelper;

import java.util.stream.Collectors;


public class BrushItem {
    private final BrushArea area;
    private final ItemStack itemStack;
    private final String name;

    public BrushItem(BrushArea area, ItemStack itemStack, String name) {
        this.area = area;
        this.itemStack = itemStack;
        this.name = name;
    }

    public BrushItem(ConfigurationSection section) {
        this.name = section.getName();

        this.itemStack = new ItemStack(Material.getMaterial(section.getString("type")));
        itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(pl.zczb.helpers.GlobalHelper.fixColor(section.getString("name")));
        itemMeta.setLore(
                section.getStringList("lore").stream()
                        .map(GlobalHelper::fixColor)
                        .collect(Collectors.toList()));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);

        if (section.get("dynamic_area") != null) {
            this.area = new WallBrushArea(section.getConfigurationSection("dynamic_area"));
        } else {
            throw new IllegalArgumentException("Cos zjebales z konfiguracja itemu " + section.getString("name") + "!");
        }

    }

    public BrushArea getArea() {
        return area;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }
}