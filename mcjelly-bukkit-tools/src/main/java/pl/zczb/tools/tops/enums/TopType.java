package pl.zczb.tools.tops.enums;

public enum TopType {
    PARKOUR("userParkour", "parkour", "getParkourTime"),
    TIME("userTime", "time", "getActualPlayerTime");

    private final String tableName;

    private final String description;

    private final String methodName;


    TopType(String tableName, String description, String methodName) {
        this.tableName = tableName;
        this.description = description;
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTableName() {
        return this.tableName;
    }
}


