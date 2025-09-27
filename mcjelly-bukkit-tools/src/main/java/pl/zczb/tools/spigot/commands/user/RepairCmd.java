package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;

import java.util.EnumSet;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.helpers.RepairHelper;


@Command(name = "repair", aliases = {"napraw"})
public class RepairCmd {
    private static final EnumSet<Material> UNREPAIRABLE_MATERIALS = EnumSet.of(Material.AIR, Material.GOLDEN_APPLE, Material.FISHING_ROD);


    @Execute
    public void repairSingleItem(@Context Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().isBlock() || UNREPAIRABLE_MATERIALS.contains(item.getType())) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTego przedmiotu nie możesz naprawić!"));

            return;
        }
        if (item.getDurability() == 0) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTen przedmiot nie wymaga naprawy."));

            return;
        }
        item.setDurability((short) 0);
        player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Naprawiłeś przedmiot w swojej ręce!"));
    }

    @Execute(name = "all")
    @Permission({"zczb.svip"})
    public void repairAllItems(@Context Player player) {
        int fixedItemsCount = RepairHelper.repairAll(player);

        if (fixedItemsCount > 0) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Naprawiono " + fixedItemsCount + " przedmiotów w ekwipunku!"));
        } else {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FŻaden z twoich przedmiotów nie wymagał naprawy."));
        }
    }
}


