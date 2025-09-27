package pl.zczb.cashblock.helpers;

public enum TimeUtil {
    TICK("TICK", 0, "TICK", 0, "TICK", 0, 50, 50),
    MILLISECOND("MILLISECOND", 1, "MILLISECOND", 1, "MILLISECOND", 1, 1, 1),
    SECOND("SECOND", 2, "SECOND", 2, "SECOND", 2, 1000, 1000),
    MINUTE("MINUTE", 3, "MINUTE", 3, "MINUTE", 3, 60000, 60),
    HOUR("HOUR", 4, "HOUR", 4, "HOUR", 4, 3600000, 60),
    DAY("DAY", 5, "DAY", 5, "DAY", 5, 86400000, 24),
    WEEK("WEEK", 6, "WEEK", 6, "WEEK", 6, 604800000, 7);

    public static final int MPT = 50;

    private final int time;
    private final int timeMulti;

    TimeUtil(String s3, int n3, String s2, int n2, String s, int n, int time, int timeMulti) {
        this.time = time;
        this.timeMulti = timeMulti;
    }

    public int getMulti() {
        return this.timeMulti;
    }

    public int getTime() {
        return this.time;
    }

    public int getTick() {
        return this.time / 50;
    }

    public int getTime(int multi) {
        return this.time * multi;
    }

    public int getTick(int multi) {
        return getTick() * multi;
    }
}


