package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.HelpopPacket;
import pl.zczb.redis.packet.Packet;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.util.HashMap;
import java.util.UUID;

@Command(name = "helpop", aliases = {"zglos"})
public class HelpopCmd {
    private static final HashMap<UUID, Long> times = new HashMap<>();

    @Execute
    public void onChannel(@Context CommandSender sender, @Join("wiadomosc") String wiadomosc) {
        Player p = (Player) sender;
        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);
        Long t = times.get(p.getUniqueId());
        if (t != null && System.currentTimeMillis() - t.longValue() < 10000L) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FWiadomosc na helpop mozesz wysylac co 10 sekund!"));

            return;
        }
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new HelpopPacket(Tools.getSectorConfig().getCurrentSector().getSectorName(), p.getName(), wiadomosc));
        times.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
        p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Twoja wiadomosc zostala wyslana!"));
    }
}


