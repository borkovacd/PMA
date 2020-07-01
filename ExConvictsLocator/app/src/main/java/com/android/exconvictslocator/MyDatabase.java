package com.android.exconvictslocator;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.Report;
import com.android.exconvictslocator.entities.User;
import com.android.exconvictslocator.entities.daos.ExConvictDao;
import com.android.exconvictslocator.entities.daos.ReportDao;
import com.android.exconvictslocator.entities.daos.UserDao;


@Database(entities={User.class, ExConvict.class, Report.class}, version=4)
public abstract class MyDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ReportDao reportDao();
    public abstract ExConvictDao exConvictDao();

    public static final String NAME = "MyDataBase";
    //private static volatile MyDatabase INSTANCE;
    private static volatile MyDatabase INSTANCE;

    public static MyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class, NAME)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();

                }
            }
        }
        return INSTANCE;
    }

}
