package com.example.teast.watchapplication.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.teast.watchapplication.Data.User;
import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.RoomService;
import com.example.teast.watchapplication.SaveSharedPreference;
import com.example.teast.watchapplication.UserDatabase;

public class LoginActivity extends AppCompatActivity {

    public static String DATABASE_NAME ="moviesDb";
    public static String API_KEY="f753674b0ff25f45fe8397f355b693bb";
    public static String SORT_PARAM="popularity.desc";
    public static String LANGUAGE="en-US";
    public static String IMAGE_BASE_URL="http://image.tmdb.org/t/p/w50";
    public static final String FILTER_LOGIN = "broadcast_login";
    public static final String FILTER_LOGGED_IN = "broadcast_logged_in";
    public static final String LOGIN_ACTION_KEY= "login";
    public static final String LOGGED_IN_ACTION_KEY = "logged_in";
    public static UserDatabase userDatabase;
    MyReceiver myReceiver;
    Toolbar toolbar;
    EditText etUserName;
    EditText etPassword;
    Button btnLogin;
    TextView registerLink;
    String userName;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUserName = (EditText) findViewById(R.id.et_UserNameLogin);
        etPassword = (EditText) findViewById(R.id.et_PasswordLogin);
        btnLogin = (Button) findViewById(R.id.login_btn);
        registerLink = (TextView) findViewById(R.id.linkRegister);
        userDatabase = UserDatabase.getDatabase(getApplicationContext());

        if(SaveSharedPreference.getUserName(LoginActivity.this).length() != 0) {
            String userName = SaveSharedPreference.getUserName(LoginActivity.this);
            Intent intent = new Intent(LoginActivity.this, RoomService.class);
            intent.setAction(LOGGED_IN_ACTION_KEY);
            intent.putExtra("userName",userName);
            startService(intent);
        }
        else {
            // Stay at the current activity.

            registerLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    LoginActivity.this.startActivity(registerIntent);
                }
            });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userName = etUserName.getText().toString();
                    password = etPassword.getText().toString();

                    Intent intent = new Intent(LoginActivity.this, RoomService.class);
                    intent.setAction(LOGIN_ACTION_KEY);
                    intent.putExtra("userName", userName);
                    intent.putExtra("password", password);
                    startService(intent);
                }
            });

        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(FILTER_LOGIN)){
            boolean isSuccessful = intent.getBooleanExtra("successful",false);
            if(isSuccessful){
             User user = intent.getParcelableExtra("User");
             SaveSharedPreference.setUserName(getApplicationContext(),user.getUserName()); // Set SaveSharedPreference to stay logged-in
             Intent userAreaIntent = new Intent(LoginActivity.this,UserAreaActivity.class);
             userAreaIntent.putExtra("User",user);
             LoginActivity.this.startActivity(userAreaIntent);
            }else{
                etPassword.requestFocus();
                etPassword.setError("Password and user name don't match!");
            }
        }
        if(intent.getAction().equals(FILTER_LOGGED_IN)){
            User user = intent.getParcelableExtra("User");
            Intent userAreaIntent = new Intent(LoginActivity.this,UserAreaActivity.class);
            userAreaIntent.putExtra("User",user);
            LoginActivity.this.startActivity(userAreaIntent);
        }
        }
    }
    private void setReceiver(){
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FILTER_LOGIN);
        intentFilter.addAction(FILTER_LOGGED_IN);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }
    @Override
    protected void onStart() {
        setReceiver();
        super.onStart();
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onStop();
    }


}
