package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.database.user.models.UserDataModel;

@Command(name = "nagroda")
public class NagrodaCmd {
    private static final String PLAYER_SKIN_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzM5ZWU3MTU0OTc5YjNmODc3MzVhMWM4YWMwODc4MTRiNzkyOGQwNTc2YTI2OTViYTAxZWQ2MTYzMTk0MjA0NSJ9fX0=";

    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;

        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Nagroda")))
                .rows(5)

                .disableAllInteractions()
                .create();

        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);


        GuiItem gracz = ItemBuilder.skull().texture(PLAYER_SKIN_BASE64)
                .name(Component.text(GlobalHelper.fixColor("&5&lɴᴀɢʀᴏᴅᴀ ᴅɪsᴄᴏʀᴅ")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Aby odebrać darmowe klucze do skrzynki")),
                        Component.text(GlobalHelper.fixColor(" &7Udaj się na discorda na kanał &d#nagroda")),
                        Component.text(GlobalHelper.fixColor(" &7Status: " + (u.isNagroda() ? "&aodebrana" : "&cnie odebrana"))),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby odebrać nagrodę"))
                )
                .asGuiItem(evente -> {
                    gui.close(p);

                    if (u.isNagroda()) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cOdebrałeś już nagrode!"));
                        return;
                    }
                    if (!u.isNagrodaVerified()) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aLink został wysłany na chacie"));
                        p.sendMessage("");
                        p.sendMessage(GlobalHelper.fixColor(" &7Link do discorda: &dhttps://discord.gg/BrvAY9wSym"));
                        p.sendMessage("  ");
                        return;
                    }
                    u.setNagroda(true);

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + p.getName() + " legendarna 1");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent addtemp vip 3d");

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

        gui.setItem(22, gracz);

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


