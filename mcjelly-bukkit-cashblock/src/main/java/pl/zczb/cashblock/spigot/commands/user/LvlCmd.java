package pl.zczb.cashblock.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.helpers.GlobalHelper;


@Command(name = "lvl", aliases = {"level", "poziom"})
public class LvlCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);
        p.sendMessage(GlobalHelper.fixColor(" "));
        p.sendMessage(GlobalHelper.fixColor("&6Informacja na temat poziomu:"));
        p.sendMessage(GlobalHelper.fixColor(" &7Poziom: &f" + u.getUserLvl().getLvl()));
        p.sendMessage(GlobalHelper.fixColor(" &7Exp: &f" + Math.round(u.getUserLvl().getXp()) + "/" + Math.round(u.getUserLvl().getXpToLvl())));
        p.sendMessage(GlobalHelper.fixColor(" &7Postęp: &f" + Math.round(u.getUserLvl().getXp() / u.getUserLvl().getXpToLvl() * 100.0D) + "%"));
        p.sendMessage(GlobalHelper.fixColor(""));
    }
}


