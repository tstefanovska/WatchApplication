package com.example.teast.watchapplication;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.teast.watchapplication.Data.User;

import java.util.List;

@Dao
public interface DaoAccess {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertUser(User user);

    @Query("SELECT * FROM user_table ORDER BY userName ASC")
    LiveData<List<User>> fetchAllUsers();

    @Query("SELECT * FROM user_table ORDER BY userName ASC")
    List<User> fetchAllUsersList();


    @Query("SELECT * FROM user_table WHERE userName =:name")
    User getUser(String name);
fffffff

    @Update
    void updateUser(User user);
    @Delete
    void deleteUser(User user);
}
