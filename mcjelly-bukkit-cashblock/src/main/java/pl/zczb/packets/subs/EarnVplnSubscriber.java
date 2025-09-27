package pl.zczb.packets.subs;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Cashblock;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.EarnVplnPacket;
import pl.zczb.redis.subscriber.RedisSubscriber;

import java.util.LinkedList;
import java.util.Queue;

public class EarnVplnSubscriber extends RedisSubscriber<EarnVplnPacket> {

    public EarnVplnSubscriber() {
        super(EarnVplnPacket.class, "CH|cashblock_tryb");
    }

    private static final Queue<EarnVplnPacket> bossBarQueue = new LinkedList<>();
    private static BossBar activeBossBar = null;
    private static BukkitRunnable bossBarTask = null;

    private static final double DISPLAY_TIME_SECONDS = 2.0;
    private static final int TICKS_PER_SECOND = 20;
    private static final int TICK_INTERVAL = 5;
    private static final int TOTAL_TICKS = (int) (DISPLAY_TIME_SECONDS * TICKS_PER_SECOND / TICK_INTERVAL);

    @Override
    public void onPacketReceived(EarnVplnPacket packet) {
        if (activeBossBar != null) {
            bossBarQueue.offer(packet);
            return;
        }

        showBossBar(packet);
    }

    private void showBossBar(EarnVplnPacket packet) {
        String title = GlobalHelper.fixColor("\uE822 &fGracz &a" + packet.getPlayerName() + " &fzarobił &2" + packet.getReward() + " vPLN &f\uE822");

        activeBossBar = Bukkit.createBossBar(title, BarColor.GREEN, BarStyle.SOLID);
        for (Player player : Bukkit.getOnlinePlayers()) {
            activeBossBar.addPlayer(player);
        }

        bossBarTask = new BukkitRunnable() {
            int ticksRemaining = TOTAL_TICKS;

            @Override
            public void run() {
                if (ticksRemaining > 0) {
                    double progress = (double) ticksRemaining / TOTAL_TICKS;
                    activeBossBar.setProgress(progress);
                    ticksRemaining--;
                } else {
                    activeBossBar.removeAll();
                    activeBossBar = null;
                    this.cancel();
                    if (!bossBarQueue.isEmpty()) {
                        EarnVplnPacket next = bossBarQueue.poll();
                        showBossBar(next);
                    }
                }
            }
        };

        bossBarTask.runTaskTimer(Cashblock.getInstance(), 0, TICK_INTERVAL);
    }
}
