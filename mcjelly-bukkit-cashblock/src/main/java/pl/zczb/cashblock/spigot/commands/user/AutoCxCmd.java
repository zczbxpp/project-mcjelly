package pl.zczb.cashblock.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.helpers.GlobalHelper;


@Command(name = "autocx")
public class AutoCxCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);

        u.setAutoCx(!u.isAutoCx());

        p.sendMessage(GlobalHelper.fixColor("&8[&6&l!&8] &aPomyślnie " + (u.isAutoCx() ? "włączyłeś" : "wyłączyłeś autocx")));
    }
}


