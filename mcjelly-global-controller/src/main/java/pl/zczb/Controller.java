package pl.zczb;

import lombok.Getter;
import pl.zczb.redis.Redis;
import pl.zczb.redis.RedisCredential;
import pl.zczb.redis.channels.RedisChannel;

public class Controller {
    @Getter
    private static Controller instance;
    @Getter
    private Redis redis;


    public Controller() {
        instance = this;
        this.redis = new Redis(new RedisCredential("localhost", 6379));

        if (redis.getStringConnection() == null) {
            System.err.println("Redis connection failed!");
        } else {
            System.out.println("Redis initialized!");
        }

        RedisChannel.INSTANCE.setupChannels(redis);
    }


    public void shutdown() {
        if (redis != null) {
            redis.close();
        }
    }
}
