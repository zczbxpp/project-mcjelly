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
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.spigot.events.BoostsHandler;
import pl.zczb.helpers.GlobalHelper;

@Command(name = "prestiz")
public class PrestizCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {

        final Player p = (Player) sender;
        prestizGui(p);
    }

    public void prestizGui(Player p) {
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Prestiz")))
                .rows(5)

                .disableAllInteractions()
                .create();
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);
        GuiItem ulepszenia = ItemBuilder.from(Material.BREWING_STAND)
                .name(OtherHelper.mm("<color:#A68EEA>Ulepszenia za punkty Prestiżu"))
                .lore(
                        OtherHelper.mm("<gray>W tym miejscu możesz wydać swoje"),
                        OtherHelper.mm("<gray>punkty, otrzymane za prestiżowanie"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<yellow>Kliknij, aby przejść dalej")

                )
                .asGuiItem(evente -> {
                    upgradeGui(p);
                });

        int prestiz = u.getUserPrestiz().getPrestiz();
        int wymaganyPoziom = (prestiz + 1) * 5;
        GuiItem ulepsz = ItemBuilder.from(Material.BOOK)
                .name(OtherHelper.mm("<color:#A68EEA>Prestiż: <color:#764FE5>" + u.getUserPrestiz().getPrestiz()))
                .lore(
                        OtherHelper.mm("<gray>Gdy zrobisz prestiż Twój poziom"),
                        OtherHelper.mm("<gray>cofa się do <color:#66D97C>1 poziomu"),
                        OtherHelper.mm("<gray>Prestiżując odblokowywujesz nowe"),
                        OtherHelper.mm("<white>przedmioty <gray>oraz <white>ulepszenia"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#A68EEA>Aktualne statystyki:"),
                        OtherHelper.mm(" <color:#A68EEA>| <gray>Prestiż: <white>" + u.getUserPrestiz().getPrestiz()),
                        OtherHelper.mm(" <color:#A68EEA>| <gray>Punkty prestiżu: <white>" + u.getUserPrestiz().getPunktyPrestizu()),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#A68EEA>Koszt ulepszenia:"),
                        OtherHelper.mm(" <color:#A68EEA>| <white>" + wymaganyPoziom + " <gray>poziom postaci"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<yellow>Kliknij, aby dokonać prestiżu")

                )
                .asGuiItem(evente -> {
                    gui.close(p);
                    if (!(u.getUserLvl().getLvl() >= wymaganyPoziom)) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie posiadasz wymaganego lvl'u!"));
                        return;
                    }
                    u.getUserLvl().setLvl(1);
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aDokonano prestiżu ulepsz postać &2/prestiz"));
                    u.getUserPrestiz().addPrestiz(1);
                    u.getUserPrestiz().addPunktyPrestizu(1);
                    OtherHelper.syncMinecraftLevelBar(p, u);
                });

        GuiItem autoprestiz = ItemBuilder.from((u.getUserPrestiz().isAutoPrestiz() ? Material.LIME_DYE : Material.RED_DYE))
                .name(OtherHelper.mm("<color:#A68EEA>AutoPrestiż <dark_gray>(" + (u.getUserPrestiz().isAutoPrestiz() ? "<color:#00FF31>✔" : "<color:#D40303>✘") + "<dark_gray>)"))
                .lore(
                        OtherHelper.mm("<gray>Twój prestiż może się automatycznie"),
                        OtherHelper.mm("<gray>ulepszać, wystarczy że włączysz tę opcję."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#A68EEA>Aktualny status:"),
                        OtherHelper.mm(" <color:#A68EEA>| <gray>Wymagana ranga: <white>\uE805 <gray>lub <white>\uE804"),
                        OtherHelper.mm(" <color:#A68EEA>| <gray>Status: <white>" + (u.getUserPrestiz().isAutoPrestiz() ? "Właczone" : "Wyłączone")),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<yellow>Kliknij, aby zmienić status")

                )
                .asGuiItem(evente -> {
                    gui.close(p);
                    if (!(p.hasPermission("zczb.donator") || p.hasPermission("zczb.mvip"))) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie posiadasz permisji kup mvip lub jelly!"));
                        return;
                    }
                    u.getUserPrestiz().setAutoPrestiz(!u.getUserPrestiz().isAutoPrestiz());
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie &2" + (u.getUserPrestiz().isAutoPrestiz() ? "włączono" : "wyłączono") + " &aauto prestiz!"));
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

        gui.setItem(20, ulepszenia);
        gui.setItem(22, ulepsz);
        gui.setItem(24, autoprestiz);

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


    public void upgradeGui(Player p) {
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Ulepszenia")))
                .rows(5)

                .disableAllInteractions()
                .create();
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);

        GuiItem exp = ItemBuilder.from(Material.EXPERIENCE_BOTTLE)
                .name(OtherHelper.mm("<color:#A68EEA>Zdobywaj więcej Exp'a"))
                .lore(
                        OtherHelper.mm("<gray>Zwiększ liczbę zdobywanego"),
                        OtherHelper.mm("<gray>expa podczas Twojego kopania."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#A68EEA>| <gray>Poziom: <color:#CFBEFF>" + u.getUserPrestiz().getMoreExp() + " <dark_gray>/ <color:#5B41A5>10"),
                        OtherHelper.mm("<color:#A68EEA>| <gray>Koszt: <white>1 <gray>punktów prestiżu"),
                        OtherHelper.mm("<color:#A68EEA>| <gray>Bonus: <white>+x0.25 <gray>na level"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<yellow>Kliknij, lewym aby ulepszyć!"),
                        OtherHelper.mm("<yellow>Kliknij, prawym aby cofnąć ulepszenia!")

                )
                .asGuiItem(evente -> {
                    gui.close(p);

                    if (evente.isLeftClick()) {
                        if (u.getUserPrestiz().getMoreExp() >= 10) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cUlepszono na maksymalny poziom!"));
                            u.getUserPrestiz().setMoreExp(10);
                            return;
                        }

                        if (!(u.getUserPrestiz().getPunktyPrestizu() >= 1)) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie posiadasz 1 punktu prestiżu!"));
                            return;
                        }
                        u.getUserPrestiz().removePunktyPrestizu(1);
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie ulepszono!"));
                        u.getUserPrestiz().addMoreExp(1);
                    } else if (evente.isRightClick()) {
                        if (!(u.getUserPrestiz().getMoreExp() >= 1)) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz nic ulepszonego!"));
                            return;
                        }
                        u.getUserPrestiz().addPunktyPrestizu(u.getUserPrestiz().getMoreExp());
                        u.getUserPrestiz().setMoreExp(0);
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zwrócono punkty prestiżu!"));
                    }

                    BoostsHandler.checkBonuses(p);
                });

        GuiItem vpln = ItemBuilder.from(Material.GREEN_DYE)
                .name(OtherHelper.mm("<color:#5CCF2B>Zdobywaj więcej vPLN"))
                .lore(
                        OtherHelper.mm("<gray>Zwiększ szanse na zdobywanie"),
                        OtherHelper.mm("<gray>vplnów podczas Twojego kopania."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#5CCF2B>| <gray>Poziom: <color:#BBF3A3>" + u.getUserPrestiz().getMoreVpln() + " <dark_gray>/ <color:#5FA541>10"),
                        OtherHelper.mm("<color:#5CCF2B>| <gray>Koszt: <white>1 <gray>punktów prestiżu"),
                        OtherHelper.mm("<color:#5CCF2B>| <gray>Bonus: <white>+x0.05 <gray>na level"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<yellow>Kliknij, lewym aby ulepszyć!"),
                        OtherHelper.mm("<yellow>Kliknij, prawym aby cofnąć ulepszenia!")

                )
                .asGuiItem(evente -> {
                    gui.close(p);
                    if (evente.isLeftClick()) {
                        if (u.getUserPrestiz().getMoreVpln() >= 10) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cUlepszono na maksymalny poziom!"));
                            u.getUserPrestiz().setMoreVpln(10);
                            return;
                        }
                        if (!(u.getUserPrestiz().getPunktyPrestizu() >= 1)) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie posiadasz 1 punktu prestiżu!"));
                            return;
                        }
                        u.getUserPrestiz().removePunktyPrestizu(1);
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie ulepszono!"));
                        u.getUserPrestiz().addMoreVpln(1);
                    } else if (evente.isRightClick()) {
                        if (!(u.getUserPrestiz().getMoreVpln() >= 1)) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz nic ulepszonego!"));
                            return;
                        }
                        u.getUserPrestiz().addPunktyPrestizu(u.getUserPrestiz().getMoreVpln());
                        u.getUserPrestiz().setMoreVpln(0);
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zwrócono punkty prestiżu!"));
                    }
                    BoostsHandler.checkBonuses(p);
                });


        GuiItem speed = ItemBuilder.from(Material.FEATHER)
                .name(OtherHelper.mm("<color:#2BCFBB>Biegaj szybciej"))
                .lore(
                        OtherHelper.mm("<gray>Zwiększ swoją prędkość"),
                        OtherHelper.mm("<gray>poruszania."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#2BCFBB>| <gray>Poziom: <color:#93F5E9>" + u.getUserPrestiz().getMoreSpeed() + " <dark_gray>/ <color:#2E9B8E>10"),
                        OtherHelper.mm("<color:#2BCFBB>| <gray>Koszt: <white>1 <gray>punktów prestiżu"),
                        OtherHelper.mm("<color:#2BCFBB>| <gray>Bonus: <white>+x0.05 <gray>na level"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<yellow>Kliknij, lewym aby ulepszyć!"),
                        OtherHelper.mm("<yellow>Kliknij, prawym aby cofnąć ulepszenia!")

                )
                .asGuiItem(evente -> {
                    gui.close(p);
                    if (evente.isLeftClick()) {
                        if (u.getUserPrestiz().getMoreSpeed() >= 10) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cUlepszono na maksymalny poziom!"));
                            u.getUserPrestiz().setMoreSpeed(10);
                            return;
                        }
                        if (!(u.getUserPrestiz().getPunktyPrestizu() >= 1)) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie posiadasz 1 punktu prestiżu!"));
                            return;
                        }
                        u.getUserPrestiz().removePunktyPrestizu(1);
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie ulepszono!"));
                        u.getUserPrestiz().addMoreSpeed(1);
                    } else if (evente.isRightClick()) {
                        if (!(u.getUserPrestiz().getMoreSpeed() >= 1)) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz nic ulepszonego!"));
                            return;
                        }
                        u.getUserPrestiz().addPunktyPrestizu(u.getUserPrestiz().getMoreSpeed());
                        u.getUserPrestiz().setMoreSpeed(0);
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zwrócono punkty prestiżu!"));
                    }
                    BoostsHandler.checkBonuses(p);
                });

        GuiItem mnoznik = ItemBuilder.from(Material.SUNFLOWER)
                .name(OtherHelper.mm("<color:#E7DA34>Mnożnik vPLN"))
                .lore(
                        OtherHelper.mm("<gray>Zwiększ swój stały mnożnik"),
                        OtherHelper.mm("<gray>vpln podczas kopania."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#E7DA34>| <gray>Poziom: <color:#F5EE93>" + u.getUserPrestiz().getMnoznikVpln() + " <dark_gray>/ <color:#9B921B>10"),
                        OtherHelper.mm("<color:#E7DA34>| <gray>Koszt: <white>1 <gray>punktów prestiżu"),
                        OtherHelper.mm("<color:#E7DA34>| <gray>Bonus: <white>+x0.05 <gray>na level"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<yellow>Kliknij, lewym aby ulepszyć!"),
                        OtherHelper.mm("<yellow>Kliknij, prawym aby cofnąć ulepszenia!")

                )
                .asGuiItem(evente -> {
                    gui.close(p);
                    if (evente.isLeftClick()) {
                        if (u.getUserPrestiz().getMnoznikVpln() >= 10) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cUlepszono na maksymalny poziom!"));
                            u.getUserPrestiz().setMnoznikVpln(10);
                            return;
                        }
                        if (!(u.getUserPrestiz().getPunktyPrestizu() >= 1)) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie posiadasz 1 punktu prestiżu!"));
                            return;
                        }
                        u.getUserPrestiz().removePunktyPrestizu(1);
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie ulepszono!"));
                        u.getUserPrestiz().addMnoznikVpln(1);
                    } else if (evente.isRightClick()) {
                        if (!(u.getUserPrestiz().getMnoznikVpln() >= 1)) {
                            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz nic ulepszonego!"));
                            return;
                        }
                        u.getUserPrestiz().addPunktyPrestizu(u.getUserPrestiz().getMnoznikVpln());
                        u.getUserPrestiz().setMnoznikVpln(0);
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zwrócono punkty prestiżu!"));
                    }

                    BoostsHandler.checkBonuses(p);
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

        GuiItem cofnij = ItemBuilder.from(Material.BARRIER)
                .name(OtherHelper.mm("<color:#FF4949>Cofnij"))
                .lore(
                        OtherHelper.mm("<gray>Cofnij do poprzedniej strony")
                )
                .asGuiItem(evente -> {
                    prestizGui(p);
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

        gui.setItem(20, exp);
        gui.setItem(21, vpln);
        gui.setItem(23, speed);
        gui.setItem(24, mnoznik);

        gui.setItem(40, cofnij);

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
