package pl.zczb.cashblock.spigot.commands.user;

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
import pl.zczb.Cashblock;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.helpers.GlobalHelper;

@Command(name = "rtp")
public class RtpCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;
        if (Cashblock.getCashblockConfig().getSector_name().equals("cashblock_event")) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNa kanale eventowym nie możesz tego używać!"));
            return;
        }


        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Losowy Teleport")))
                .rows(5)

                .disableAllInteractions()
                .create();


        GuiItem gracz = ItemBuilder.from(Material.WOODEN_AXE)
                .name(Component.text(GlobalHelper.fixColor("&5&lᴛᴇʟᴇᴘᴏʀᴛ ")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &8- &7Zasięg &f1000x1000")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby się teleportować"))
                )
                .asGuiItem(event -> {
                    gui.close(p);
                    OtherHelper.randomTeleport(p, 200, 1000, 200, 1000);
                });

        GuiItem vipplus = ItemBuilder.from(Material.GOLDEN_AXE)
                .name(Component.text(GlobalHelper.fixColor("&5&lᴛᴇʟᴇᴘᴏʀᴛ &8(&f\uE802&7,&f\uE803&7,&f\uE804&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &8- &7Zasięg &f3000x3000")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby się teleportować"))
                )
                .asGuiItem(event -> {
                    gui.close(p);
                    if (!p.hasPermission("zczb.vip")) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie posiadasz rangi aby użyć tego teleportu!"));
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie posiadasz rangi aby użyć teleportu!"));
                        return;
                    }
                    OtherHelper.randomTeleport(p, 1500, 3000, 1500, 3000);
                });

        GuiItem donator = ItemBuilder.from(Material.DIAMOND_AXE)
                .name(Component.text(GlobalHelper.fixColor("&5&lᴛᴇʟᴇᴘᴏʀᴛ &f\uE805")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &8- &7Zasięg &f5000x5000")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby się teleportować"))
                )
                .asGuiItem(event -> {
                    gui.close(p);
                    if (!p.hasPermission("zczb.donator")) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie posiadasz rangi aby użyć tego teleportu!"));
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie posiadasz rangi aby użyć teleportu!"));
                        return;
                    }
                    OtherHelper.randomTeleport(p, 3500, 5000, 3500, 5000);
                });

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

        gui.setItem(20, gracz);
        gui.setItem(22, vipplus);
        gui.setItem(24, donator);

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

