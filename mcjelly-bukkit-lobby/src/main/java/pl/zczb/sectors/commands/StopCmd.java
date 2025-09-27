package pl.zczb.sectors.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import pl.zczb.sectors.managers.SectorManager;


@Command(name = "stop")
public class StopCmd {
    @Execute
    @Permission({"zczb.root"})
    public void onChannel(@Context CommandSender sender) {
        SectorManager.stopSectorPanic();
    }
}


