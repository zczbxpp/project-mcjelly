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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.config.data.Kit;
import pl.zczb.tools.database.user.models.UserDataModel;
import pl.zczb.tools.helpers.DataUtil;

import java.util.List;
import java.util.stream.Collectors;

@Command(name = "kit", aliases = {"zestawy", "zestaw"})
public class KitCmd {
    @Execute
    public void onKitCommand(@Context CommandSender sender) {
        Player p = (Player) sender;
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Kit")))
                .rows(6)
                .disableAllInteractions()
                .create();

        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);
        for (Kit kit : Tools.getKitConfig().values()) {


            Material material = Material.getMaterial(kit.getMaterial().toUpperCase());
            if (material == null) continue;

            List<Component> lore = kit.getLore().stream()
                    .map(line -> GlobalHelper.fixColor(line.replace("{isAvaible}",
                            (!p.hasPermission("zczb.gracz") ? "&fNie." :
                                    (u.getUserKits().isCooldownForKit(kit.getName()) ? ("&f" + DataUtil.secondsToString(u.getUserKits().getCooldownForKit(kit.getName()))) : "&fTak.")))))
                    .map(Component::text)
                    .collect(Collectors.toList());


            GuiItem item = ItemBuilder.from(material)
                    .name(Component.text(GlobalHelper.fixColor(kit.getLabel())))
                    .lore(lore)
                    .asGuiItem(e -> {
                        openKitDetailsGUI(p, kit);
                    });


            gui.setItem(kit.getSlot(), item);

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


        gui.setItem(36, yellow);
        gui.setItem(44, yellow);

        gui.setItem(45, orange);
        gui.setItem(46, yellow);
        gui.setItem(47, white);
        gui.setItem(48, white);

        gui.setItem(50, white);
        gui.setItem(51, white);
        gui.setItem(52, yellow);
        gui.setItem(53, orange);

        gui.open(p);
    }

    private void openKitDetailsGUI(Player p, Kit kit) {


        UserDataModel user = Tools.getInstance().getUserHandler().getPlayer(p);
        if (user == null) {
            p.sendMessage(GlobalHelper.fixColor("&cWystąpił błąd podczas ładowania danych."));
            return;
        }


        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Kit")))
                .rows(6)
                .disableAllInteractions()
                .create();

        List<ItemStack> rewardItems = kit.buildRewardItems(p);
        for (int i = 0; i < rewardItems.size() && i < 45; i++) {
            gui.setItem(i, new GuiItem(rewardItems.get(i)));
        }

        GuiItem odbierz = ItemBuilder.from(Material.LIME_DYE)
                .name(Component.text(GlobalHelper.fixColor("&aOdbiesz zestaw")))
                .asGuiItem(event -> {
                    if (!p.hasPermission(kit.getPermission())) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie posiadasz dostępu do tego zestawu"));
                        return;
                    }

                    if (!p.hasPermission("zczb.mod") && user.getUserKits().isCooldownForKit(kit.getName())) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNastepny zestaw bedziesz mogl odebrac za " + DataUtil.secondsToString(user.getUserKits().getCooldownForKit(kit.getName()))));
                        gui.close(p);
                        return;
                    }

                    user.getUserKits().setCooldownForKit(kit.getName(), System.currentTimeMillis() + (kit.getCooldown() * 1000L));

                    for (ItemStack item : rewardItems) {
                        if (item.getType() == Material.TRIPWIRE_HOOK) continue;

                        ItemStack clonedItem = item.clone();
                        ItemMeta meta = clonedItem.getItemMeta();

                        if (meta != null) {

                            if (meta.hasLore()) {
                                List<String> updatedLore = meta.getLore().stream()
                                        .map(line -> line.replace("%player%", p.getName()))
                                        .collect(Collectors.toList());
                                meta.setLore(updatedLore);
                            }

                            clonedItem.setItemMeta(meta);
                        }

                        p.getInventory().addItem(clonedItem);
                    }


                    for (String cmd : kit.getRewardCommands()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                cmd.replace("{PLAYER}", p.getName()));
                    }

                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aOdebrałeś zestaw " + kit.getName() + "!"));
                    p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Odebrałeś zestaw " + kit.getName()));
                    p.closeInventory();


                });

        gui.setItem(53, odbierz);
        gui.open(p);
    }
}


