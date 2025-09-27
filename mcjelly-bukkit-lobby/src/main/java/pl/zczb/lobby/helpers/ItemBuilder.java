package pl.zczb.lobby.helpers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.zczb.helpers.GlobalHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemBuilder {
    private ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(Material m, int amount) {
        this.is = new ItemStack(m, amount);
    }

    public ItemBuilder(Material m, int amount, byte durability) {
        this.is = new ItemStack(m, amount, durability);
    }

    public ItemBuilder setType(Material m, int amount) {
        this.is = new ItemStack(m, amount);
        return this;
    }

    public ItemBuilder clone() {
        return new ItemBuilder(this.is);
    }

    public ItemBuilder setDurability(short dur) {
        this.is.setDurability(dur);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(GlobalHelper.fixColor(name));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        if (!this.is.containsEnchantment(ench)) {
            this.is.addUnsafeEnchantment(ench, level);
        }
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        if (this.is.containsEnchantment(ench)) {
            this.is.removeEnchantment(ench);
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        ItemMeta im = this.is.getItemMeta();
        im.addEnchant(ench, level, true);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.is.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        this.is.setDurability((short) Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta im = this.is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = this.is.getItemMeta();
        im.setLore(GlobalHelper.fixLore(lore));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(String line) {
        ItemMeta im = this.is.getItemMeta();
        ArrayList<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line)) {
            return this;
        }
        lore.remove(line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        ItemMeta im = this.is.getItemMeta();
        im.addItemFlags(itemFlag);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.is.setType(material);
        return this;
    }

    public ItemBuilder removeLoreLine(int index) {
        ItemMeta im = this.is.getItemMeta();
        ArrayList<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index >= lore.size()) {
            return this;
        }
        lore.remove(index);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLastLoreLine() {
        ItemMeta im = this.is.getItemMeta();
        if (!im.hasLore()) {
            return this;
        }
        List<String> lore = im.getLore();
        if (lore.isEmpty()) {
            return this;
        }
        lore.remove(lore.size() - 1);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public void imClear() {
        ItemMeta im = this.is.getItemMeta();
        im.setLore(null);
    }

    public ItemBuilder addLoreLine(String line) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = im.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLineContains(String line, String element) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = im.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        boolean replaced = false;

        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).contains(GlobalHelper.fixColor(element))) {
                    lore.set(i, GlobalHelper.fixColor(line));
                    replaced = true;
                    break;
                }
            }
        }

        if (!replaced) {
            lore.add(GlobalHelper.fixColor(line));
        }

        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String line, int pos) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = im.getLore();
        if (lore == null || pos < 0 || pos >= lore.size()) {
            return this;
        }
        lore.set(pos, GlobalHelper.fixColor(line));
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        SkullMeta im = (SkullMeta) this.is.getItemMeta();
        im.setOwner(owner);
        this.is.setItemMeta(im);
        return this;
    }

    public boolean hasLore() {
        ItemMeta im = this.is.getItemMeta();
        return im.hasLore() && im.getLore() != null && !im.getLore().isEmpty();
    }

    public ItemBuilder setCustomModelData(int customModelData) {
        ItemMeta im = this.is.getItemMeta();
        im.setCustomModelData(customModelData);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemStack toItemStack() {
        return this.is;
    }
}
