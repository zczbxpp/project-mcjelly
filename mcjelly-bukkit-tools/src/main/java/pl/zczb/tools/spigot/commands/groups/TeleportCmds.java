package pl.zczb.tools.spigot.commands.groups;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.STeleportPacket;
import pl.zczb.packets.TpaAcceptAllPacket;
import pl.zczb.packets.TpaAcceptPacket;
import pl.zczb.packets.TpaRequestPacket;
import pl.zczb.redis.packet.Packet;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.util.HashSet;
import java.util.Set;


@RootCommand
public class TeleportCmds {
    @Execute(name = "tphere", aliases = {"stp"})
    @Permission({"zczb.helper"})
    public void tphere(@Context Player p, @Arg("gracz") UserDataModel user) {
        if (user.getNick().equals(p.getName())) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie mozesz teleportowac siebie do siebie!"));

            return;
        }
        if (user.getSector().equals(Tools.getSectorConfig().getCurrentSector().getSectorName())) {
            Player pe = Bukkit.getPlayer(user.getNick());

            pe.teleport((Entity) p);
            p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie przeteleportowano."));

            return;
        }
        Controller.getInstance().getRedis().publish(user.getSector(), (Packet) new STeleportPacket(p.getName(), user.getNick()));

        p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie przeteleportowano"));
    }


    @Execute(name = "tp")
    @Permission({"zczb.helper"})
    public void tp(@Context Player p, @Arg("gracz") UserDataModel user) {
        if (user.getNick().equals(p.getName())) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie mozesz teleportowac siebie do siebie!"));

            return;
        }

        if (user.getSector().equals(Tools.getSectorConfig().getCurrentSector().getSectorName())) {
            Player pe = Bukkit.getPlayer(user.getNick());


            p.teleport((Entity) pe);
            p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie przeteleportowano."));

            return;
        }
        SectorManager.teleportToPlayer(p, user);

        p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie przeteleportowano"));
    }

    @Execute(name = "tp")
    @Permission({"zczb.helper"})
    public void tpToCoords(@Context Player sender, @Arg("x") double x, @Arg("y") double y, @Arg("z") double z) {
        Location targetLocation = new Location(sender.getWorld(), x, y, z);
        sender.teleport(targetLocation);
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie przeteleportowano na podane koordynaty."));
    }


    @Execute(name = "tpa")
    public void tpa(@Context Player player, @Arg("gracz") UserDataModel user) {
        if (Tools.getSectorConfig().getCurrentSector().getSectorName().endsWith("_event")) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNa kanale eventowym nie możesz tego używać!"));

            return;
        }
        if (player.getDisplayName().equalsIgnoreCase(user.getNick())) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie mozesz wyslac prosby o teleportacje do samego siebie!"));

            return;
        }
        if (user.hasTpaRequestFrom(player.getName())) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTen gracz posiada juz prosble o teleportacje od Ciebie!"));

            return;
        }
        player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Wyslales prosbe o teleportacje do gracza &#20EA00" + user.getNick()));
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new TpaRequestPacket(user.getNick(), player.getName()));
    }


    @Execute(name = "tpaccept")
    public void tpaccept(@Context Player p, @Arg("gracz") String nick) {
        if (Tools.getSectorConfig().getCurrentSector().getSectorName().endsWith("_event")) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNa kanale eventowym nie możesz tego używać!"));
            return;
        }
        UserDataModel user = Tools.getInstance().getUserHandler().getPlayer(p);


        if (nick.equalsIgnoreCase("*")) {
            if (user.getTpaRequests().size() <= 0L) {
                GlobalHelper.fixColor(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz aktywnych prosb o teleportacje!"));
                return;
            }
            Set<String> list = new HashSet<>();
            user.getTpaRequests().asMap().forEach((name, time) -> list.add(name));

            Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new TpaAcceptAllPacket(user.getNick(), list));

            GlobalHelper.fixColor(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Zaakceptowales " + user.getTpaRequests().size() + " prosb o teleportacje."));

            return;
        }
        if (!user.hasTpaRequestFrom(nick)) {
            GlobalHelper.fixColor(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie masz aktywnych prosb o teleportacje od &#FF0000" + nick + "&#FF3F3F!"));

            return;
        }
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new TpaAcceptPacket(user.getNick(), nick));

        GlobalHelper.fixColor(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Zaakceptowales prosbe o teleportacje."));
    }
}


