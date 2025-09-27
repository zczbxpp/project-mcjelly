package pl.zczb.sectors.tasks;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.redis.channels.RedisChannel;
import pl.zczb.redis.packet.Packet;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.managers.data.Sector;
import pl.zczb.sectors.packets.SectorStatsPacket;

import java.util.Set;

public class SectorUpdateTask implements Runnable {
    public static int warta_amount = 0;
    public static int global_amount = 0;

    public static Set<String> players;

    public void run() {
        int playerCount = Bukkit.getOnlinePlayers().size();
        double tps = (MinecraftServer.getServer()).recentTps[0];

        long now = System.currentTimeMillis();

        for (Sector sector : SectorManager.getAllSectors().values()) {
            if (now - sector.getLastUpdate() > 10000L) {
                sector.setOnline(false);
                sector.setTps(0.0D);
                sector.setPlayerCount(0);
                sector.setAdminMode(false);
            }
        }


        warta_amount = RedisChannel.INSTANCE.getOnlineWartaCash();
        global_amount = RedisChannel.INSTANCE.getOnlinePlayers().size() + warta_amount;
        players = RedisChannel.INSTANCE.getOnlinePlayers();

        Controller.getInstance().getRedis().publish("CH|sector-stats", (Packet) new SectorStatsPacket(Tools.getSectorConfig().getCurrentSector().getSectorName(), playerCount, tps, System.currentTimeMillis(), Tools.getSectorConfig().isAdminMode(), Tools.getSectorConfig().getCurrentSector().getSectorType()));
    }
}


