package pl.zczb.tools.spigot.events.clear;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;

public class AutoMessageTask
        extends BukkitRunnable {
    private int currentMessageIndex = 0;


    public void run() {
        if (!Tools.getSectorConfig().getAuto_message().isEmpty()) {

            String message = Tools.getSectorConfig().getAuto_message().get(this.currentMessageIndex);

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage(GlobalHelper.fixColor(message));
            }


            this.currentMessageIndex++;

            if (this.currentMessageIndex >= Tools.getSectorConfig().getAuto_message().size())
                this.currentMessageIndex = 0;
        }
    }
}


