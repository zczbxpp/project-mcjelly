package pl.zczb.cashblock.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import pl.zczb.Cashblock;
import pl.zczb.Controller;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.DataUtil;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.TurboKasaPacket;
import pl.zczb.packets.TurboKasaUserPacket;
import pl.zczb.redis.packet.Packet;

import java.util.HashMap;
import java.util.Map;

@Command(name = "turbokasa")
public class TurboKasaCmd {
    public static BossBar bossBar = Bukkit.createBossBar(GlobalHelper.fixColor("&#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(serwer) &fbedzie trwać jeszcze przez &#FFDB0E0s"), BarColor.RED, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);
    public static Map<String, BossBar> bossbars = new HashMap<>();

    @Execute
    @Permission({"zczb.root"})
    public void executeAll(@Context CommandSender sender, @Arg("czas") String time) {
        long eventtime = DataUtil.parseDateDiff(time, true);
        Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new TurboKasaPacket(eventtime, sender.getName()));
    }

    @Execute(name = "gracz")
    @Permission({"zczb.root"})
    public void executeGracz(@Context CommandSender sender, @Arg("gracz") String nick, @Arg("czas") String time) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(nick);
        if (u == null) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie znaleziono gracza z takim nickiem"));

            return;
        }
        long eventtime = DataUtil.parseDateDiff(time, true);
        Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new TurboKasaUserPacket(eventtime, u.getNick()));
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Dano graczowi usluge TurboKase x2"));
    }
}


