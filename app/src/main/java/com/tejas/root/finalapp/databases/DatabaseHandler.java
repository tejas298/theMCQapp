package com.tejas.root.finalapp.databases;

/**
 * Created by root on 27/12/18.
 */

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseHandler {

    private Context mCtx;
    private static DatabaseHandler mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseHandler(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "tejas")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public static synchronized DatabaseHandler getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseHandler(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
