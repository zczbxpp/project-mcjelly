package pl.zczb.tools.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@Command(name = "invsee")
@Permission({"zczb.root"})
public class InvseeCmd {
    @Execute(name = "inventory")
    public void seeInventory(@Context Player sender, @Arg("gracz") Player target) {
        sender.openInventory((Inventory) target.getInventory());
    }

    @Execute(name = "enderchest")
    public void seeEnderChest(@Context Player sender, @Arg("gracz") Player target) {
        sender.openInventory(target.getEnderChest());
    }
}


