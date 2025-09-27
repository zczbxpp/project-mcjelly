package pl.zczb.tools.tops.impl;

import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Tools;

public class TopTask extends BukkitRunnable {
    public void run() {
        Tools.getInstance().getTopManager().refreshTops();
    }
}


