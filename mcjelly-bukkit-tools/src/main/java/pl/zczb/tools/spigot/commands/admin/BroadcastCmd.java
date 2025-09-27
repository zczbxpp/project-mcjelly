package pl.zczb.tools.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.BroadcastPacket;
import pl.zczb.redis.packet.Packet;

@Command(name = "alert", aliases = {"broadcast", "bc"})
@Permission({"zczb.admin"})
public class BroadcastCmd {
    @Execute(name = "actionbar")
    public void broadcastActionbar(@Context CommandSender sender, @Join("wiadomosc") String message) {
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new BroadcastPacket("actionbar", message));
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Wysłano ogłoszenie na actionbar."));
    }

    @Execute(name = "subtitle")
    public void broadcastSubtitle(@Context CommandSender sender, @Join("wiadomosc") String message) {
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new BroadcastPacket("subtitle", message));
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Wysłano ogłoszenie jako subtitle."));
    }

    @Execute(name = "chat")
    public void broadcastChat(@Context CommandSender sender, @Join("wiadomosc") String message) {
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new BroadcastPacket("chat", message));
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Wysłano ogłoszenie na czacie."));
    }
}


