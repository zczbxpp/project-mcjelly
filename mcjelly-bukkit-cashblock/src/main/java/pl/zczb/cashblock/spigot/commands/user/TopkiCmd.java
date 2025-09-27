package pl.zczb.cashblock.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.tops.enums.TopType;
import pl.zczb.cashblock.tops.impl.Top;
import pl.zczb.helpers.GlobalHelper;

import java.util.List;

@Command(name = "top-tryb", aliases = {"toptryb"})
public class TopkiCmd {
    @Execute(name = "kasy")
    public void executeTopKasy(@Context Player player) {
        openTopGui(player, TopType.PLN, "&8Topka &7kasy", "Ilość posiadanych vpln");
    }

    @Execute(name = "lvl")
    public void executeTopLvl(@Context Player player) {
        openTopGui(player, TopType.LVL, "&8Topka &7poziomów", "Ilość posiadanego poziomu");
    }

    @Execute(name = "stone")
    public void executeTopStone(@Context Player player) {
        openTopGui(player, TopType.STONE, "&8Topka &7kamienia", "Ilość wykopanego kamienia");
    }

    @Execute(name = "prestiz")
    public void executeTopPrestiz(@Context Player player) {
        openTopGui(player, TopType.PRESTIGE, "&8Topka &7prestiży", "Ilość posiadanych prestiży");
    }

    private void openTopGui(Player player, TopType topType, String title, String valueDescription) {
        List<Top> tops = Cashblock.getInstance().getTopManager().getTopList(topType).getTops();


        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Topka " + topType.name())))
                .rows(3)
                .disableAllInteractions()
                .create();


        int index = 0;

        for (Top top : tops) {
            if (index >= 27) break;

            String nick = top.getNickName();
            String value = top.getTopValue();
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(nick);
            if (top.getTopValue().contains("vPLN")) {
                ItemBuilder skull = ItemBuilder.from(ItemBuilder.skull().owner(offlinePlayer)
                        .name(Component.text(GlobalHelper.fixColor("&7#" + (index + 1) + " &f" + nick)))
                        .lore(
                                Component.text(GlobalHelper.fixColor("&8* &7" + valueDescription + "&8: &f" + value))
                        ).build());

                gui.setItem(index, skull.asGuiItem());
            } else {
                ItemBuilder skull = ItemBuilder.from(ItemBuilder.skull().owner(offlinePlayer)
                        .name(Component.text(GlobalHelper.fixColor("&7#" + (index + 1) + " &f" + nick)))
                        .lore(
                                Component.text(GlobalHelper.fixColor("&8* &7" + valueDescription + "&8: &f" + OtherHelper.formatNumber(Long.parseLong(value))))
                        ).build());

                gui.setItem(index, skull.asGuiItem());
            }

            index++;
        }

        gui.open(player);
    }
}


