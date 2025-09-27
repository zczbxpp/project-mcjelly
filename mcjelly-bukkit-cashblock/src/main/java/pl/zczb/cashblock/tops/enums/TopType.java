package pl.zczb.cashblock.tops.enums;

public enum TopType {
    PLN("pln", "pln", "getPln"),
    STONE("stone", "stone", "getStone"),
    PRESTIGE("prestiz", "prestiz", "getPrestigeLevel"),
    LVL("lvl", "lvl", "getLvl");

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


