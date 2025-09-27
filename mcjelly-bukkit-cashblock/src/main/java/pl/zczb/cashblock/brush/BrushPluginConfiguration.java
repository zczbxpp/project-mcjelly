package pl.zczb.cashblock.brush;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.zczb.helpers.GlobalHelper;

import java.util.*;

public class BrushPluginConfiguration {
    private final Set<BrushItem> brushItemList = new HashSet<>();
    private final EnumSet<Material> blockedMaterials = EnumSet.noneOf(Material.class);
    private final Map<String, String> upgradeMap = new HashMap<>();
    private final Map<String, String> downgradeMap = new HashMap<>();


    public BrushItem findByItemStack2(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return null;
        }

        String itemDisplayName = pl.zczb.helpers.GlobalHelper.fixColor(itemStack.getItemMeta().getDisplayName());

        for (BrushItem item : brushItemList) {
            if (item.getItemStack().hasItemMeta() && item.getItemStack().getItemMeta().hasDisplayName()) {
                String brushDisplayName = GlobalHelper.fixColor(item.getItemStack().getItemMeta().getDisplayName());

                if (itemDisplayName.equalsIgnoreCase(brushDisplayName)) {
                    return item;
                }
            }
        }
        return null;
    }

    public boolean hasSameNameInLore(ItemStack item, String playerName) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) {
            return false;
        }

        String loreLine = lore.get(0);

        return loreLine.contains(playerName);
    }


    public BrushItem findByName(String name) {
        for (BrushItem item : brushItemList)
            if (item.getName().equals(name))
                return item;
        return null;
    }

    public Collection<BrushItem> getBrushItems() {
        return Collections.unmodifiableCollection(brushItemList);
    }

    public void loadConfiguration(FileConfiguration configuration) {

        ConfigurationSection brushes = configuration.getConfigurationSection("brushes");
        if (brushes == null)
            throw new IllegalArgumentException("XD!");

        for (String key : brushes.getKeys(false)) {
            brushItemList.add(new BrushItem(
                    brushes.getConfigurationSection(key)
            ));
        }
        for(String material: configuration.getStringList("blocked")) {
            blockedMaterials.add(Material.getMaterial(material));
        }
    }

    public boolean isAllowed(Material material) {
        return !blockedMaterials.contains(material);
    }
}