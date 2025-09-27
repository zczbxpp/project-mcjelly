package pl.zczb.tools.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.helpers.EnchantHelper;

import java.util.Optional;


@Command(name = "enchant")
@Permission({"zczb.root"})
public class EnchantCmd {
    @Execute
    public void enchantCommand(@Context Player sender, @Arg("enchant") String enchantment, @Arg("level") Optional<Integer> optionalLevel) {
        ItemStack item = sender.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FMusisz trzymać jakiś przedmiot w ręce!"));

            return;
        }

        Enchantment enchantment1 = EnchantHelper.getEnchantment(enchantment);


        if (enchantment1 !=null) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie ma takiego enchantu!"));

            return;
        }
        int level = ((Integer) optionalLevel.orElse(Integer.valueOf(enchantment1.getMaxLevel()))).intValue();

        item.addUnsafeEnchantment(enchantment1, level);
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Zaklęcie zostało dodane do przedmiotu w twojej ręce."));
    }
}


