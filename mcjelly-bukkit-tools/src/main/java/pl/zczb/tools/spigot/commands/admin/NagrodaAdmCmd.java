package pl.zczb.tools.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.NagrodaAdmPacket;
import pl.zczb.redis.packet.Packet;
import pl.zczb.tools.database.user.models.UserDataModel;

@Command(name = "nadajdc")
@Permission({"zczb.root"})
public class NagrodaAdmCmd {
    @Execute
    public void giveReward(@Context CommandSender sender, @Arg("gracz") UserDataModel targetUser) {
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new NagrodaAdmPacket(targetUser.getNick()));
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie nadano nagrodę graczowi &a" + targetUser.getNick() + "&6."));
    }
}


