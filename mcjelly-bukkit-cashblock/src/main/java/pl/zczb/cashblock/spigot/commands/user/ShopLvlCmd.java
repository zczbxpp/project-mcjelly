package pl.zczb.cashblock.spigot.commands.user;

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
import org.bukkit.inventory.ItemStack;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.helpers.GlobalHelper;

@Command(name = "sklepzalvl")
public class ShopLvlCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {

        final Player p = (Player) sender;

        sklepczas_gui(p);
    }


    public void sklepczas_gui(Player p) {
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Sklep za poziom")))
                .rows(5)

                .disableAllInteractions()
                .create();

        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);
        GuiItem zestaw_diamond = ItemBuilder.from(Material.DIAMOND_SWORD)
                .name(Component.text(GlobalHelper.fixColor("&7Zestaw &bDiament")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Zestaw zawiera:")),
                        Component.text(GlobalHelper.fixColor(" &8- &fHełm diamentowy 4/3 x1")),
                        Component.text(GlobalHelper.fixColor(" &8- &fKlata diamentowy 4/3 x1")),
                        Component.text(GlobalHelper.fixColor(" &8- &fSpodnie diamentowy 4/3 x1")),
                        Component.text(GlobalHelper.fixColor(" &8- &fButy diamentowy 4/3 x1")),
                        Component.text(GlobalHelper.fixColor(" &8- &fMiecz diamentowy 5/2 x1")),
                        Component.text(GlobalHelper.fixColor(" &8- &fZłote jabłka x12")),
                        Component.text(GlobalHelper.fixColor(" &8- &fEnchantowane złote jabłka x2")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Cena: &a2 lvle")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem(event -> {

                    if (u.getUserLvl().getLvl() < 2) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz tyle lvla na koncie!"));
                        return;
                    }
                    u.getUserLvl().removeLvl(2);
                    p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie zakupiłeś przedmiot!"));
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zakupiłeś przedmiot!"));
                    ItemStack helm = ItemBuilder.from(Material.DIAMOND_HELMET).amount(1)
                            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                            .enchant(Enchantment.DURABILITY, 3)
                            .build();
                    ItemStack klata = ItemBuilder.from(Material.DIAMOND_CHESTPLATE).amount(1)
                            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                            .enchant(Enchantment.DURABILITY, 3)
                            .build();
                    ItemStack spodnie = ItemBuilder.from(Material.DIAMOND_LEGGINGS).amount(1)
                            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                            .enchant(Enchantment.DURABILITY, 3)
                            .build();
                    ItemStack buty = ItemBuilder.from(Material.DIAMOND_BOOTS).amount(1)
                            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                            .enchant(Enchantment.DURABILITY, 3)
                            .build();
                    ItemStack miecz = ItemBuilder.from(Material.DIAMOND_SWORD).amount(1)
                            .enchant(Enchantment.DAMAGE_ALL, 5)
                            .enchant(Enchantment.DURABILITY, 3)
                            .enchant(Enchantment.FIRE_ASPECT, 2)
                            .build();
                    ItemStack refy = ItemBuilder.from(Material.GOLDEN_APPLE).amount(12)
                            .build();
                    ItemStack koxy = ItemBuilder.from(Material.ENCHANTED_GOLDEN_APPLE).amount(2)
                            .build();


                    p.getInventory().addItem(helm);
                    p.getInventory().addItem(klata);
                    p.getInventory().addItem(spodnie);
                    p.getInventory().addItem(buty);
                    p.getInventory().addItem(miecz);
                    p.getInventory().addItem(refy);
                    p.getInventory().addItem(koxy);

                });

        GuiItem zestaw_netherite = ItemBuilder.from(Material.NETHERITE_SWORD)
                .name(Component.text(GlobalHelper.fixColor("&7Zestaw &5Netherite")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Zestaw zawiera:")),
                        Component.text(GlobalHelper.fixColor(" &8- &fHełm netherite 4/3 x1")),
                        Component.text(GlobalHelper.fixColor(" &8- &fKlata netherite 4/3 x1")),
                        Component.text(GlobalHelper.fixColor(" &8- &fSpodnie netherite 4/3 x1")),
                        Component.text(GlobalHelper.fixColor(" &8- &fButy netherite 4/3 x1")),
                        Component.text(GlobalHelper.fixColor(" &8- &fMiecz netherite 5/2 x1")),
                        Component.text(GlobalHelper.fixColor(" &8- &fZłote jabłka x12")),
                        Component.text(GlobalHelper.fixColor(" &8- &fEnchantowane złote jabłka x2")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Cena: &a4 lvle")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem(event -> {

                    if (u.getUserLvl().getLvl() < 4) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz tyle lvla na koncie!"));
                        return;
                    }
                    u.getUserLvl().removeLvl(4);
                    p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie zakupiłeś przedmiot!"));
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zakupiłeś przedmiot!"));

                    ItemStack helm = ItemBuilder.from(Material.NETHERITE_HELMET).amount(1)
                            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                            .enchant(Enchantment.DURABILITY, 3)
                            .build();
                    ItemStack klata = ItemBuilder.from(Material.NETHERITE_CHESTPLATE).amount(1)
                            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                            .enchant(Enchantment.DURABILITY, 3)
                            .build();
                    ItemStack spodnie = ItemBuilder.from(Material.NETHERITE_LEGGINGS).amount(1)
                            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                            .enchant(Enchantment.DURABILITY, 3)
                            .build();
                    ItemStack buty = ItemBuilder.from(Material.NETHERITE_BOOTS).amount(1)
                            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                            .enchant(Enchantment.DURABILITY, 3)
                            .build();
                    ItemStack miecz = ItemBuilder.from(Material.NETHERITE_SWORD).amount(1)
                            .enchant(Enchantment.DAMAGE_ALL, 5)
                            .enchant(Enchantment.DURABILITY, 3)
                            .enchant(Enchantment.FIRE_ASPECT, 2)
                            .build();
                    ItemStack refy = ItemBuilder.from(Material.GOLDEN_APPLE).amount(12)
                            .build();
                    ItemStack koxy = ItemBuilder.from(Material.ENCHANTED_GOLDEN_APPLE).amount(2)
                            .build();

                    p.getInventory().addItem(helm);
                    p.getInventory().addItem(klata);
                    p.getInventory().addItem(spodnie);
                    p.getInventory().addItem(buty);
                    p.getInventory().addItem(miecz);
                    p.getInventory().addItem(refy);
                    p.getInventory().addItem(koxy);

                });

        GuiItem rzadka = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(Component.text(GlobalHelper.fixColor("&7Klucz do skrzyni &3Rzadkiej")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Klucza użyjesz na &f/spawn")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKlucz potrzebny do otwarcia skrzyni!")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&7Cena: &a5 lvli")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem(event -> {
                    if (u.getUserLvl().getLvl() < 5) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz tyle lvla na koncie!"));
                        return;
                    }
                    u.getUserLvl().removeLvl(5);
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
                        Component.text(GlobalHelper.fixColor("&7Cena: &a20 lvli")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem(event -> {
                    if (u.getUserLvl().getLvl() < 20) {
                        p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz tyle lvla na koncie!"));
                        return;
                    }
                    u.getUserLvl().removeLvl(20);
                    p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie zakupiłeś przedmiot!"));
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie zakupiłeś przedmiot!"));

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + p.getName() + " epicka 1");
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

        gui.setItem(10, zestaw_diamond);
        gui.setItem(11, zestaw_netherite);
        gui.setItem(12, rzadka);

//        gui.setItem(21, vpln);
//        gui.setItem(23, czas);

//        gui.setItem(14, vip);

//        gui.setItem(25, pety);

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

