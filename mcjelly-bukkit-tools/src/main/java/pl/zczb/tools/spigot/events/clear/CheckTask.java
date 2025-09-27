package pl.zczb.tools.spigot.events.clear;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.tools.database.user.models.UserDataModel;

public class CheckTask extends BukkitRunnable {
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(player);
            if (u.isSprawdzany())
                player.sendTitle(GlobalHelper.fixColor("&2Jesteś sprawdzany!"), GlobalHelper.fixColor("&aWejdż na discord lub &2/przyznajsie"), 0, 60, 5);
        }
    }
}


