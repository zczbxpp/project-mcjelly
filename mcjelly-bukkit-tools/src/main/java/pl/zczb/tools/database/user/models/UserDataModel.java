package pl.zczb.tools.database.user.models;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import lombok.Data;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

@Data
public class UserDataModel {

    private static final Gson GSON = new Gson();

    private String uuid;
    private String nick;




    private String sector;
    private boolean isChangingSector;

    private final UserSynchro userSynchro;

    private String lastConverser;

    private boolean gamma;

    private boolean warta;
    private long wartaTime;

    private long parkourTime;

    private boolean nagroda;
    private boolean nagrodaVerified;

    private boolean vanish;

    private long playerTime;
    private long joinTime;

    private final UserKits userKits;
    private final UserBans userBans;
    private boolean socialSpy;


    private transient Cache<String, Long> tpaRequests;
    private boolean sprawdzany;
    private final UserEnderchests userEnderchests;
    public UserDataModel(String uuid, String name, UserSynchro userSynchro, boolean warta, long wartaTime, long parkourTime, boolean nagroda, boolean nagrodaVerified, long playerTime, UserKits userKits, UserBans userBans, UserEnderchests userEnderchests) {
        this.uuid = uuid;
        this.nick = name;
        this.sector = "";

        this.isChangingSector = false;
        this.userSynchro = userSynchro;
        this.lastConverser = "";
        this.gamma = false;
        this.tpaRequests = CacheBuilder.newBuilder().expireAfterWrite(25L, TimeUnit.SECONDS).build();
        this.warta = warta;
        this.wartaTime = wartaTime;

        this.parkourTime = parkourTime;

        this.nagroda = nagroda;
        this.nagrodaVerified = nagrodaVerified;

        this.vanish = false;


        this.playerTime = playerTime;
        this.joinTime = 0;


        this.userKits = userKits;
        this.userBans = userBans;
        this.socialSpy = false;
        this.sprawdzany = false;

        this.userEnderchests = userEnderchests;
    }

    private long calculateSeconds() {
        if (joinTime == 0) return 0;
        return (System.currentTimeMillis() - joinTime) / 1000;
    }

    public long getActualPlayerTime() {
        return playerTime + calculateSeconds();
    }


    public long removePlayerTime(long time) {
        long currentTime = getPlayerTime();

        if (currentTime < time) {
            this.playerTime = 0;
            this.joinTime = 0;
            return currentTime;
        }

        this.playerTime = currentTime - time;
        this.joinTime = System.currentTimeMillis();
        return time;
    }

    public void registerTpaRequest(String userName) {
        tpaRequests.put(userName, Long.valueOf(System.currentTimeMillis()));
    }


    public void setChangingSector(boolean is) {
        this.isChangingSector = is;
    }

    public boolean hasTpaRequestFrom(String userName) {
        return (tpaRequests.getIfPresent(userName) != null);
    }

    public void removeTpaRequest(String userName) {
        tpaRequests.invalidate(userName);
    }

    public void clearTpaRequests() {
        tpaRequests.invalidateAll();
    }

    public Cache<String, Long> getTpaRequests() {
        return tpaRequests;
    }

    public Long addPlayerTime(Long time) {
        this.playerTime += time;
        return time;
    }

    public String serialize() {
        return GSON.toJson(this);
    }

    public static UserDataModel deserialize(String json) {
        UserDataModel user = GSON.fromJson(json, UserDataModel.class);
        if (user.tpaRequests == null) {
            user.tpaRequests = CacheBuilder.newBuilder().expireAfterWrite(25L, TimeUnit.SECONDS).build();
        }

        return user;
    }

}
