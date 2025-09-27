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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
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

@Command(name = "przecena")
@Permission({"zczb.root"})
public class ItemshopDiscountCmd {
    public static BossBar bossBar = Bukkit.createBossBar(pl.zczb.helpers.GlobalHelper.fixColor("&fAktualnie trwa promocja &#FFDE70&l-10% &fw &#FFDE70/itemshop"), BarColor.YELLOW, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);

    @Execute
    public void openShopGui(@Context Player p, @Arg("procent") int przecena) {

            double percentage = Double.parseDouble(String.valueOf(przecena));

            if (percentage < 0 || percentage > 100) {
                p.sendMessage("Procent musi być liczbą w zakresie od 0 do 100.");
                return;
            }


            Controller.getInstance().getRedis().publish("CH|itemshop", new ItemshopDiscountPacket(percentage));

            if (percentage == 0) {
                p.sendMessage(GlobalHelper.fixColor("&aPrzecena w itemshopie została wyłączona."));
            } else {
                p.sendMessage(GlobalHelper.fixColor("&aUstawiono globalną przecenę w itemshopie na " + percentage + "%!"));
            }

            p.sendMessage(GlobalHelper.fixColor("&aGracze muszą otworzyć sklep ponownie, aby zobaczyć nowe ceny."));

    }
}


