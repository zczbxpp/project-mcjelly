package pl.zczb.sectors.packets.subs;

import org.bukkit.Bukkit;
import pl.zczb.Tools;
import pl.zczb.redis.subscriber.RedisSubscriber;
import pl.zczb.sectors.packets.SectorCreateAccountPacket;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.util.UUID;

public final class SectorCreateAccountSubscriber extends RedisSubscriber<SectorCreateAccountPacket> {
    public SectorCreateAccountSubscriber() {
        super(SectorCreateAccountPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
    }


    public void onPacketReceived(SectorCreateAccountPacket packet) {
        if (packet.getSectorName().equals(Tools.getSectorConfig().getCurrentSector().getSectorName())) {
            return;
        }
        UserDataModel u = UserDataModel.deserialize(packet.getUserData());
        // u.setChangingSector(false);
        Tools.getInstance().getUserHandler().cacheUser(UUID.fromString(packet.getUuid()), u);

    }
}


