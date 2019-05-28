package com.example.teast.watchapplication;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.teast.watchapplication.Activities.LoginActivity;
import com.example.teast.watchapplication.Activities.RegisterActivity;
import com.example.teast.watchapplication.Activities.UserAreaActivity;
import com.example.teast.watchapplication.Data.User;

public class RoomService extends IntentService {


    public RoomService(){
        super("RoomService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        UserDatabase userDatabase = LoginActivity.userDatabase;
        String action = intent.getAction();
        if(action.equals(RegisterActivity.REGISTER_ACTION_KEY)){
            User user = intent.getParcelableExtra("User");
            User userCheck = userDatabase.daoAccess().getUser(user.getUserName());
            if(userCheck!=null){
                String message = "Invalid username";
                intent.putExtra("UserName",message);
                intent.setAction(RegisterActivity.BROADCAST_ACTION_KEY);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }else {
                userDatabase.daoAccess().insertUser(user);
                intent.setAction(RegisterActivity.BROADCAST_ACTION_KEY);
                intent.putExtra("UserName","Valid username");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }
        if(action.equals(LoginActivity.LOGIN_ACTION_KEY)){
            String userName = intent.getStringExtra("userName");
            String password = intent.getStringExtra("password");
            User user = userDatabase.daoAccess().getUser(userName);
            boolean successful = password.equals(user.getPassword()); // checks if the passwords match
            intent.setAction(LoginActivity.FILTER_LOGIN);
            if(successful){
                intent.putExtra("User",user);
            }
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putExtra("successful",successful));
        }
        if(action.equals(LoginActivity.LOGGED_IN_ACTION_KEY)){
            String  userName = intent.getStringExtra("userName");
            User user = userDatabase.daoAccess().getUser(userName);
            intent.setAction(LoginActivity.FILTER_LOGGED_IN);
            intent.putExtra("User",user);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
        if(action.equals(UserAreaActivity.UPDATE_ACTION_KEY)){
            User user = intent.getParcelableExtra("User");
            userDatabase.daoAccess().updateUser(user);
        }

    }
}
