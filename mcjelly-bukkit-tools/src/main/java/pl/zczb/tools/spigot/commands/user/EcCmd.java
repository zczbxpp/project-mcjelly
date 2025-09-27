package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.tools.objects.impl.EnderChestsManager;


@Command(name = "ec", aliases = {"enderchest"})
public class EcCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;
        EnderChestsManager.openEnderGui(p);
    }
}


