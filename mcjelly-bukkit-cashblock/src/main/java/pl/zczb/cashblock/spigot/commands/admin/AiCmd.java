package pl.zczb.cashblock.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.brush.BrushItem;
import pl.zczb.cashblock.brush.BrushPluginConfiguration;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.objects.impl.PetManager;
import pl.zczb.cashblock.objects.impl.PickaxeManager;
import pl.zczb.helpers.GlobalHelper;

import java.util.Arrays;
import java.util.List;

@Command(name = "ai", aliases = {"adminitems"})
@Permission({"zczb.root"})
public class AiCmd {
    private final BrushPluginConfiguration configuration;
    private final PetManager petManager;

    public AiCmd(BrushPluginConfiguration configuration, PetManager petManager) {
        this.configuration = configuration;
        this.petManager = petManager;
    }

    @Execute
    public void onAdminItems(@Context Player p) {

        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Itemy Admina")))
                .rows(3)
                .create();

        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);
        List<String> brushSizes = Arrays.asList("1x1", "3x3", "5x5", "7x7", "9x9");

        for (int i = 0; i < brushSizes.size(); i++) {
            BrushItem brush = configuration.findByName(brushSizes.get(i));
            if (brush != null) {
                ItemStack brushItemStack = brush.getItemStack().clone();
                GuiItem guiItem = ItemBuilder.from(brushItemStack).asGuiItem();
                gui.setItem(i, guiItem);
            }
        }

        GuiItem magicznykamien = ItemBuilder.from(Material.DRAGON_EGG)
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
                .asGuiItem();

        GuiItem glasses = ItemBuilder.from(Material.TURTLE_HELMET)
                .name(Component.text(GlobalHelper.fixColor("&b&lOkularki swagu")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Ten przedmiot zwieksza drop kasy o &b+x0.25")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&4UWAGA: &cMusisz zalożyć ten przedmiot")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem();

        GuiItem lornetka = ItemBuilder.from(Material.SPYGLASS)
                .name(Component.text(GlobalHelper.fixColor("&a&lLornetka")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Ten przedmiot zwieksza drop kasy o &a+x0.25")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&4UWAGA: &cMusisz trzymać ten przedmiot")),
                        Component.text(GlobalHelper.fixColor("&cw lewej ręce!")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem();

        GuiItem honeycomb = ItemBuilder.from(Material.HONEYCOMB)
                .name(Component.text(GlobalHelper.fixColor("&6&lPlaster Miodu")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Ten przedmiot zwieksza drop kasy o &6+x0.1")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&4UWAGA: &cMusisz trzymać ten przedmiot")),
                        Component.text(GlobalHelper.fixColor("&cw lewej ręce!")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .asGuiItem();


        ItemStack vipItem = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&7Voucher na &eVIP &8(&f7 dni&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to voucher ktory po użyciu")),
                        Component.text(GlobalHelper.fixColor(" &7nadaje rangę na &f7 dni")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                )
                .build();
        ItemMeta vipMeta = vipItem.getItemMeta();
        vipMeta.setCustomModelData(1001);
        vipItem.setItemMeta(vipMeta);


        GuiItem vip = new GuiItem(vipItem);

        ItemStack svipItem = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&7Voucher na &6SVIP &8(&f7 dni&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to voucher ktory po użyciu")),
                        Component.text(GlobalHelper.fixColor(" &7nadaje rangę na &f7 dni")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                )
                .build();
        ItemMeta svipMeta = svipItem.getItemMeta();
        svipMeta.setCustomModelData(1002);
        svipItem.setItemMeta(svipMeta);

        GuiItem svip = new GuiItem(svipItem);


        ItemStack mvipItem = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&7Voucher na &bMVIP &8(&f7 dni&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to voucher ktory po użyciu")),
                        Component.text(GlobalHelper.fixColor(" &7nadaje rangę na &f7 dni")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                )
                .build();
        ItemMeta mvipMeta = mvipItem.getItemMeta();
        mvipMeta.setCustomModelData(1003);
        mvipItem.setItemMeta(mvipMeta);

        GuiItem mvip = new GuiItem(mvipItem);


        GuiItem turbo15 = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&7Voucher na &bTurboKase &8(&f15 min&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to voucher ktory po użyciu")),
                        Component.text(GlobalHelper.fixColor(" &7nadaje turbokase")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                )
                .asGuiItem();

        GuiItem turbo30 = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&7Voucher na &bTurboKase &8(&f30 min&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to voucher ktory po użyciu")),
                        Component.text(GlobalHelper.fixColor(" &7nadaje turbokase")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                )
                .asGuiItem();

        GuiItem odlamek = ItemBuilder.from(Material.AMETHYST_SHARD)
                .name(Component.text(GlobalHelper.fixColor("&b&lOdłamek kosmosu")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to odłamek który możesz wpłacic &f/zbyszek")),
                        Component.text(GlobalHelper.fixColor(" &7oraz możesz ulepszyć nim kilof &aPPM &7trzymajac go!")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .glow()
                .asGuiItem();

        GuiItem materia = ItemBuilder.from(Material.AMETHYST_CLUSTER)
                .name(Component.text(GlobalHelper.fixColor("&5&lMateria kosmosu")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to materia potrzebna do zaklinania")),
                        Component.text(GlobalHelper.fixColor(" &7brushy u &dastronauty &7na spawnie!")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .glow()
                .asGuiItem();

        GuiItem fly = ItemBuilder.from(Material.PAPER)
                .name(Component.text(GlobalHelper.fixColor("&7Voucher na &fFly &8(&f3 dni&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to voucher ktory po użyciu")),
                        Component.text(GlobalHelper.fixColor(" &7nadaje możliwość latania na &f3 dni")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                )
                .asGuiItem();

        GuiItem poziom10 = ItemBuilder.from(Material.EXPERIENCE_BOTTLE)
                .name(Component.text(GlobalHelper.fixColor("&2&lSkompresowany poziom &8(&f10 poziomów&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Po użyciu na twoje konto zostanie dodany poziom")),
                        Component.text(GlobalHelper.fixColor(" &7ta skompresowana buteleczka doda ci &f10 poziomów")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                )
                .asGuiItem();

        GuiItem poziom20 = ItemBuilder.from(Material.EXPERIENCE_BOTTLE)
                .name(Component.text(GlobalHelper.fixColor("&2&lSkompresowany poziom &8(&f20 poziomów&8)")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Po użyciu na twoje konto zostanie dodany poziom")),
                        Component.text(GlobalHelper.fixColor(" &7ta skompresowana buteleczka doda ci &f20 poziomów")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&AKliknij PPM aby użyć"))
                )
                .asGuiItem();

        GuiItem sword1 = ItemBuilder.from(Material.NETHERITE_SWORD)
                .name(Component.text(GlobalHelper.fixColor("&4&lMiecz wojownika")))
                .enchant(Enchantment.DURABILITY,3)
                .enchant(Enchantment.DAMAGE_ALL, 6)
                .enchant(Enchantment.FIRE_ASPECT,3)
                .asGuiItem();

        gui.setItem(5, magicznykamien);
        gui.setItem(6, glasses);
        gui.setItem(7, lornetka);
        gui.setItem(8, honeycomb);
        gui.setItem(9, vip);
        gui.setItem(10, svip);
        gui.setItem(11, mvip);
        gui.setItem(12, petManager.getPet("apsik").getItem());
        gui.setItem(13, petManager.getPet("gburek").getItem());
        gui.setItem(14, petManager.getPet("wesolek").getItem());
        gui.setItem(15, petManager.getPet("gapcio").getItem());
        gui.setItem(16, petManager.getPet("aronek").getItem());
        gui.setItem(17, turbo15);
        gui.setItem(18, turbo30);
        gui.setItem(19, odlamek);
        gui.setItem(20, fly);
        gui.setItem(21, new GuiItem(PickaxeManager.createPickaxe(p)));
        gui.setItem(22, poziom10);
        gui.setItem(23, poziom20);
        gui.setItem(24, sword1);

        gui.open(p);
    }


}


