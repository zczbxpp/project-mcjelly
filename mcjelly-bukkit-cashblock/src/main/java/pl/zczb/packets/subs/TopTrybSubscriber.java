package pl.zczb.packets.subs;

import pl.zczb.Cashblock;
import pl.zczb.packets.TopTrybPacket;
import pl.zczb.redis.subscriber.RedisSubscriber;

public final class TopTrybSubscriber extends RedisSubscriber<TopTrybPacket> {
    public TopTrybSubscriber() {
        super(TopTrybPacket.class, "CH|cashblock_tryb");
    }

    public void onPacketReceived(TopTrybPacket packet) {
        Cashblock.getInstance().getTopManager().replaceTop(packet.getTopType(), packet.getTopList());
    }
}


