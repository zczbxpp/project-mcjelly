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
import pl.zczb.Cashblock;
import pl.zczb.cashblock.brush.BrushItem;
import pl.zczb.cashblock.brush.BrushPluginConfiguration;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.helpers.GlobalHelper;

import java.util.Map;

@Command(name = "kowal")
public class KowalCmd {
    private final BrushPluginConfiguration configuration;

    public KowalCmd(BrushPluginConfiguration configuration) {
        this.configuration = configuration;
    }


    @Execute
    public void onChannel(@Context CommandSender sender) {

        final Player p = (Player) sender;

        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);


        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Ulepszanie brusha")))
                .rows(5)

                .disableAllInteractions()
                .create();


        GuiItem upgradeBrush = ItemBuilder.from(Material.BLAZE_ROD)
                .name(Component.text(GlobalHelper.fixColor("&5&lᴜʟᴇᴘsᴢᴇɴɪᴇ ʙʀᴜsʜᴀ")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Tutaj możesz ulepszyć swojego brusha!")),
                        Component.text(GlobalHelper.fixColor(" &7aby go ulepszyć potrzebujesz &dMagiczny kamień")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby ulepszyć brusha!"))
                )
                .asGuiItem(event -> {

                    openKowalGui(p);

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

        gui.setItem(22, upgradeBrush);


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


    public void openKowalGui(Player p) {

        BrushItem item = configuration.findByItemStack2(p.getInventory().getItemInMainHand());
        if (item == null) {
            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cMusisz trzymać brusha w ręce"));
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FMusisz trzymać brusha w ręce"));
            return;
        }
        if (item.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(GlobalHelper.fixColor("&d✦ &5&lBrush &d&l3x3 &7(Testowy) &d✦"))) {
            p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie możesz ulepszyć testowego brusha"));
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz ulepszyć testowego brusha"));
            return;
        }


        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Ulepszanie brusha")))
                .rows(5)

                .disableAllInteractions()
                .create();


        GuiItem ulepszenie25 = ItemBuilder.from(Material.NETHER_STAR)
                .name(Component.text(GlobalHelper.fixColor("&5&lᴜʟᴇᴘsᴢᴇɴɪᴇ 25%")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Ulepszenie&8:")),
                        Component.text(GlobalHelper.fixColor(" &7Aby ulepszyć potrzebujesz &51 &dMagiczny kamień")),
                        Component.text(GlobalHelper.fixColor(" &7Masz &525% &7szans na powodzenie")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby ulepszyć"))
                )
                .asGuiItem(event -> {

                    BrushItem item2 = configuration.findByItemStack2(p.getInventory().getItemInMainHand());

                    if (item2 == null) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cMusisz trzymać brusha w ręce"));
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FMusisz trzymać brusha w ręce"));
                        return;
                    }

                    if (!configuration.hasSameNameInLore(p.getInventory().getItemInMainHand(), p.getName())) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie jesteś włascicielem brusha!"));
                        return;
                    }

                    if (item2.getName().equals("9x9")) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cMasz juz maksymalny poziom brusha"));
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FMasz juz maksymalny poziom brusha"));
                        return;
                    }
                    ItemStack dragonEgg = ItemBuilder.from(Material.DRAGON_EGG)
                            .glow()
                            .name(Component.text(GlobalHelper.fixColor("&d&lMagiczny kamień")))
                            .lore(
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor(" &7Kamień potrzebny jest do ulepszenia brusha")),
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor("&4UWAGA: &cJest to najcenniejszy przedmiot na serwerze")),
                                    Component.text(GlobalHelper.fixColor("&cBrusha możesz ulepszyć pod komendą /kowal")),
                                    Component.text(GlobalHelper.fixColor(""))
                            )
                            .asGuiItem().getItemStack();
                    int requiredAmount = 1;

                    if (!OtherHelper.hasItem(p, dragonEgg, requiredAmount)) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cPotrzebujesz 1 magiczny kamień!"));
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz wystarczającej ilości magicznych kamieni!"));
                        return;
                    }


                    OtherHelper.removeItem(p, dragonEgg, requiredAmount);

                    if (OtherHelper.getChance(15.00D)) {
                        upgradeBrush(p);
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUdało ci się ulepszyć brusha :)"));
                        p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Udało ci się ulepszyć brusha!"));
                        return;
                    }
                    downgradeBrush(p);
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNiestety nie udało ci sie :c"));
                    p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNiestety nie udało ci się ulepszyć brusha"));
                });

        GuiItem ulepszenie50 = ItemBuilder.from(Material.NETHER_STAR)
                .name(Component.text(GlobalHelper.fixColor("&5&lᴜʟᴇᴘsᴢᴇɴɪᴇ 50%")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Ulepszenie&8:")),
                        Component.text(GlobalHelper.fixColor(" &7Aby ulepszyć potrzebujesz &52 &dMagiczne kamienie")),
                        Component.text(GlobalHelper.fixColor(" &7Masz &550% &7szans na powodzenie")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby ulepszyć"))
                )
                .asGuiItem(event -> {

                    BrushItem item2 = configuration.findByItemStack2(p.getInventory().getItemInMainHand());

                    if (item2 == null) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cMusisz trzymać brusha w ręce"));
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FMusisz trzymać brusha w ręce"));
                        return;
                    }
                    if (!configuration.hasSameNameInLore(p.getInventory().getItemInMainHand(), p.getName())) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie jesteś włascicielem brusha!"));
                        return;
                    }
                    if (item2.getName().equals("9x9")) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cMasz juz maksymalny poziom brusha"));
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FMasz juz maksymalny poziom brusha"));
                        return;
                    }
                    ItemStack dragonEgg = ItemBuilder.from(Material.DRAGON_EGG)
                            .glow()
                            .name(Component.text(GlobalHelper.fixColor("&d&lMagiczny kamień")))
                            .lore(
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor(" &7Kamień potrzebny jest do ulepszenia brusha")),
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor("&4UWAGA: &cJest to najcenniejszy przedmiot na serwerze")),
                                    Component.text(GlobalHelper.fixColor("&cBrusha możesz ulepszyć pod komendą /kowal")),
                                    Component.text(GlobalHelper.fixColor(""))
                            )
                            .asGuiItem().getItemStack();
                    int requiredAmount = 2;

                    if (!OtherHelper.hasItem(p, dragonEgg, requiredAmount)) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cPotrzebujesz 2 magiczne kamienie!"));
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz wystarczającej ilości magicznych kamieni!"));
                        return;
                    }


                    OtherHelper.removeItem(p, dragonEgg, requiredAmount);
                    if (OtherHelper.getChance(35.00D)) {
                        upgradeBrush(p);
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUdało ci się ulepszyć brusha :)"));
                        p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Udało ci się ulepszyć brusha!"));
                        return;
                    }
                    downgradeBrush(p);
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNiestety nie udało ci sie :c"));
                    p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNiestety nie udało ci się ulepszyć brusha"));
                });

        GuiItem emerald = ItemBuilder.from(Material.EMERALD)
                .name(Component.text(GlobalHelper.fixColor("&5&lᴊᴀᴋ ᴛᴏ ᴅᴢɪᴀʟᴀ?")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jesli ulepszenie sie &auda &7wtedy")),
                        Component.text(GlobalHelper.fixColor(" &7dostajesz &dbrusha &7poziom wyżej!")),
                        Component.text(GlobalHelper.fixColor(" ")),
                        Component.text(GlobalHelper.fixColor(" &7Natomiast jesli ulepszenie sie &cnie uda")),
                        Component.text(GlobalHelper.fixColor(" &7wtedy poziom twojego &dbrusha &7spada niżej")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Najniższy poziom brusha to &d1")),
                        Component.text(GlobalHelper.fixColor(" &7Jeśli twój brush ma poziom pierwszy")),
                        Component.text(GlobalHelper.fixColor(" &7i ulepszenie sie nie uda to brush zostaje")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Do ulepszenia potrzebujesz &dMagicznych kamieni")),
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

        gui.setItem(4, emerald);

        gui.setItem(5, white);
        gui.setItem(6, white);
        gui.setItem(7, yellow);
        gui.setItem(8, orange);

        gui.setItem(9, yellow);
        gui.setItem(17, yellow);

        gui.setItem(21, ulepszenie25);

        gui.setItem(23, ulepszenie50);


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


    public void upgradeBrush(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (!itemInHand.hasItemMeta() || !itemInHand.getItemMeta().hasDisplayName()) {
            return;
        }

        Map<String, String> upgradeMap = Map.of(
                "3x3", "5x5",
                "5x5", "7x7",
                "7x7", "9x9"
        );

        String currentName = itemInHand.getItemMeta().getDisplayName();

        for (Map.Entry<String, String> entry : upgradeMap.entrySet()) {
            if (currentName.contains(entry.getKey())) {
                String nextLevelName = entry.getValue();
                BrushItem nextBrush = configuration.findByName(nextLevelName);

                if (nextBrush != null) {
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                    ItemStack personalized = OtherHelper.personalizeBrush(nextBrush.getItemStack(), player.getName());
                    player.getInventory().addItem(personalized);
                }
                return;
            }
        }
    }

    public void downgradeBrush(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (!itemInHand.hasItemMeta() || !itemInHand.getItemMeta().hasDisplayName()) {
            return;
        }

        Map<String, String> downgradeMap = Map.of(
                "5x5", "3x3",
                "7x7", "5x5",
                "9x9", "7x7"
        );

        String currentName = itemInHand.getItemMeta().getDisplayName();

        for (Map.Entry<String, String> entry : downgradeMap.entrySet()) {
            if (currentName.contains(entry.getKey())) {
                String previousLevelName = entry.getValue();
                BrushItem previousBrush = configuration.findByName(previousLevelName);

                if (previousBrush != null) {
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                    ItemStack personalized = OtherHelper.personalizeBrush(previousBrush.getItemStack(), player.getName());
                    player.getInventory().addItem(personalized);
                }
                return;
            }
        }
    }
}