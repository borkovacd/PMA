package com.android.exconvictslocator;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.ExConvictReport;
import com.android.exconvictslocator.entities.Report;
import com.android.exconvictslocator.entities.User;
import com.android.exconvictslocator.entities.daos.ExConvictDao;
import com.android.exconvictslocator.entities.daos.ReportDao;
import com.android.exconvictslocator.entities.daos.UserDao;


@Database(entities={User.class, ExConvict.class, Report.class}, version=3)
public abstract class MyDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ReportDao reportDao();
    public abstract ExConvictDao exConvictDao();

    public static final String NAME = "MyDataBase";
    private static volatile MyDatabase INSTANCE;

    static MyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class, NAME).fallbackToDestructiveMigration().allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private  void populateDbInit(){
        ExConvict exc1 = new ExConvict( "Pera", "Peric", "-",
        R.drawable.img1, "Topolska 18", "M", "1955", "ubistvo", "opis..........1");
        ExConvict exc2 = new ExConvict( "Mika", "Mikic", "-",
                R.drawable.img2, "Topolska 19", "M", "1968", "ubistvo", "opis..........2");
        ExConvict exc3 = new ExConvict( "Zika", "Zikic", "-",
                R.drawable.img3, "Topolska 20", "M", "1985", "silovanje", "opis..........3");




    }

}
