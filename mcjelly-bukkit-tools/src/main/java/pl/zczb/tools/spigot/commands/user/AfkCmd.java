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
import pl.zczb.helpers.GlobalHelper;

@Command(name = "afk", aliases = {"strefaafk"})
public class AfkCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;

        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Strefa afk")))
                .rows(5)

                .disableAllInteractions()
                .create();


        pl.zczb.tools.helpers.ItemBuilder gracz = new pl.zczb.tools.helpers.ItemBuilder(Material.LEATHER_BOOTS)
                .setName(GlobalHelper.fixColor("&7&lGracz"))
                .addLoreLine(GlobalHelper.fixColor(""))
                .addLoreLine(GlobalHelper.fixColor("&8* &FCo &#9B9B9B20 min &fmasz &#9B9B9B10% &fna wylosowanie &#9B9B9BKlucza afk"))
                .addLoreLine(GlobalHelper.fixColor(""));


        pl.zczb.tools.helpers.ItemBuilder vip = new pl.zczb.tools.helpers.ItemBuilder(Material.CHAINMAIL_BOOTS)
                .setName(GlobalHelper.fixColor("&f\uE802"))
                .addLoreLine(GlobalHelper.fixColor(""))
                .addLoreLine(GlobalHelper.fixColor("&8* &FCo &#FF910020 min &fmasz &#FF910020% &fna wylosowanie &#FF9100Klucza afk"))
                .addLoreLine(GlobalHelper.fixColor(""));

        pl.zczb.tools.helpers.ItemBuilder svip = new pl.zczb.tools.helpers.ItemBuilder(Material.IRON_BOOTS)
                .setName(GlobalHelper.fixColor("&f\uE803"))
                .addLoreLine(GlobalHelper.fixColor(""))
                .addLoreLine(GlobalHelper.fixColor("&8* &FCo &#FFF80020 min &fmasz &#FFF80030% &fna wylosowanie &#FFF800Klucza afk"))
                .addLoreLine(GlobalHelper.fixColor(""));

        pl.zczb.tools.helpers.ItemBuilder mvip = new pl.zczb.tools.helpers.ItemBuilder(Material.GOLDEN_BOOTS)
                .setName(GlobalHelper.fixColor("&f\uE804"))
                .addLoreLine(GlobalHelper.fixColor(""))
                .addLoreLine(GlobalHelper.fixColor("&8* &FCo &#00D6FF20 min &fmasz &#00D6FF40% &fna wylosowanie &#00D6FFKlucza afk"))
                .addLoreLine(GlobalHelper.fixColor(""));

        pl.zczb.tools.helpers.ItemBuilder donator = new pl.zczb.tools.helpers.ItemBuilder(Material.DIAMOND_BOOTS)
                .setName(GlobalHelper.fixColor("&f\uE805"))
                .addLoreLine(GlobalHelper.fixColor(""))
                .addLoreLine(GlobalHelper.fixColor("&8* &FCo &#6BFF0020 min &fmasz &#6BFF0070% &fna wylosowanie &#6BFF00Klucza afk"))
                .addLoreLine(GlobalHelper.fixColor(""));

        pl.zczb.tools.helpers.ItemBuilder media = new pl.zczb.tools.helpers.ItemBuilder(Material.NETHERITE_BOOTS)
                .setName(GlobalHelper.fixColor("&f\uE806"))
                .addLoreLine(GlobalHelper.fixColor(""))
                .addLoreLine(GlobalHelper.fixColor("&8* &FCo &#FF81EC20 min &fmasz &#FF81EC70% &fna wylosowanie &#FF81ECKlucza afk"))
                .addLoreLine(GlobalHelper.fixColor(""));

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

        gui.setItem(20, ItemBuilder.from(gracz.toItemStack()).asGuiItem());
        gui.setItem(21, ItemBuilder.from(vip.toItemStack()).asGuiItem());
        gui.setItem(22, ItemBuilder.from(svip.toItemStack()).asGuiItem());
        gui.setItem(23, ItemBuilder.from(mvip.toItemStack()).asGuiItem());
        gui.setItem(24, ItemBuilder.from(donator.toItemStack()).asGuiItem());
        gui.setItem(31, ItemBuilder.from(media.toItemStack()).asGuiItem());


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



