package pl.zczb.tools.spigot.commands.addons;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import pl.zczb.helpers.GlobalHelper;


public class BaseInvalidUsageHandler implements InvalidUsageHandler<CommandSender> {
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = (CommandSender) invocation.sender();
        Schematic schematic = result.getSchematic();

        if (schematic.isOnlyFirst()) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF7D00&l!&8] &7Poprawne użycie: &#FFB874" + schematic.first()));

            return;
        }

        sender.sendMessage(GlobalHelper.fixColor("&8[&#FF7D00&l!&8] &7Poprawne użycie:"));

        for (String scheme : schematic.all())
            sender.sendMessage(GlobalHelper.fixColor("&8- &#FFB874" + scheme));
    }
}


