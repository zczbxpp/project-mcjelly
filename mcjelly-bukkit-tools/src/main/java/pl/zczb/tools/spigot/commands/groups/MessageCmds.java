package pl.zczb.tools.spigot.commands.groups;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.MsgPacket;
import pl.zczb.redis.packet.Packet;
import pl.zczb.tools.database.user.models.UserDataModel;

@RootCommand
public class MessageCmds {
    @Execute(name = "msg", aliases = {"tell", "w"})
    @Permission("zczb.gracz")
    public void msg(@Context Player sender, @Arg("gracz") UserDataModel targetUser, @Join("wiadomosc") String message) {
        if (sender.getName().equalsIgnoreCase(targetUser.getNick())) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz napisać sam do siebie."));

            return;
        }
        UserDataModel senderUser = Tools.getInstance().getUserHandler().getPlayer(sender);

        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new MsgPacket(sender.getName(), targetUser.getNick(), message));
        sender.sendMessage(GlobalHelper.fixColor(" &#FFB874Ty &8→ &7" + targetUser.getNick() + "&8: &f" + message));

        senderUser.setLastConverser(targetUser.getNick());
        targetUser.setLastConverser(senderUser.getNick());
    }

    @Execute(name = "reply", aliases = {"r"})
    @Permission("zczb.gracz")
    public void reply(@Context Player sender, @Join("wiadomosc") String message) {
        UserDataModel senderUser = Tools.getInstance().getUserHandler().getPlayer(sender);
        if (senderUser.getLastConverser() == null || senderUser.getLastConverser().isEmpty()) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz komu odpowiedzieć."));

            return;
        }
        UserDataModel otherUser = Tools.getInstance().getUserHandler().getPlayer(senderUser.getLastConverser());
        if (otherUser == null) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FOsoba, której próbujesz odpowiedzieć, jest offline."));

            return;
        }
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new MsgPacket(sender.getName(), otherUser.getNick(), message));
        sender.sendMessage(GlobalHelper.fixColor(" &#FFB874Ty &8→ &7" + otherUser.getNick() + "&8: &f" + message));
    }

    @Execute(name = "socialspy", aliases = {"spy"})
    @Permission("zczb.helper")
    public void socialspy(@Context Player sender) {
        UserDataModel user = Tools.getInstance().getUserHandler().getPlayer(sender);
        user.setSocialSpy(!user.isSocialSpy());


        String feedbackMessage = user.isSocialSpy() ? "&8[&#20EA00&l!&8] &#6FFF58Socialspy zostało włączone!" : "&8[&#FF0000&l!&8] &#FF3F3FSocialspy został wyłączony!";

        sender.sendMessage(GlobalHelper.fixColor(feedbackMessage));
    }
}


