package com.tejas.root.finalapp.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.tejas.root.finalapp.entities.CoinsStack;
import com.tejas.root.finalapp.entities.PaperInformation;
import com.tejas.root.finalapp.interfaces.TestDao;

/**
 * Created by root on 27/12/18.
 */

@Database(entities = {PaperInformation.class, CoinsStack.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TestDao getTestDao();
}
