package pl.zczb.cashblock.helpers;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.zczb.helpers.GlobalHelper;

public class ItemParser {
    public static ItemStack parseItem(String input) {
        try {
            String[] parts = input.split(" ");
            Material mat = Material.valueOf(parts[0]);
            dev.triumphteam.gui.builder.item.ItemBuilder builder = dev.triumphteam.gui.builder.item.ItemBuilder.from(mat);

            for (int i = 1; i < parts.length; i++) {
                String part = parts[i];
                if (part.startsWith("enchants:")) {
                    String[] enchants = part.substring(9).split(",");
                    for (String ench : enchants) {
                        String[] kv = ench.split(":");
                        Enchantment enchant = Enchantment.getByName(kv[0].toUpperCase());
                        if (enchant != null) builder.enchant(enchant, Integer.parseInt(kv[1]));
                    }
                } else if (part.startsWith("amount:")) {
                    builder.amount(Integer.parseInt(part.substring(7)));
                } else if (part.startsWith("name:")) {
                    builder.name((Component) Component.text(GlobalHelper.fixColor(part.substring(5))));
                }
            }

            return builder.build();
        } catch (Exception e) {
            return new ItemStack(Material.BARRIER);
        }
    }
}


