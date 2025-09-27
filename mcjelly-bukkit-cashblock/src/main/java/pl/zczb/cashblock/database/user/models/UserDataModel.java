package pl.zczb.cashblock.database.user.models;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import lombok.Data;
import pl.zczb.cashblock.helpers.OtherHelper;

import java.util.concurrent.TimeUnit;

@Data
public class UserDataModel {

    private static final Gson GSON = new Gson();
    private String uuid;
    private String nick;
    private double pln;
    private int stone;
    private transient Cache<String, Long> tpaRequests;
    private double mnoznik;
    private String activePet;
    private long turboDropTime;
    private boolean turboDrop;

    private final UserPets userPets;
    private final UserLvl userLvl;
    private final UserHomes userHomes;

    private boolean testBrush;
    private long testBrushTime;

    private boolean autoCx;

    private final UserPrestiz userPrestiz;

    public UserDataModel(String uuid, String name, double pln, int stone,
                         String activePet, long turboDropTime, boolean turboDrop, UserPets userPets,
                         UserLvl userLvl, UserHomes userHomes, boolean testBrush, long testBrushTime, boolean autoCx, UserPrestiz userPrestiz) {
        this.uuid = uuid;
        this.nick = name;

        this.pln = pln;
        this.stone = stone;
        this.tpaRequests = CacheBuilder.newBuilder().expireAfterWrite(25L, TimeUnit.SECONDS).build();
        this.mnoznik = 1.00D;
        this.activePet = activePet;

        this.turboDropTime = turboDropTime;
        this.turboDrop = turboDrop;

        this.userPets = userPets;
        this.userLvl = userLvl;

        this.userHomes = userHomes;


        this.testBrush = testBrush;
        this.testBrushTime = testBrushTime;

        this.autoCx = autoCx;
        this.userPrestiz = userPrestiz;
    }

    public double getPln() {
        return OtherHelper.round(this.pln, 2);
    }

    public double addStone(final int coins) {
        this.stone += coins;
        return coins;
    }

    public double addPln(final double coins) {
        this.pln += coins;
        return coins;
    }


    public double removePln(final double coins) {
        this.pln -= coins;
        return coins;
    }

    public double addMnoznik(final double coins) {
        this.mnoznik += coins;
        return coins;
    }

    public double removeMnoznik(final double coins) {
        this.mnoznik -= coins;
        return coins;
    }

    public void setPln(final double coins) {
        this.pln = coins;
    }

    public void registerTpaRequest(String userName) {
        tpaRequests.put(userName, Long.valueOf(System.currentTimeMillis()));
    }

    public boolean hasTpaRequestFrom(String userName) {
        return (tpaRequests.getIfPresent(userName) != null);
    }

    public void removeTpaRequest(String userName) {
        tpaRequests.invalidate(userName);
    }

    public Cache<String, Long> getTpaRequests() {
        return tpaRequests;
    }

    public int getLvl() {
        return this.userLvl.getLvl();
    }

    public int getPrestigeLevel() {
        return this.userPrestiz.getPrestiz();
    }


    public String serialize() {
        return GSON.toJson(this);
    }

    public static UserDataModel deserialize(String json) {
        UserDataModel user = GSON.fromJson(json, UserDataModel.class);
        return user;
    }

}
