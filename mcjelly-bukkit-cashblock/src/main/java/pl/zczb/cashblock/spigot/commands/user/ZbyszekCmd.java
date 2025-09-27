package pl.zczb.cashblock.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Cashblock;
import pl.zczb.Controller;
import pl.zczb.cashblock.boss.BossHandler;
import pl.zczb.cashblock.database.zbyszek.models.ZbyszekDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.ZbyszekPacket;

import java.util.Arrays;
import java.util.Collections;


@Command(name = "zbyszek")
public class ZbyszekCmd {
    @Execute
    public void onCommand(@Context CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tylko gracze mogą używać tej komendy!");

            return;
        }

        Player p = (Player) sender;
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Zbyszek")))
                .type(GuiType.HOPPER)
                .disableAllInteractions()
                .create();

        ZbyszekDataModel zbyszek_piniata = Cashblock.getInstance().getZbyszekHandler().getZbyszek("piniata");
        ZbyszekDataModel zbyszek_turbokasa = Cashblock.getInstance().getZbyszekHandler().getZbyszek("turbokasa");
        ZbyszekDataModel zbyszek_deszczkluczy = Cashblock.getInstance().getZbyszekHandler().getZbyszek("deszczkluczy");

        GuiItem piniata = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQyY2UzNDU2YzNhODM1OTlmMWNiYjVlZmFlZGY1MGY5NDI0YjhlZGQyMDk4NjNhNmI2NjFmMzM5MDk2NWM1ZCJ9fX0=")
                .name(Component.text(GlobalHelper.fixColor("&e&lPiniata")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Event &6piniata &7zostanie aktywowany gdy")),
                        Component.text(GlobalHelper.fixColor(" &7gracze wpłacą 250 &bOdłamków kosmosu")),
                        Component.text(GlobalHelper.fixColor(" &7Event ten polega na pojawieniu się")),
                        Component.text(GlobalHelper.fixColor(" &6eventowej lamy &7na &cstrefie pvp&7!")),
                        Component.text(GlobalHelper.fixColor(" &7")),
                        Component.text(GlobalHelper.fixColor(" &7Aby zdobyć nagrody trzeba uderzać &6piniate")),
                        Component.text(GlobalHelper.fixColor(" &7")),
                        Component.text(GlobalHelper.fixColor(" &7Wpłacono: &f" + zbyszek_piniata.getBalance() + "/250")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby wpłacić"))
                )
                .asGuiItem(evente -> {
                    openInput(p, "piniata");

                });

        GuiItem turbo = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQ0ZGViZmRhOGVjYzRjOWNmNDczNWU3YzgyNDM5MjkxOTE1YTcxNjgyZTRjNGI4NDhlZmIxNDEwNmUwYzNkNSJ9fX0=")
                .name(Component.text(GlobalHelper.fixColor("&b&lTurboKasa")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Event &3TurboKasa &7zostanie aktywowany gdy")),
                        Component.text(GlobalHelper.fixColor(" &7gracze wpłacą 250 &bOdłamków kosmosu")),
                        Component.text(GlobalHelper.fixColor(" &7Event ten polega na zwiększonym")),
                        Component.text(GlobalHelper.fixColor(" &6dropie vplnów &7z kamienia &arazy 2&7!")),
                        Component.text(GlobalHelper.fixColor(" &7")),
                        Component.text(GlobalHelper.fixColor(" &7Event ten bedzię trwać przez &630 minut")),
                        Component.text(GlobalHelper.fixColor(" &7")),
                        Component.text(GlobalHelper.fixColor(" &7Wpłacono: &f" + zbyszek_turbokasa.getBalance() + "/250")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby wpłacić"))
                )
                .asGuiItem(evente -> {

                    openInput(p, "turbokasa");
                });


        GuiItem klucze = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY0YTEwZmJkYjQ5MTNjZjc1ZDU3YjZhZDU1Y2MwZTM0ZGUxYWYxZjMzODgyMTA2ZWFlMGY4ZWMwMGE2ZjY4In19fQ==")
                .name(Component.text(GlobalHelper.fixColor("&d&lDeszcz kluczy")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Event &5Deszcz kluczy &7zostanie aktywowany gdy")),
                        Component.text(GlobalHelper.fixColor(" &7gracze wpłacą 500 &bOdłamków kosmosu")),
                        Component.text(GlobalHelper.fixColor(" &7Event ten polega na rozdaniu")),
                        Component.text(GlobalHelper.fixColor(" &5Epickich kluczy &7każdemu na serwerze razy 2!")),
                        Component.text(GlobalHelper.fixColor(" &7")),
                        Component.text(GlobalHelper.fixColor(" &7Wpłacono: &f" + zbyszek_deszczkluczy.getBalance() + "/500")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby wpłacić"))
                )
                .asGuiItem(evente -> {

                    openInput(p, "deszczkluczy");
                });


        gui.setItem(1, piniata);
        gui.setItem(2, turbo);
        gui.setItem(3, klucze);

        gui.open(p);
    }

    public void openInput(Player p, String name) {

        new AnvilGUI.Builder()
                .onClose(stateSnapshot -> {

                })
                .itemLeft(ItemBuilder.from(Material.AMETHYST_SHARD)
                        .name(Component.text(GlobalHelper.fixColor("&b&lOdłamek kosmosu")))
                        .lore(
                                Component.text(GlobalHelper.fixColor("")),
                                Component.text(GlobalHelper.fixColor(" &7Jest to odłamek który możesz wpłacić &f/zbyszek")),
                                Component.text(GlobalHelper.fixColor(" &7oraz możesz wymienić go na &5&lMaterie kosmosu")),
                                Component.text(GlobalHelper.fixColor(""))
                        )
                        .glow()
                        .amount(1)
                        .build())
                .itemOutput(ItemBuilder.from(Material.LIME_DYE).setName(GlobalHelper.fixColor("&aPotwierdz")).build())
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }


                    int amount;
                    try {
                        amount = Integer.parseInt(stateSnapshot.getText());
                    } catch (NumberFormatException e) {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("Zła liczba!"));
                    }


                    if (amount < 0) {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("Zła liczba!"));
                    }


                    if (!OtherHelper.hasItem(p, GlobalHelper.fixColor("&b&lOdłamek kosmosu"), amount)) {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("Nie masz tyle!"));
                    }

                    if(BossHandler.isLama()) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cAktualnie trwa event na warp pvp!"));
                        return Arrays.asList(AnvilGUI.ResponseAction.close());
                    }


                    if (OtherHelper.removeItemByName(p, GlobalHelper.fixColor("&b&lOdłamek kosmosu"), amount)) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aWpłaciłeś &2" + amount + " &aodłamkow"));

                        Controller.getInstance().getRedis().publish("CH|cashblock_tryb", new ZbyszekPacket("add", name, amount));
                    } else {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie udało sie wpłacić :c"));

                    }

                    return Arrays.asList(AnvilGUI.ResponseAction.close());

                })
                .text("1")
                .title("Wpisz ilość:")
                .plugin(Cashblock.getInstance())
                .open(p);

    }
}