package pl.zczb.itemshop.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.zczb.Controller;
import pl.zczb.Itemshop;
import pl.zczb.itemshop.data.ItemshopRegistry;
import pl.zczb.itemshop.data.ShopItem;
import pl.zczb.itemshop.data.user.models.UserDataModel;
import pl.zczb.itemshop.helpers.GlobalHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Command(name = "itemshop", aliases = {"is", "portfel"})
public class ItemshopCmd {
    @Execute
    public void openShopGui(@Context Player p) {
        if (!ItemshopRegistry.hasItems()) {
            p.sendMessage(GlobalHelper.fixColor("&8[&4&l!&8] &cNa tym trybie nie ma sklepu itemshop!"));


            return;
        }

        Gui gui = Gui.gui()
                .title(Component.text(pl.zczb.helpers.GlobalHelper.fixColor("&8Itemshop")))
                .rows(6)
                .disableAllInteractions()
                .create();


        GuiItem yellow = ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE)
                .name(Component.text(pl.zczb.helpers.GlobalHelper.fixColor(" ")))
                .asGuiItem();
        GuiItem orange = ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
                .name(Component.text(pl.zczb.helpers.GlobalHelper.fixColor(" ")))
                .asGuiItem();

        gui.setItem(0, orange);
        gui.setItem(1, yellow);
        gui.setItem(2, yellow);

        gui.setItem(6, yellow);
        gui.setItem(7, orange);
        gui.setItem(8, yellow);

        gui.setItem(9, yellow);
        gui.setItem(17, yellow);

        gui.setItem(36, yellow);
        gui.setItem(44, yellow);

        gui.setItem(45, orange);
        gui.setItem(46, yellow);
        gui.setItem(47, yellow);

        gui.setItem(51, yellow);
        gui.setItem(52, yellow);
        gui.setItem(53, orange);


        UserDataModel user = Itemshop.getInstance().getUserHandler().getPlayer(p);

        GuiItem gracz = ItemBuilder.from(Material.PLAYER_HEAD)
                .name(Component.text(pl.zczb.helpers.GlobalHelper.fixColor("&6&lPortfel")))
                .lore(
                        Component.text(pl.zczb.helpers.GlobalHelper.fixColor("")),
                        Component.text(pl.zczb.helpers.GlobalHelper.fixColor(" &7Posiadasz &e" + user.getPln() + " zł")),
                        Component.text(pl.zczb.helpers.GlobalHelper.fixColor(""))
                )
                .asGuiItem();

        ItemStack item2 = gracz.getItemStack();
        SkullMeta skullMeta = (SkullMeta) item2.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwner(p.getName());
            item2.setItemMeta(skullMeta);
        }
        gui.setItem(49, gracz);

        for (ShopItem shopItem : ItemshopRegistry.getItems()) {

            ItemStack itemToDisplay = shopItem.getItem().clone();
            ItemMeta itemMeta = itemToDisplay.getItemMeta();

            if (itemMeta != null) {

                List<String> lore = itemMeta.hasLore() ? new ArrayList<>(itemMeta.getLore()) : new ArrayList<>();

                double originalPrice = shopItem.getPrice();
                double finalPrice = shopItem.getFinalPrice();
                lore.add(" ");

                if (finalPrice < originalPrice) {
                    String oldPriceText = String.format("%.2f", originalPrice);
                    String newPriceText = String.format("%.2f", finalPrice);

                    lore.add(GlobalHelper.fixColor("&7Cena: &c&m" + oldPriceText + "zł"));
                    lore.add(GlobalHelper.fixColor("&7Promocja: &a" + newPriceText + "zł"));
                } else {
                    String priceText = String.format("%.2f", finalPrice);
                    lore.add(GlobalHelper.fixColor("&7Cena: &e" + priceText + "zł"));
                }

                lore.add(" ");
                lore.add(GlobalHelper.fixColor("&aKliknij, aby kupić"));

                itemMeta.setLore(lore);
                itemToDisplay.setItemMeta(itemMeta);
            }


            gui.setItem(shopItem.getSlot(), new GuiItem(itemToDisplay, event -> {
                Player clicker = (Player) event.getWhoClicked();
                UserDataModel u = Itemshop.getInstance().getUserHandler().getPlayer(clicker);

                double cost = shopItem.getFinalPrice();

                if (u.getPln() < cost) {
                    clicker.sendMessage(GlobalHelper.fixColor("&8[&4&l!&8] &cNie masz wystarczająco pieniędzy!"));
                    return;
                }

                clicker.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zakupiono usługę!"));

                Controller.getInstance().getRedis().publish("CH|itemshop", new ItemshopBuyPacket(p.getName(), cost));

                shopItem.getOnBuy().accept(clicker);
            }));
        }

        gui.open(p);
    }


    @Execute
    @Permission("zczb.root")
    public void giveCurrency(@Context CommandSender sender, @Arg("gracz") UserDataModel user, @Arg("kwota") double amount, @Arg("ogloszenie") boolean broadcast) {
        if (user == null) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&4&l!&8] &cNie ma takiego gracza!"));
            return;
        }
        if (amount <= 0.0D) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&4&l!&8] &cIlość musi być większa niż 0!"));

            return;
        }
        Controller.getInstance().getRedis().publish("CH|itemshop", new ItemshopPacket(user.getNick(), amount, broadcast));
        sender.sendMessage(GlobalHelper.fixColor("&aPomyślnie nadano &e" + amount + " zł &agraczowi &e" + user.getNick()));
    }

    private void populateShopItems(Gui gui, Player player) {
        for (Iterator<ShopItem> iterator = ItemshopRegistry.getItems().iterator(); iterator.hasNext(); ) {
            ShopItem shopItem = iterator.next();
            ItemStack item = shopItem.getItem().clone();
            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta != null) {
                List<String> lore = new ArrayList<>((itemMeta.getLore() != null) ? itemMeta.getLore() : new ArrayList<>());
                lore.add("");
                lore.add(GlobalHelper.fixColor("&7Cena: &e" + shopItem.getPrice() + "zł"));
                lore.add(" ");
                lore.add(GlobalHelper.fixColor("&aKliknij, aby kupić"));
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
            }

            GuiItem guiItem = new GuiItem(GlobalHelper.personalizeBrush(ItemBuilder.from(item).build(), player.getName()), event -> {
                Player clicker = (Player) event.getWhoClicked();

                UserDataModel u = Itemshop.getInstance().getUserHandler().getPlayer(clicker);

                if (u.getPln() < shopItem.getPrice()) {
                    clicker.sendMessage(GlobalHelper.fixColor("&8[&4&l!&8] &cNie masz wystarczająco pieniędzy!"));
                    return;
                }
                clicker.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zakupiono usługę!"));
                Controller.getInstance().getRedis().publish("CH|itemshop", new ItemshopBuyPacket(player.getName(), shopItem.getPrice()));
                shopItem.getOnBuy().accept(clicker);
            });
            gui.setItem(shopItem.getSlot(), guiItem);
        }

    }

    private void addPlayerWalletInfo(Gui gui, Player player) {
        UserDataModel user = Itemshop.getInstance().getUserHandler().getPlayer(player);


        ItemStack head = ((ItemBuilder) ((ItemBuilder) ItemBuilder.from(Material.PLAYER_HEAD).name((Component) Component.text(GlobalHelper.fixColor("&6&lPortfel")))).lore(new Component[]{(Component) Component.text(""), (Component) Component.text(GlobalHelper.fixColor(" &7Posiadasz &e" + user.getPln() + " zł")), (Component) Component.text("")})).build();

        ItemMeta itemMeta = head.getItemMeta();
        if (itemMeta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            skullMeta.setOwningPlayer((OfflinePlayer) player);
            head.setItemMeta((ItemMeta) skullMeta);
        }

        gui.setItem(49, new GuiItem(head));
    }

    private void applyGuiDecoration(Gui gui) {
        GuiItem yellow = ((ItemBuilder) ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).name((Component) Component.text(" "))).asGuiItem();
        GuiItem orange = ((ItemBuilder) ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE).name((Component) Component.text(" "))).asGuiItem();
        int[] yellowSlots = {1, 2, 6, 8, 9, 17, 36, 44, 46, 47, 51, 52};
        int[] orangeSlots = {0, 7, 45, 53};
        for (int slot : yellowSlots) gui.setItem(slot, yellow);
        for (int slot : orangeSlots) gui.setItem(slot, orange);
    }
}


