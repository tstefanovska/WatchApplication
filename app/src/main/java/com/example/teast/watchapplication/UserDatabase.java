package com.example.teast.watchapplication;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.teast.watchapplication.Data.User;

@Database(entities = {User.class}, version = 3,exportSchema = false)
@TypeConverters({Converters.class})

public abstract class UserDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
    private static UserDatabase INSTANCE;
    private static final String DATABASE_NAME = "usersDb";

    public static UserDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
