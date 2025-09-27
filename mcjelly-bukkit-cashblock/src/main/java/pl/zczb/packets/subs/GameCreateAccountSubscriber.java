package pl.zczb.packets.subs;

import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.packets.GameCreateAccountPacket;
import pl.zczb.redis.subscriber.RedisSubscriber;

import java.util.UUID;

public class GameCreateAccountSubscriber extends RedisSubscriber<GameCreateAccountPacket> {
    public GameCreateAccountSubscriber() {
        super(GameCreateAccountPacket.class, "CH|cashblock_tryb");
    }


    public void onPacketReceived(GameCreateAccountPacket packet) {
        if (packet.getSectorName().equals(Cashblock.getCashblockConfig().getSector_name())) {
            return;
        }
        Cashblock.getInstance().getUserHandler().cacheUser(UUID.fromString(packet.getUuid()), UserDataModel.deserialize(packet.getUserData()));
    }
}


