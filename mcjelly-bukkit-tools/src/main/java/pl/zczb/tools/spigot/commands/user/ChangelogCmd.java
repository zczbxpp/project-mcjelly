package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.config.data.Changelog;

import java.util.List;
import java.util.stream.Collectors;


@Command(name = "zmiany", aliases = {"changelog"})
public class ChangelogCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;

        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Zmiany")))
                .rows(5)

                .disableAllInteractions()
                .create();


        GuiItem what = ItemBuilder.from(Material.LEGACY_BOOK_AND_QUILL)
                .name(Component.text(GlobalHelper.fixColor("&2&lZmiany")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&fCo tutaj znajdziesz?")),
                        Component.text(GlobalHelper.fixColor("&7Tutaj możesz zobaczyć co sie zmieniło")),
                        Component.text(GlobalHelper.fixColor("&7co zostało naprawione oraz co wprowadziliśmy")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem();

        List<Changelog> changelogs = Tools.getChangelogConfig().getChangelogs();

        int[] slots = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        };

        int index = 0;

        for (Changelog warp : changelogs) {
            if (index >= slots.length) break;

            List<Component> lore = warp.getLore().stream()
                    .map(GlobalHelper::fixColor)
                    .map(Component::text)
                    .collect(Collectors.toList());

            GuiItem item = ItemBuilder.from(Material.LEGACY_EMPTY_MAP)
                    .name(Component.text(GlobalHelper.fixColor(warp.getName())))
                    .lore(lore)
                    .asGuiItem();
            gui.setItem(slots[index], item);
            index++;
        }


        GuiItem zmiana1 = ItemBuilder.from(Material.LEGACY_EMPTY_MAP)
                .name(Component.text(GlobalHelper.fixColor("&a&lDzień 21.03.2025")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &fZmiany na ten dzień:")),
                        Component.text(GlobalHelper.fixColor("&8- &7Dodanie magicznego młota")),
                        Component.text(GlobalHelper.fixColor("&8- &7Poprawienie mnożników rang")),
                        Component.text(GlobalHelper.fixColor("&8- &7Dodanie właścicieli do brusha")),
                        Component.text(GlobalHelper.fixColor("&8- &7Dodanie komendy &f/kosz")),
                        Component.text(GlobalHelper.fixColor("&8- &7Zresetowano wszystkim level")),
                        Component.text(GlobalHelper.fixColor("&8- &7Dodanie komendy &f/zmiany")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem();

        GuiItem zmiana2 = ItemBuilder.from(Material.LEGACY_EMPTY_MAP)
                .name(Component.text(GlobalHelper.fixColor("&a&lDzień 22.03.2025")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Zmiany na ten dzień:")),
                        Component.text(GlobalHelper.fixColor("&8- &fDodanie przedmiotów za level pod /sklep")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem();

        GuiItem zmiana3 = ItemBuilder.from(Material.LEGACY_EMPTY_MAP)
                .name(Component.text(GlobalHelper.fixColor("&a&lDzień 22.03.2025")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Zmiany na ten dzień:")),
                        Component.text(GlobalHelper.fixColor("&8- &fDodanie przedmiotów za level pod /sklep")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem();
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

        gui.setItem(4, what);


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