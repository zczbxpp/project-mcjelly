package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;


@Command(name = "kosz")
public class TrashCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;
        if (Tools.getSectorConfig().getCurrentSector().getSectorName().endsWith("_event")) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNa kanale eventowym nie możesz tego używać!"));


            return;
        }

        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Kosz")))
                .rows(6)
                .create();
        gui.open(p);
    }
}


