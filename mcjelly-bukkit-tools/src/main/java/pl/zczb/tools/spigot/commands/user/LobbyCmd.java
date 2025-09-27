package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Tools;
import pl.zczb.tools.database.user.models.UserDataModel;
import pl.zczb.tools.helpers.BungeeHelper;

import java.io.IOException;


@Command(name = "lobby")
public class LobbyCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) throws IOException {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Ta komenda jest dostępna tylko dla graczy.");

            return;
        }
        Player p = (Player) sender;


        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);

        u.getUserSynchro().leaveFromGame(p);

        BungeeHelper.sendToServer((Player) sender, "lobby_1");
    }
}


