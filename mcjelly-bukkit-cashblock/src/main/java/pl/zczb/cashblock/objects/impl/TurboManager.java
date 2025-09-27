package pl.zczb.cashblock.objects.impl;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
public class TurboManager {

    @Getter
    public static HashMap<Integer,String> turbodrop = new HashMap<>();
    @Setter
    @Getter public static String turbodrop_admin = null;
    @Setter @Getter public static Long turbodrop_time = 0L;

    public static boolean isOnTurboDrop() {
        return turbodrop_time > System.currentTimeMillis();
    }


}
