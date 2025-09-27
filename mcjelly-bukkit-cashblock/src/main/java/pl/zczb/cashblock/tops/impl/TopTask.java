package pl.zczb.cashblock.tops.impl;

import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Cashblock;

public class TopTask extends BukkitRunnable {
    public void run() {
        Cashblock.getInstance().getTopManager().refreshTops();
    }
}


