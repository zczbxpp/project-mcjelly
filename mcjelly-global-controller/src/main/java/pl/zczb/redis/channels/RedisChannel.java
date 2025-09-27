package pl.zczb.redis.channels;


import io.lettuce.core.api.sync.RedisCommands;
import pl.zczb.redis.Redis;

import java.util.Set;
import java.util.stream.Collectors;

public class RedisChannel {

    public static final RedisChannel INSTANCE = new RedisChannel();

    private RedisCommands<String, String> syncCommands;

    public void setupChannels(Redis redis) {
        this.syncCommands = redis.getStringConnection().sync();
    }

    public void addOnlinePlayer(String playerName) {
        syncCommands.sadd("ONLINE_PLAYERS", playerName);

    }

    public void removeOnlinePlayer(String playerName) {
        syncCommands.srem("ONLINE_PLAYERS", playerName);
    }

    public Set<String> getOnlinePlayers() {
        return syncCommands.smembers("ONLINE_PLAYERS");
    }

    public void clearOnlinePlayers() {
        syncCommands.del("ONLINE_PLAYERS");
    }



    public void addOnlineWartaCash(Integer numberOfPlayers) {
        syncCommands.incrby("ONLINE_WARTA_CASH", numberOfPlayers);
    }

    public void removeOnlineWartaCash(Integer numberOfPlayers) {
        syncCommands.incrby("ONLINE_WARTA_CASH", -numberOfPlayers);
    }


    public int getOnlineWartaCash() {
        String playersCount = syncCommands.get("ONLINE_WARTA_CASH");
        return playersCount != null ? Integer.parseInt(playersCount) : 0;
    }

    public void setOnlineWartaCash(Integer numberOfPlayers) {
        syncCommands.set("ONLINE_WARTA_CASH", String.valueOf(numberOfPlayers));
    }

    public void clearOnlineWartaCash() {
        syncCommands.del("ONLINE_WARTA_CASH");
    }

    private RedisChannel() { }
}
