package pl.zczb.tools.helpers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import pl.zczb.helpers.GlobalHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemParser {
    public static ItemStack parseItem(String input, Player p) {
        try {
            String[] split = input.split(" ");
            Material mat = Material.valueOf(split[0].toUpperCase());
            ItemBuilder builder = new ItemBuilder(mat);
            Map<String, String> tagMap = new HashMap<>();

            String key = null;
            StringBuilder value = new StringBuilder();


            for (int i = 1; i < split.length; i++) {
                String part = split[i];
                if (part.contains(":") && !part.startsWith("http")) {
                    if (key != null) {
                        tagMap.put(key.toLowerCase(), value.toString().trim());
                    }
                    int idx = part.indexOf(":");
                    key = part.substring(0, idx);
                    value = new StringBuilder(part.substring(idx + 1));
                } else if (key != null) {
                    value.append(" ").append(part);
                }
            }

            if (key != null) {
                tagMap.put(key.toLowerCase(), value.toString().trim());
            }


            if (tagMap.containsKey("amount")) {
                builder.setAmount(Integer.parseInt(tagMap.get("amount")));
            }


            if (tagMap.containsKey("name")) {
                builder.setName(GlobalHelper.fixColor(tagMap.get("name")));
            }


            if (tagMap.containsKey("lore")) {
                String[] lines = ((String) tagMap.get("lore")).split("\\\\");

                List<String> loreList = new ArrayList<>();

                for (String line : lines) {
                    String processed = line.replace("%player%", p.getName()).trim();
                    if (processed.isEmpty()) {
                        loreList.add(" ");
                    } else {
                        loreList.add(GlobalHelper.fixColor(processed));
                    }
                }

                builder.setLore(loreList);
            }

            if (tagMap.containsKey("glow") && Boolean.parseBoolean(tagMap.get("glow"))) {
                builder.addEnchant(Enchantment.LUCK, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS);
            }

            if (tagMap.containsKey("enchants")) {
                String[] enchants = ((String) tagMap.get("enchants")).split(",");
                for (String ench : enchants) {
                    String[] kv = ench.split(":");
                    Enchantment enchant = Enchantment.getByName(kv[0].toUpperCase());
                    if (enchant != null) {
                        int level = (kv.length > 1) ? Integer.parseInt(kv[1]) : 1;
                        builder.addEnchant(enchant, level);
                    }
                }
            }


            if (tagMap.containsKey("modeldata")) {
                builder.setCustomModelData(Integer.parseInt(tagMap.get("modeldata")));
            }


            if (tagMap.containsKey("potion")) {
                String[] args = ((String) tagMap.get("potion")).split(":");
                PotionType type = PotionType.valueOf(args[0].toUpperCase());
                int level = (args.length > 1) ? Integer.parseInt(args[1]) : 1;
                boolean splash = (args.length > 2 && Boolean.parseBoolean(args[2]));

                Potion potion = new Potion(type);
                potion.setLevel(level);
                potion.setSplash(splash);
                return potion.toItemStack(1);
            }


            if (tagMap.containsKey("custompot")) {
                String[] args = ((String) tagMap.get("custompot")).split(":");
                PotionEffectType effect = PotionEffectType.getByName(args[0].toUpperCase());
                int duration = Integer.parseInt(args[1]);
                int amplifier = Integer.parseInt(args[2]);
                boolean splash = (args.length > 3 && Boolean.parseBoolean(args[3]));

                ItemStack potion = new ItemStack(splash ? Material.SPLASH_POTION : Material.POTION);
                PotionMeta meta = (PotionMeta) potion.getItemMeta();
                if (meta != null && effect != null) {
                    meta.addCustomEffect(new PotionEffect(effect, duration, amplifier), true);
                    potion.setItemMeta(meta);
                }
                return potion;
            }

            return builder.toItemStack();
        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack(Material.BARRIER);
        }
    }
}


