package pl.zczb.lobby.queue;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import pl.zczb.helpers.GlobalHelper;

public class BossBarHandler {
    public BossBarHandler(Player player, QueueInstance queue) {
        this.bossBar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);
        this.bossBar.addPlayer(player);
        update(queue);
    }

    private final BossBar bossBar;

    public void update(QueueInstance queue) {
        int position = queue.getPosition(this.bossBar.getPlayers().get(0));
        int total = queue.getSize();

        double progress = Math.max(0.05D, 1.0D - (position - 1) / total);
        this.bossBar.setProgress(progress);
        this.bossBar.setTitle(GlobalHelper.fixColor("&#4EEF45✈ | &fKolejka do &#4EEF45" + queue.getName() + " &ftwoja pozycja &#4EEF45" + position + " &fz &#4EEF45" + total));
    }

    public void remove() {
        this.bossBar.removeAll();
    }
}


