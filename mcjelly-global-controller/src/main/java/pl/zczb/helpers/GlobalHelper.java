package pl.zczb.helpers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GlobalHelper {
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    public static String fixColor(String text) {
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));
        StringBuilder finalText = new StringBuilder();
        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]).replace(">>", "»"));
                }
            } else {
                finalText.append(texts[i]);
            }
        }
        return finalText.toString();
    }

    public static List<String> fixLore(List<String> lore) {
        ArrayList<String> fixLore = new ArrayList<String>();
        if (lore == null) {
            return fixLore;
        }
        lore.forEach(s -> fixLore.add(fixColor(s)));

        return fixLore;
    }

    public static List<String> lores(ItemStack st, Player p) {
        ItemMeta itemMeta = st.getItemMeta();

        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            return new ArrayList<>();
        }
        return lore.stream()
                .map(GlobalHelper::fixColor)
                .collect(Collectors.toList());
    }

}
