package pl.zczb.itemshop.helpers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import pl.zczb.Itemshop;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class GlobalHelper {
    public static String fixColor(String s) {
        String[] split = s.replace("\"", "").split(String.format("((?<=%1$s)|(?=%1$s))", new Object[]{"&"}));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (split[i].equalsIgnoreCase("&")) {
                i++;
                if (split[i].charAt(0) == '#') {
                    sb.append("" + ChatColor.of(split[i].substring(0, 7)) + ChatColor.of(split[i].substring(0, 7)));
                } else {

                    sb.append(ChatColor.translateAlternateColorCodes('&', "&" + split[i]));
                }
            } else {

                sb.append(split[i].replace("\"", ""));
            }
        }
        return sb.toString().replace("<<", "«").replace(">>", "»").replace("\"", "");
    }

    public static ItemStack personalizeBrush(ItemStack brushItem, String playerName) {
        ItemStack cloned = brushItem.clone();
        ItemMeta meta = cloned.getItemMeta();

        if (meta != null && meta.hasLore()) {
            List<String> lore = meta.getLore();
            if (lore != null) {


                List<String> updatedLore = (List<String>) lore.stream().map(line -> line.replace("{name}", playerName)).collect(Collectors.toList());
                meta.setLore(updatedLore);
            }
            cloned.setItemMeta(meta);
        }

        return cloned;
    }

    public static String fixHexColor(String text) {
        text = text.replace("&", "§");


        Pattern hexPattern = Pattern.compile("§#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);


            String legacyHex = "§x" + (String) hex.chars().<CharSequence>mapToObj(c -> "§" + (char) c).collect(Collectors.joining());
            matcher.appendReplacement(buffer, legacyHex);
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    public static void connectToSector(Player player, String sector) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(sector);

        player.sendPluginMessage((Plugin) Itemshop.getInstance(), "BungeeCord", output.toByteArray());
    }

    public static double round(double value, int decimals) {
        double p = Math.pow(10.0D, decimals);
        return Math.round(value * p) / p;
    }
}


