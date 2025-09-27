package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.config.data.Help;

import java.util.List;
import java.util.stream.Collectors;


@Command(name = "top", aliases = {"topki", "topka"})
public class TopCmd {
    @Execute
    public void openTopGui(@Context Player p) {
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Topki")))
                .rows(5)

                .disableAllInteractions()
                .create();


        for (Help warp : Tools.getTopConfig().values()) {
            Material material = Material.getMaterial(warp.getMaterial().toUpperCase());
            if (material == null) continue;

            List<Component> lore = warp.getLore().stream()
                    .map(GlobalHelper::fixColor)
                    .map(Component::text)
                    .collect(Collectors.toList());

            GuiItem item = ItemBuilder.from(material)
                    .name(Component.text(GlobalHelper.fixColor(warp.getName())))
                    .lore(lore)
                    .asGuiItem(evente -> {
                        if (!warp.getCommand().equalsIgnoreCase("")) {
                            p.performCommand(warp.getCommand());
                        }
                    });

            gui.setItem(warp.getSlot(), item);
        }


        GuiItem white = ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor(" ")))
                .asGuiItem();
        GuiItem yellow = ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor(" ")))
                .asGuiItem();
        GuiItem orange = ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor(" ")))
                .asGuiItem();


        gui.setItem(0, orange);
        gui.setItem(1, yellow);
        gui.setItem(2, white);
        gui.setItem(3, white);

        gui.setItem(5, white);
        gui.setItem(6, white);
        gui.setItem(7, yellow);
        gui.setItem(8, orange);

        gui.setItem(9, yellow);
        gui.setItem(17, yellow);


        gui.setItem(27, yellow);
        gui.setItem(35, yellow);

        gui.setItem(36, orange);
        gui.setItem(37, yellow);
        gui.setItem(38, white);
        gui.setItem(39, white);

        gui.setItem(41, white);
        gui.setItem(42, white);
        gui.setItem(43, yellow);
        gui.setItem(44, orange);


        gui.open(p);


    }


}
