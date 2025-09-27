package pl.zczb.tools.config.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.zczb.tools.helpers.ItemParser;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kit {
    private String name;
    private String label;
    private int slot;
    private String material;
    private List<String> lore;
    private List<String> items;
    private long cooldown;
    private String permission;

    public List<ItemStack> buildRewardItems(Player p) {
        return this.items.stream()
                .filter(s -> !s.startsWith("cmd:"))
                .map(s -> ItemParser.parseItem(s, p))
                .collect(Collectors.toList());
    }

    public List<String> getRewardCommands() {
        return this.items.stream()
                .filter(s -> s.startsWith("cmd:"))
                .map(s -> s.substring(4))
                .collect(Collectors.toList());
    }
}