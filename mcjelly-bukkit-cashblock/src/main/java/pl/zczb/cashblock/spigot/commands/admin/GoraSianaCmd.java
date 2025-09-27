package pl.zczb.cashblock.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import pl.zczb.cashblock.spigot.events.gorasiana.GoraSianaManager;

import java.util.Random;


@Command(name = "gorasiana")
@Permission({"zczb.root"})
public class GoraSianaCmd {
    private final GoraSianaManager goraSianaManager;

    public GoraSianaCmd(GoraSianaManager goraSianaManager) {
        this.goraSianaManager = goraSianaManager;
    }

    @Execute
    public void onSpeed(@Context CommandSender sender) {
        Random random = new Random();

        this.goraSianaManager.startEvent(random.nextInt(3));
    }
}


