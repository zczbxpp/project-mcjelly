package pl.zczb.tools.tops.impl;

import java.io.Serializable;

public class Top implements Serializable {
    private final String nickName;
    private final String topValue;

    public Top(String nickName, String topValue) {
        this.nickName = nickName;
        this.topValue = topValue;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getTopValue() {
        return this.topValue;
    }
}


