package pl.zczb.redis.subscriber;
import io.lettuce.core.pubsub.RedisPubSubListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.zczb.redis.packet.Packet;

@RequiredArgsConstructor
public abstract class RedisSubscriber<T extends Packet> implements RedisPubSubListener<String, Packet> {

    @Getter private final Class<T> packetType;
    @Getter private final String atChannel;

    @Override
    public void message(String channel, Packet packet) {
        if (!atChannel.equals(channel)) return;
        if (!packetType.isAssignableFrom(packet.getClass())) return;
        onPacketReceived((T) packet);
    }

    @Override
    public void message(String pattern, String channel, Packet packet) {}
    @Override
    public void subscribed(String channel, long count) {}
    @Override
    public void psubscribed(String pattern, long count) {}
    @Override
    public void unsubscribed(String channel, long count) {}
    @Override
    public void punsubscribed(String pattern, long count) {}

    public abstract void onPacketReceived(T packet);
}
