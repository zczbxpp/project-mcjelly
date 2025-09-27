package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.database.user.models.UserDataModel;


@Command(name = "gamma")
public class GammaCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        Player p = (Player) sender;


        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);
        if (u.isGamma()) {
            u.setGamma(false);
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie wyłączono gamme!"));
            return;
        }
        u.setGamma(true);
        p.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(999999, 10));
        p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie włączono gamme!"));
    }
}


