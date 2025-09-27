package pl.zczb.itemshop.data.user.models;

import com.google.gson.Gson;
import lombok.Data;
import pl.zczb.itemshop.helpers.GlobalHelper;


@Data
public class UserDataModel {

    private static final Gson GSON = new Gson();
    private String uuid;
    private String nick;
    private double pln;

    public UserDataModel(String uuid, String name, double pln) {
        this.uuid = uuid;
        this.nick = name;
        this.pln = pln;
    }

    public double getPln() {
        return GlobalHelper.round(this.pln, 2);
    }


    public double addPln(final double coins) {
        this.pln += coins;
        return coins;
    }

    public double removePln(final double coins) {
        this.pln -= coins;
        return coins;
    }


    public void setPln(final double coins) {
        this.pln = coins;
    }

    public String serialize() {
        return GSON.toJson(this);
    }

    public static UserDataModel deserialize(String json) {
        UserDataModel user = GSON.fromJson(json, UserDataModel.class);


        return user;
    }

}
