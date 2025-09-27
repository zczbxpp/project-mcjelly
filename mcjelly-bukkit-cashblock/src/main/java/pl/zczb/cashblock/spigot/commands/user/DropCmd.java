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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.helpers.GlobalHelper;

@Command(name = "drop")
public class DropCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Drop")))
                .rows(5)

                .disableAllInteractions()
                .create();
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);


        GuiItem piniata = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQyY2UzNDU2YzNhODM1OTlmMWNiYjVlZmFlZGY1MGY5NDI0YjhlZGQyMDk4NjNhNmI2NjFmMzM5MDk2NWM1ZCJ9fX0=")
                .name(OtherHelper.mm("<gray>Drop z bossa <color:#FFFC83>Piniata"))
                .lore(
                        OtherHelper.mm("<gray>W tym miejscu możesz sprawdzić"),
                        OtherHelper.mm("<gray>nagrody/drop z eventu piniata"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<yellow>Kliknij, aby przejść dalej")

                )
                .asGuiItem(evente -> {
                    openPiniataDrop(p);
                });


        GuiItem golem = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI3MTkxM2EzZmM4ZjU2YmRmNmI5MGE0YjRlZDZhMDVjNTYyY2UwMDc2YjUzNDRkNDQ0ZmIyYjA0MGFlNTdkIn19fQ==")
                .name(OtherHelper.mm("<gray>Drop z bossa <color:#83CDFF>Lodowy golem"))
                .lore(
                        OtherHelper.mm("<gray>W tym miejscu możesz sprawdzić"),
                        OtherHelper.mm("<gray>nagrody/drop z eventu lodowy golem"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<yellow>Kliknij, aby przejść dalej")

                )
                .asGuiItem(evente -> {
                    openGolemDrop(p);
                });


        GuiItem vpln = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhkZDBlYjVjMDEwNzhmMTg0MzM0NTY1ZjEzMjQyMTE5YWI1MTQ3Nzc3NDcyMWQ2ZTUzYzcxYzM5ODYxM2E1NiJ9fX0=")
                .name(OtherHelper.mm("<gray>Drop z bloków <color:#5CD629>VPLN"))
                .lore(
                        OtherHelper.mm("<gray>W tym miejscu możesz sprawdzić"),
                        OtherHelper.mm("<gray>nagrody/drop z bloków"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<yellow>Kliknij, aby przejść dalej")

                )
                .asGuiItem(evente -> {
                    openVplnDrop(p);
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

        gui.setItem(20, vpln);
        gui.setItem(22, piniata);
        gui.setItem(24, golem);

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


    public void openVplnDrop(Player p) {

        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Drop z bloków vpln")))
                .rows(3)

                .disableAllInteractions()
                .create();


        GuiItem vpln1 = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==")
                .name(OtherHelper.mm("<gray>Kwota: <color:#5CD629>0.01 vpln"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić kopiąc"),
                        OtherHelper.mm("<gray>wszystkie bloki na mapie"),
                        OtherHelper.mm("")

                )
                .asGuiItem();
        GuiItem vpln2 = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==")
                .name(OtherHelper.mm("<gray>Kwota: <color:#5CD629>0.05 vpln"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić kopiąc"),
                        OtherHelper.mm("<gray>wszystkie bloki na mapie"),
                        OtherHelper.mm("")

                )
                .asGuiItem();

        GuiItem vpln3 = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==")
                .name(OtherHelper.mm("<gray>Kwota: <color:#5CD629>0.10 vpln"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić kopiąc"),
                        OtherHelper.mm("<gray>wszystkie bloki na mapie"),
                        OtherHelper.mm("")

                )
                .asGuiItem();
        GuiItem vpln4 = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==")
                .name(OtherHelper.mm("<gray>Kwota: <color:#5CD629>0.50 vpln"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić kopiąc"),
                        OtherHelper.mm("<gray>wszystkie bloki na mapie"),
                        OtherHelper.mm("")

                )
                .asGuiItem();
        GuiItem vpln5 = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==")
                .name(OtherHelper.mm("<gray>Kwota: <color:#5CD629>1.00 vpln"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić kopiąc"),
                        OtherHelper.mm("<gray>wszystkie bloki na mapie"),
                        OtherHelper.mm("")

                )
                .asGuiItem();

        GuiItem vpln6 = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==")
                .name(OtherHelper.mm("<gray>Kwota: <color:#5CD629>5.00 vpln"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić kopiąc"),
                        OtherHelper.mm("<gray>wszystkie bloki na mapie"),
                        OtherHelper.mm("")

                )
                .asGuiItem();


        gui.setItem(0, vpln1);
        gui.setItem(1, vpln2);
        gui.setItem(2, vpln3);
        gui.setItem(3, vpln4);

        gui.setItem(4, vpln5);
        gui.setItem(5, vpln6);

        gui.open(p);
    }

    public void openPiniataDrop(Player p) {

        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Drop z bossa piniaty")))
                .rows(3)

                .disableAllInteractions()
                .create();


        GuiItem vpln1 = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==")
                .name(OtherHelper.mm("<gray>Kwota <color:#5CD629>1 zł do portfela"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")

                )
                .asGuiItem();

        GuiItem vpln2 = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==")
                .name(OtherHelper.mm("<gray>Kwota <color:#5CD629>2 zł do portfela"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")

                )
                .asGuiItem();


        GuiItem odlamek = ItemBuilder.from(Material.AMETHYST_SHARD)
                .name(Component.text(GlobalHelper.fixColor("&b&lOdłamek kosmosu")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .glow()
                .amount(1)
                .asGuiItem();

        ItemStack rzadkaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &3&lRzadkiej")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten klucz możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .build();
        ItemMeta rzadkaMeta = rzadkaItem.getItemMeta();
        rzadkaMeta.setCustomModelData(1002);
        rzadkaItem.setItemMeta(rzadkaMeta);

        GuiItem rzadka = new GuiItem(rzadkaItem);

        ItemStack epickaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &5&lEpickiej")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten klucz możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .build();
        ItemMeta epickaMeta = epickaItem.getItemMeta();
        epickaMeta.setCustomModelData(1003);
        epickaItem.setItemMeta(epickaMeta);

        GuiItem epicka = new GuiItem(epickaItem);

        ItemStack legendarnaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &6&lLegendarnej")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten klucz możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .build();
        ItemMeta legendarnaMeta = legendarnaItem.getItemMeta();
        legendarnaMeta.setCustomModelData(1004);
        legendarnaItem.setItemMeta(legendarnaMeta);

        GuiItem legendarna = new GuiItem(legendarnaItem);


        ItemStack gigaboxItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &4&lGigabox")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten klucz możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .build();
        ItemMeta gigaboxMeta = gigaboxItem.getItemMeta();
        gigaboxMeta.setCustomModelData(1005);
        gigaboxItem.setItemMeta(gigaboxMeta);

        GuiItem gigabox = new GuiItem(gigaboxItem);


        GuiItem kamien = ItemBuilder.from(Material.DRAGON_EGG).amount(1).glow()
                .name(Component.text(GlobalHelper.fixColor("&d&lMagiczny kamień")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten kamien możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .asGuiItem();

        GuiItem glasses = ItemBuilder.from(Material.TURTLE_HELMET)
                .name(Component.text(GlobalHelper.fixColor("&b&lOkularki swagu")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten przedmiot możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")

                ).asGuiItem();

        GuiItem lornetka = ItemBuilder.from(Material.SPYGLASS)
                .name(Component.text(GlobalHelper.fixColor("&a&lLornetka")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten przedmiot możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                ).asGuiItem();


        gui.setItem(0, vpln1);
        gui.setItem(1, vpln2);
        gui.setItem(2, odlamek);
        gui.setItem(3, rzadka);

        gui.setItem(5, epicka);
        gui.setItem(6, legendarna);
        gui.setItem(7, gigabox);
        gui.setItem(8, kamien);
        gui.setItem(9, glasses);
        gui.setItem(10, lornetka);
        gui.open(p);
    }


    public void openGolemDrop(Player p) {

        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Drop z bossa golema")))
                .rows(3)

                .disableAllInteractions()
                .create();


        GuiItem vpln1 = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==")
                .name(OtherHelper.mm("<gray>Kwota <color:#5CD629>1 zł do portfela"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")

                )
                .asGuiItem();

        GuiItem vpln2 = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==")
                .name(OtherHelper.mm("<gray>Kwota <color:#5CD629>2 zł do portfela"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")

                )
                .asGuiItem();


        GuiItem odlamek = ItemBuilder.from(Material.AMETHYST_SHARD)
                .name(Component.text(GlobalHelper.fixColor("&b&lOdłamek kosmosu")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Tą kwote możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .glow()
                .amount(16)
                .asGuiItem();

        ItemStack rzadkaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &3&lRzadkiej")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten klucz możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .build();
        ItemMeta rzadkaMeta = rzadkaItem.getItemMeta();
        rzadkaMeta.setCustomModelData(1002);
        rzadkaItem.setItemMeta(rzadkaMeta);

        GuiItem rzadka = new GuiItem(rzadkaItem);

        ItemStack epickaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &5&lEpickiej")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten klucz możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .build();
        ItemMeta epickaMeta = epickaItem.getItemMeta();
        epickaMeta.setCustomModelData(1003);
        epickaItem.setItemMeta(epickaMeta);

        GuiItem epicka = new GuiItem(epickaItem);

        ItemStack legendarnaItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &6&lLegendarnej")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten klucz możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .build();
        ItemMeta legendarnaMeta = legendarnaItem.getItemMeta();
        legendarnaMeta.setCustomModelData(1004);
        legendarnaItem.setItemMeta(legendarnaMeta);

        GuiItem legendarna = new GuiItem(legendarnaItem);

        GuiItem kamien = ItemBuilder.from(Material.DRAGON_EGG).amount(1).glow()
                .name(Component.text(GlobalHelper.fixColor("&d&lMagiczny kamień")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten kamien możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .asGuiItem();

        GuiItem glasses = ItemBuilder.from(Material.TURTLE_HELMET)
                .name(Component.text(GlobalHelper.fixColor("&b&lOkularki swagu")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten przedmiot możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")

                ).asGuiItem();

        GuiItem lornetka = ItemBuilder.from(Material.SPYGLASS)
                .name(Component.text(GlobalHelper.fixColor("&a&lLornetka")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten przedmiot możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                ).asGuiItem();


        GuiItem turbo30 = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&7Voucher na &bTurboKase &8(&f30 min&8)")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten voucher możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                ).asGuiItem();

        ItemStack mvipItem = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&7Voucher na &bMVIP &8(&f7 dni&8)")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten voucher możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .build();
        ItemMeta mvipMeta = mvipItem.getItemMeta();
        mvipMeta.setCustomModelData(1003);
        mvipItem.setItemMeta(mvipMeta);
        GuiItem mvip = new GuiItem(mvipItem);

        ItemStack svipItem = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&7Voucher na &6SVIP &8(&f7 dni&8)")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten voucher możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .build();

        ItemMeta svipMeta = svipItem.getItemMeta();
        svipMeta.setCustomModelData(1002);
        svipItem.setItemMeta(svipMeta);
        GuiItem svip = new GuiItem(svipItem);

        GuiItem fly = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&7Voucher na &fFly &8(&f3 dni&8)")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten voucher możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                ).asGuiItem();


        ItemStack gigaboxItem = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &4&lGigabox")))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Ten klucz możesz trafić bijąc"),
                        OtherHelper.mm("<gray>piniate na warp pvp"),
                        OtherHelper.mm("")
                )
                .build();
        ItemMeta gigaboxMeta = gigaboxItem.getItemMeta();
        gigaboxMeta.setCustomModelData(1005);
        gigaboxItem.setItemMeta(gigaboxMeta);

        GuiItem gigabox = new GuiItem(gigaboxItem);


        gui.setItem(0, vpln1);
        gui.setItem(1, vpln2);
        gui.setItem(2, odlamek);
        gui.setItem(3, rzadka);

        gui.setItem(5, epicka);
        gui.setItem(6, legendarna);
        gui.setItem(7, gigabox);
        gui.setItem(8, kamien);
        gui.setItem(9, glasses);
        gui.setItem(10, lornetka);
        gui.setItem(11, turbo30);
        gui.setItem(12, svip);
        gui.setItem(13, mvip);
        gui.setItem(14, fly);
        gui.open(p);
    }
}
