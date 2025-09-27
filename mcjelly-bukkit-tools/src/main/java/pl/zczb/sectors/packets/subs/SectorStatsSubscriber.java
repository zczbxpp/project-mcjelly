package pl.zczb.sectors.packets.subs;

import pl.zczb.redis.subscriber.RedisSubscriber;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.managers.data.Sector;
import pl.zczb.sectors.packets.SectorStatsPacket;

public final class SectorStatsSubscriber extends RedisSubscriber<SectorStatsPacket> {
    public SectorStatsSubscriber() {
        super(SectorStatsPacket.class, "CH|sector-stats");
    }


    public void onPacketReceived(SectorStatsPacket packet) {
        Sector sector = SectorManager.getSector(packet.getSectorName());

        if (sector != null) {
            sector.setTps(packet.getTps());
            sector.setPlayerCount(packet.getPlayerCount());
            sector.setLastUpdate(packet.getLastUpdate());
            sector.setSectorType(packet.getSectorType());
            sector.setOnline(true);
            sector.setAdminMode(packet.isAdminMode());
        }
    }
}


