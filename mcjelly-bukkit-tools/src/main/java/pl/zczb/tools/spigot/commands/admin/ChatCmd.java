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
import pl.zczb.packets.ChatManagePacket;
import pl.zczb.redis.packet.Packet;


@Command(name = "chat")
@Permission({"zczb.root"})
public class ChatCmd {
    @Execute(name = "clear")
    public void clearChat(@Context CommandSender sender) {
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Wyczyszczono chat globalny."));
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new ChatManagePacket(sender.getName(), "clear", false));
    }


    @Execute(name = "global")
    public void toggleGlobalChat(@Context CommandSender sender, @Arg("true/false") boolean enabled) {
        String statusMessage = enabled ? "&8[&#20EA00&l!&8] &#6FFF58Włączono chat globalny." : "&8[&#FF0000&l!&8] &#FF3F3FWyłączono chat globalny.";

        sender.sendMessage(GlobalHelper.fixColor(statusMessage));
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new ChatManagePacket(sender.getName(), "global", enabled));
    }


    @Execute(name = "premium")
    public void togglePremiumChat(@Context CommandSender sender, @Arg("true/false") boolean enabled) {
        String statusMessage = enabled ? "&8[&#20EA00&l!&8] &#6FFF58Włączono chat premium." : "&8[&#FF0000&l!&8] &#FF3F3FWyłączono chat premium.";

        sender.sendMessage(GlobalHelper.fixColor(statusMessage));
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new ChatManagePacket(sender.getName(), "premium", enabled));
    }
}


