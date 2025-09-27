package pl.zczb.tools.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.KickPacket;
import pl.zczb.redis.packet.Packet;
import pl.zczb.tools.database.user.models.UserDataModel;

@Command(name = "kick")
@Permission({"zczb.helper"})
public class KickCmd {
    @Execute
    public void kick(@Context Player sender, @Arg("gracz") UserDataModel targetUser, @Join("wiadomosc") String reason) {
        if (targetUser.getNick().equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie mozesz wyrzucic samego siebie!"));

            return;
        }
        String kickMessage = "&cZostales wyrzucony z serwera\n&cPowod: &4" + reason;

        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new KickPacket(targetUser.getNick(), kickMessage));

        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie wyrzucono gracza &c" + targetUser.getNick() + " &7z powodem &e" + reason + "&7."));
    }
}


