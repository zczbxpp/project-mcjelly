package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@Command(name = "wb", aliases = {"crafting", "craft"})
public class WbCmd {
    @Execute
    @Permission({"zczb.vip"})
    public void openWorkbench(@Context CommandSender sender) {
        Player player = (Player) sender;
        player.openWorkbench(null, true);
    }
}


