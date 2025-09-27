package pl.zczb.tools.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.util.ArrayList;
import java.util.List;


@Command(name = "vanish", aliases = {"v"})
public class VanishCmd {
    @Generated
    public static List<Player> getVanished() {
        return vanished;
    }

    public static List<Player> vanished = new ArrayList<>();

    @Execute
    @Permission({"zczb.helper"})
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;
        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);
        if (getVanished().contains(p)) {
            getVanished().remove(p);
            u.setVanish(false);
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.hasPermission("zczb.helper"))
                    online.showPlayer(p);
            }
            p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().clone().add(0.0D, 1.5D, 0.0D), 120, 0.8D, 0.4D, 0.7D, 0.05D, new Particle.DustOptions(
                    Color.fromRGB(255, 102, 178), 1.0F));

            p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Wyłączono vanisha"));
        } else {

            getVanished().add(p);
            u.setVanish(true);
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.hasPermission("zczb.helper"))
                    online.hidePlayer(p);
            }
            p.getWorld().spawnParticle(Particle.REDSTONE, p
                    .getLocation().clone().add(0.0D, 1.5D, 0.0D), 120, 0.3D, 0.3D, 0.3D, 0.05D, new Particle.DustOptions(
                    Color.fromRGB(255, 0, 0), 1.0F));

            p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Włączono vanisha"));
        }
    }
}


