package pl.zczb.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import pl.zczb.redis.codec.RedisJsonCodec;
import pl.zczb.redis.packet.Packet;
import pl.zczb.redis.subscriber.RedisSubscriber;

import java.time.Duration;

public final class Redis {

    private final RedisClient redisClient;
    private final StatefulRedisPubSubConnection<String, Packet> redisPubSubConnection;
    private final StatefulRedisConnection<String, Packet> redisConnection;
    private final RedisURI redisURI;

    public Redis(RedisCredential credential) {
        this.redisURI = RedisURI.builder()
                .withHost(credential.getHostname())
                .withPort(credential.getPort())
                .withTimeout(Duration.ofSeconds(10))
                .build();

        this.redisClient = RedisClient.create(redisURI);


        this.redisPubSubConnection = redisClient.connectPubSub(new RedisJsonCodec());

        this.redisConnection = redisClient.connect(new RedisJsonCodec());
    }


    public void publish(String channel, Packet packet) {
        redisConnection.sync().publish(channel, packet);
    }

    public <T extends Packet> void subscribe(String channel, RedisSubscriber<T> subscriber) {
        redisPubSubConnection.sync().subscribe(channel);
        redisPubSubConnection.addListener(subscriber);
    }

    public StatefulRedisConnection<String, String> getStringConnection() {
        return redisClient.connect(StringCodec.UTF8);
    }


    public void close() {
        redisPubSubConnection.close();
        redisConnection.close();
        redisClient.shutdown();
    }
}
