package pl.zczb.tools.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.helpers.GlobalHelper;


@Command(name = "feed")
public class FeedCmd {
    @Execute
    @Permission({"zczb.vip"})
    public void onSpeed(@Context CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(GlobalHelper.fixColor("&cTylko gracze moga uzywac tej komendy!"));

            return;
        }

        Player player = (Player) sender;
        player.setFoodLevel(20);

        player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Twój poziom głodu został uzupełniony!"));
    }
}


