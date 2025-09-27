package pl.zczb.tools.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;


@Command(name = "fly")
public class FlyCmd {
    @Execute
    @Permission({"zczb.fly"})
    public void onSpeed(@Context CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(GlobalHelper.fixColor("&cTylko gracze moga uzywac tej komendy!"));
            return;
        }
        Player player = (Player) sender;
        if (Tools.getSectorConfig().getCurrentSector().getSectorName().endsWith("_event")) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNa kanale eventowym nie można używać fly'a"));
            return;
        }
        boolean flyEnabled = !player.getAllowFlight();
        player.setAllowFlight(flyEnabled);

        player.sendMessage(GlobalHelper.fixColor(flyEnabled ? "&8[&#20EA00&l!&8] &#6FFF58Fly został włączony!" : "&8[&#20EA00&l!&8] &#6FFF58Fly został wyłączony!"));
    }
}


