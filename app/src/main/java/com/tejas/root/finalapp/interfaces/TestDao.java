package com.tejas.root.finalapp.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tejas.root.finalapp.entities.CoinsStack;
import com.tejas.root.finalapp.entities.PaperInformation;

import java.util.List;

/**
 * Created by root on 27/12/18.
 */

@Dao
public interface TestDao {

    @Query("SELECT * FROM paperinformation")
    List<PaperInformation> getAllPapers();

    @Insert
    void insertPapers(List<PaperInformation> list);

    @Delete
    void deleteRecords(List<PaperInformation> list);
    @Query("delete from paperinformation")
    void deleteAllRecords();

    @Update
    void updatePaperInformation(PaperInformation info);

    @Update
    void updateCoins(CoinsStack coinsStack);

    @Query("SELECT * from coinsTable")
    List<CoinsStack> getCoins();

    @Insert
    void insertCoin(CoinsStack stack);

    @Query("SELECT SUM(coins) from coinsTable")
    int getTotalCoins();

    @Query("delete from coinsTable")
    void deleteCoins();
}
