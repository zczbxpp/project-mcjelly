package pl.zczb.tools.spigot.commands.vip;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.zczb.helpers.GlobalHelper;

@Command(name = "hat")
@Permission({"zczb.vip"})
public class HatCmd {
    @Execute
    public void setHat(@Context Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() == Material.AIR) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FMusisz trzymac przedmiot, aby zalozyc go na glowe!"));

            return;
        }
        ItemStack currentHelmet = player.getInventory().getHelmet();

        player.getInventory().setHelmet(itemInHand);

        if (currentHelmet != null && currentHelmet.getType() != Material.AIR) {
            player.getInventory().setItemInMainHand(currentHelmet);
        } else {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }

        player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Założyłeś przedmiot na głowę!"));
    }
}


