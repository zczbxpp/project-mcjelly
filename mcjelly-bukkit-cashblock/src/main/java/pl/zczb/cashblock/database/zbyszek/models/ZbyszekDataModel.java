package pl.zczb.cashblock.database.zbyszek.models;

import lombok.Data;

@Data
public class ZbyszekDataModel {

    private String name;
    private int balance;

    public ZbyszekDataModel(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public int addBalance(final int balance) {
        this.balance += balance;
        return balance;
    }

    public int removeBalance(final int balance) {
        this.balance -= balance;
        return balance;
    }
}
