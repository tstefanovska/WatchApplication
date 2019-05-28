package com.example.teast.watchapplication;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.example.teast.watchapplication.Data.User;

import java.util.List;

public class UserRepository {
    private String DB_NAME = "db_users";
    private static UserDatabase userDatabase;

    public  UserRepository(Context context){
        userDatabase = UserDatabase.getDatabase(context);
    }
    public  void insertUser(String name,String userName,String endcryptedPassword, int age){
        User user = new User(name,userName, endcryptedPassword, age);
        insertUser(user);
    }

    private static void insertUser(final User user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userDatabase.daoAccess().insertUser(user);
                return null;
            }
        }.execute();
    }

    public static void updateUser(final User user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userDatabase.daoAccess().updateUser(user);
                return null;
            }
        }.execute();
    }
   /* public static void deleteUser(final String userName) {
        final LiveData<User> user = getUser(userName);
        if(user != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    userDatabase.daoAccess().deleteUser(user.getValue());
                    return null;
                }
            }.execute();
        }
    }
    public static void deleteUser(final User user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userDatabase.daoAccess().deleteUser(user);
                return null;
            }
        }.execute();
    }*/

    public LiveData<List<User>> getUsers() {
        return userDatabase.daoAccess().fetchAllUsers();
    }

    public User getUser(String userName) {
        return userDatabase.daoAccess().getUser(userName);
    }


}
