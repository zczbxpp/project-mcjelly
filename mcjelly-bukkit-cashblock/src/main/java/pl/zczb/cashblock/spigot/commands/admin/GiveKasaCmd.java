package pl.zczb.cashblock.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Cashblock;
import pl.zczb.Controller;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.ManageKasaPacket;
import pl.zczb.redis.packet.Packet;

@Command(name = "givepln")
public class GiveKasaCmd {
    @Execute
    @Permission({"zczb.root"})
    public void onDefault(@Context CommandSender sender) {
        sender.sendMessage(GlobalHelper.fixColor("&8[&#FF7D00&l!&8] &7Poprawne użycie: &#FFB874/givepln <set/add/remove> <nick> <kwota>"));
    }

    @Execute
    @Permission({"zczb.root"})
    public void setKasa(@Context Player sender, @Arg("gracz") String targetName, @Arg("ilosc") double amount) {
        modifyKasa(sender, targetName, amount, "set");
    }

    @Execute(name = "add")
    @Permission({"zczb.root"})
    public void addKasa(@Context Player sender, @Arg("gracz") String targetName, @Arg("ilosc") double amount) {
        modifyKasa(sender, targetName, amount, "add");
    }

    @Execute(name = "remove")
    @Permission({"zczb.root"})
    public void removeKasa(@Context Player sender, @Arg("gracz") String targetName, @Arg("ilosc") double amount) {
        modifyKasa(sender, targetName, amount, "remove");
    }

    private void modifyKasa(Player sender, String targetName, double amount, String action) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(targetName);
        if (u == null) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&5&l!&8] &7Gracz jest offline!"));

            return;
        }
        if (amount < 0.0D) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&5&l!&8] &7Kwota nie może być ujemna!"));

            return;
        }
        switch (action) {
            case "set":
                Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new ManageKasaPacket(targetName, "set", amount));
                sender.sendMessage(GlobalHelper.fixColor("&8[&5&l!&8] &7Ustawiono saldo gracza &d" + targetName + " &7na &a" + amount + " PLN"));
                break;
            case "add":
                Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new ManageKasaPacket(targetName, "add", amount));
                sender.sendMessage(GlobalHelper.fixColor("&8[&5&l!&8] &7Dodano &a" + amount + " PLN &7dla gracza &d" + targetName));
                break;
            case "remove":
                if (u.getPln() < amount) {
                    sender.sendMessage(GlobalHelper.fixColor("&8[&5&l!&8] &7Gracz nie ma wystarczająco pieniędzy!"));
                    return;
                }
                Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new ManageKasaPacket(targetName, "remove", amount));
                sender.sendMessage(GlobalHelper.fixColor("&8[&5&l!&8] &7Odjęto &c" + amount + " PLN &7od gracza &d" + targetName));
                break;
        }
    }
}


