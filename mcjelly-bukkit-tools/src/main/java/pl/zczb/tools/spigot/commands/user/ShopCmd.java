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
import pl.zczb.tools.config.data.Help;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.util.List;
import java.util.stream.Collectors;

@Command(name = "sklep")
public class ShopCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;

        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Sklep")))
                .rows(5)

                .disableAllInteractions()
                .create();


        for (Help warp : Tools.getShopConfig().values()) {
            Material material = Material.getMaterial(warp.getMaterial().toUpperCase());
            if (material == null) continue;


            List<Component> lore = warp.getLore().stream()
                    .map(GlobalHelper::fixColor)
                    .map(s -> s.replace("{PLAYER}", p.getName()))
                    .map(Component::text)
                    .collect(Collectors.toList());

            GuiItem item = ItemBuilder.from(material)
                    .name(Component.text(GlobalHelper.fixColor(warp.getName())))
                    .lore(lore)
                    .asGuiItem(evente -> {
                        if (warp.getCommand().equalsIgnoreCase("czas")) {
                            sklepczas_gui(p);
                            return;
                        }
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


    public void sklepczas_gui(Player p) {
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Sklep za czas")))
                .rows(5)

                .disableAllInteractions()
                .create();

        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p.getName());

        GuiItem white = ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor(" ")))
                .asGuiItem();
        GuiItem yellow = ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor(" ")))
                .asGuiItem();
        GuiItem orange = ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor(" ")))
                .asGuiItem();

        GuiItem vip = ItemBuilder.from(Material.BOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Ranga &eVIP &8(&f3 dni&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Po zakupie otrzymasz range vip")),
                        Component.text(GlobalHelper.fixColor(" &7na okres &f3 dni")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Cena: &a1 dzien")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem(event -> {
                    long requiredTime = 24 * 60 * 60;
                    long playerTime = u.getPlayerTime();

                    if (playerTime < requiredTime) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz wystarczająco dużo czasu na koncie!"));
                        return;
                    }

                    u.removePlayerTime(requiredTime);
                    p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie zakupiłeś przedmiot!"));
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zakupiłeś range na VIP!"));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent addtemp vip 3d");
                });

        GuiItem svip = ItemBuilder.from(Material.BOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Ranga &6SVIP &8(&f3 dni&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Po zakupie otrzymasz range svip")),
                        Component.text(GlobalHelper.fixColor(" &7na okres &f3 dni")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Cena: &a2 dni")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem(event -> {
                    long requiredTime = 48 * 60 * 60;
                    long playerTime = u.getPlayerTime();

                    if (playerTime < requiredTime) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz wystarczająco dużo czasu na koncie!"));
                        return;
                    }

                    u.removePlayerTime(requiredTime);
                    p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie zakupiłeś przedmiot!"));
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zakupiłeś range na SVIP!"));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent addtemp svip 3d");
                });


        GuiItem zwykla = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &8Zwykłej")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Cena: &a1 godzina")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem(event -> {
                    long requiredTime = 60 * 60;
                    long playerTime = u.getPlayerTime();

                    if (playerTime < requiredTime) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz wystarczająco dużo czasu!"));
                        return;
                    }

                    u.removePlayerTime(requiredTime);
                    p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie zakupiłeś przedmiot!"));
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zakupiłeś przedmiot!"));

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + p.getName() + " zwykla 1");
                });

        GuiItem rzadka = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &3Rzadkiej")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Cena: &a2 godziny")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem(event -> {
                    long requiredTime = 2 * 60 * 60;
                    long playerTime = u.getPlayerTime();

                    if (playerTime < requiredTime) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz wystarczająco dużo czasu!"));
                        return;
                    }

                    u.removePlayerTime(requiredTime);
                    p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie zakupiłeś przedmiot!"));
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zakupiłeś przedmiot!"));

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + p.getName() + " rzadka 1");
                });


        GuiItem epicka = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &5Epickiej")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Cena: &a4 godziny")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem(event -> {
                    long requiredTime = 4 * 60 * 60;
                    long playerTime = u.getPlayerTime();

                    if (playerTime < requiredTime) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz wystarczająco dużo czasu!"));
                        return;
                    }

                    u.removePlayerTime(requiredTime);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + p.getName() + " epicka 1");
                });

        GuiItem legendarna = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &6Legendarnej")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Cena: &a12 godzin")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem(event -> {
                    long requiredTime = 12 * 60 * 60;
                    long playerTime = u.getPlayerTime();
                    if (playerTime < requiredTime) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz wystarczająco dużo czasu!"));
                        return;
                    }

                    u.removePlayerTime(requiredTime);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + p.getName() + " legendarna 1");
                });


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

//        gui.setItem(21, vpln);
//        gui.setItem(23, czas);
        gui.setItem(10, vip);
        gui.setItem(11, svip);
        gui.setItem(12, zwykla);
        gui.setItem(13, rzadka);
        gui.setItem(14, epicka);
        gui.setItem(15, legendarna);

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
