package pl.zczb.cashblock.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.zczb.cashblock.brush.BrushItem;
import pl.zczb.cashblock.brush.BrushPluginConfiguration;
import pl.zczb.helpers.GlobalHelper;

import java.util.List;
import java.util.stream.Collectors;


@Command(name = "givebrush")
@Permission({"zczb.root"})
public class GiveBrushCmd {
    private final BrushPluginConfiguration configuration;

    public GiveBrushCmd(BrushPluginConfiguration configuration) {
        this.configuration = configuration;
    }

    @Execute
    public void giveBrush(@Context Player player, @Arg("brush") String brushName) {
        BrushItem item = this.configuration.findByName(brushName);
        if (item == null) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie znaleziono brusha o takiej nazwie!"));
            player.sendMessage(GlobalHelper.fixColor("&6Dostepne brushe: &e" + (String) this.configuration.getBrushItems().stream()
                    .map(BrushItem::getName)
                    .collect(Collectors.joining(", "))));

            return;
        }
        ItemStack itemStack = item.getItemStack().clone();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            List<String> lore = meta.getLore();
            if (lore != null && !lore.isEmpty()) {
                lore.set(0, ((String) lore.get(0)).replace("{name}", player.getName()));
                meta.setLore(lore);
            }

            if (brushName.equalsIgnoreCase("mlot")) {
                meta.setCustomModelData(Integer.valueOf(1001));
            }

            itemStack.setItemMeta(meta);
        }

        player.getInventory().addItem(itemStack);
        player.sendMessage(GlobalHelper.fixColor("&aPomyslnie nadano brushersa!"));
    }
}


