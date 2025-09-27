package pl.zczb.sectors.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.sectors.managers.SectorManager;

@Command(name = "stop")
@Permission({"zczb.root"})
public class StopCmd {
    @Execute
    public void stopServer(@Context CommandSender sender, @Arg boolean isNormalStop) {
        if (isNormalStop) {
            sender.sendMessage(GlobalHelper.fixColor("&cRozpoczęto normalne zatrzymywanie sektora..."));
            SectorManager.stopSector();
        } else {
            sender.sendMessage(GlobalHelper.fixColor("&4&lRozpoczęto awaryjne zatrzymywanie sektora! (PANIC)"));
            SectorManager.stopSectorPanic();
        }
    }
}


