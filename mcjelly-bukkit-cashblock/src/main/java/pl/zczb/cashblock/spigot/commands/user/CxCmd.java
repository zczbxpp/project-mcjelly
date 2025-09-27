package pl.zczb.cashblock.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.helpers.GlobalHelper;

import java.util.Arrays;
import java.util.Set;


@Command(name = "cx", aliases = {"cobblex"})
public class CxCmd {
    private static final Set<Material> TARGET_MATERIALS = Set.of(Material.COBBLESTONE, Material.COBBLED_DEEPSLATE);

    private static final int REQUIRED_AMOUNT = 576;

    @Execute
    public void onCommand(@Context Player player) {
        PlayerInventory playerInventory = player.getInventory();


        int totalCount = Arrays.<ItemStack>stream(playerInventory.getContents()).filter(item -> (item != null && TARGET_MATERIALS.contains(item.getType()))).mapToInt(ItemStack::getAmount).sum();

        if (totalCount < 576) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FPotrzebujesz 9 stacków kamienia (Cobblestone/Deepslate), aby użyć tej komendy."));

            return;
        }
        int toRemove = 576;
        for (int i = 0; i < playerInventory.getSize(); i++) {
            ItemStack item = playerInventory.getItem(i);

            if (item != null && TARGET_MATERIALS.contains(item.getType())) {
                int amount = item.getAmount();
                if (amount <= toRemove) {
                    playerInventory.clear(i);
                    toRemove -= amount;
                } else {
                    item.setAmount(amount - toRemove);

                    break;
                }
            }
        }
        player.updateInventory();

        if (OtherHelper.getChance(20.0D)) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Udało ci się trafić klucz!"));
            Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "getcase give " + player.getName() + " zwykla 1");
        } else {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie udało ci się trafić klucza"));
        }
    }
}


