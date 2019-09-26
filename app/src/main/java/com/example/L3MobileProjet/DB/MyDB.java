package com.example.L3MobileProjet.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class},version =1, exportSchema = false)
public abstract class MyDB extends RoomDatabase {

    private static MyDB INSTANCE;

    public abstract MyDAO mydao();

    private static final Object sLock = new Object();

    public static MyDB getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        MyDB.class, "MyDB.db")
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }
}
