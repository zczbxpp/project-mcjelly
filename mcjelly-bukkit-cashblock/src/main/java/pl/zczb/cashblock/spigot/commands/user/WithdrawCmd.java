package pl.zczb.cashblock.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.helpers.GlobalHelper;

import java.text.DecimalFormat;


@Command(name = "wyplata", aliases = {"wyplac"})
public class WithdrawCmd {
    @Execute
    public void executeInfo(@Context Player player) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(player);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double cash = 20.0D - u.getPln();

        if (cash < 0.0D) {
            cash = 0.0D;
        }

        player.sendMessage(GlobalHelper.fixColor(" "));
        player.sendMessage(GlobalHelper.fixColor(" &#FF7D00Informacja na temat twojego konta:"));
        player.sendMessage(GlobalHelper.fixColor(" &7Aktualnie posiadasz &#FF7D00" + decimalFormat.format(u.getPln()) + " &7vPLN"));
        player.sendMessage(GlobalHelper.fixColor(" &7Do wypłaty brakuje ci &#FF7D00" + decimalFormat.format(cash) + " &7vPLN"));
        player.sendMessage(GlobalHelper.fixColor(" &7Aby wypłacić wejdz na &#FFB874/discord"));
        player.sendMessage(GlobalHelper.fixColor(" &7Aby wypłacić na &#FFB874/portfel &7użyj &#FFB874/wyplac portfel"));
        player.sendMessage(GlobalHelper.fixColor(""));
    }

    @Execute(name = "portfel")
    public void executeWallet(@Context Player player) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(player);

        if (u.getPln() >= 20.0D) {
            u.removePln(20.0D);
            Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "is " + player.getName() + " 20 true");
            player.sendMessage(GlobalHelper.fixColor("&aPomyślnie zlecono wypłatę środków na portfel!"));
        } else {
            player.sendMessage(GlobalHelper.fixColor("&cNie posiadasz wystarczającej ilości środków! (Minimum: 20 vPLN)"));
        }
    }
}


