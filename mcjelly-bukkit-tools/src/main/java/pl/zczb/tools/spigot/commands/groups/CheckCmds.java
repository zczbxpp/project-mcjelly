package pl.zczb.tools.spigot.commands.groups;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.CheckPacket;
import pl.zczb.redis.packet.Packet;
import pl.zczb.tools.database.user.models.UserDataModel;

@RootCommand
public class CheckCmds {
    @Execute(name = "sprawdz")
    @Permission({"zczb.helper"})
    public void sprawdz(@Context Player sender, @Arg("gracz") UserDataModel targetUser) {
        if (targetUser.isSprawdzany()) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FGracz jest już sprawdzany!"));

            return;
        }
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new CheckPacket("sprawdz", targetUser.getNick(), ""));
    }

    @Execute(name = "cheater")
    @Permission({"zczb.helper"})
    public void cheater(@Context Player sender, @Arg("gracz") UserDataModel targetUser) {
        if (!targetUser.isSprawdzany()) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FGracz nie jest sprawdzany!"));

            return;
        }
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new CheckPacket("ban", targetUser.getNick(), sender.getName()));
    }

    @Execute(name = "czysty")
    @Permission({"zczb.helper"})
    public void czysty(@Context Player sender, @Arg("gracz") UserDataModel targetUser) {
        if (!targetUser.isSprawdzany()) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FGracz nie jest sprawdzany!"));

            return;
        }
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new CheckPacket("czysty", targetUser.getNick(), ""));
    }

    @Execute(name = "przyznajesie")
    public void przyznajesie(@Context CommandSender sender) {
        Player player = (Player) sender;

        UserDataModel user = Tools.getInstance().getUserHandler().getPlayer(player);
        if (!user.isSprawdzany()) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie jesteś sprawdzany!"));

            return;
        }
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new CheckPacket("przyznajsie", player.getName(), ""));
    }
}


