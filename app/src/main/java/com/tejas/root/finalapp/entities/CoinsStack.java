package com.tejas.root.finalapp.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by root on 1/1/19.
 */

@Entity(tableName = "coinsTable")
public class CoinsStack {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "coins")
    private int coins;

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
