package pl.zczb.sectors.packets.subs;

import pl.zczb.Tools;
import pl.zczb.redis.subscriber.RedisSubscriber;
import pl.zczb.sectors.packets.SectorJoinPacket;
import pl.zczb.tools.database.user.models.UserDataModel;

public final class SectorJoinSubscriber extends RedisSubscriber<SectorJoinPacket> {
    public SectorJoinSubscriber() {
        super(SectorJoinPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
    }


    public void onPacketReceived(SectorJoinPacket packet) {
        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(packet.getPlayerName());
        u.setSector(packet.getSectorName());
    }
}


