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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.RoomService;
import com.example.teast.watchapplication.SHAHash;
import com.example.teast.watchapplication.Data.User;
import com.example.teast.watchapplication.UserDatabase;
import com.example.teast.watchapplication.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static String DATABASE_NAME ="usersDb";
    public static final String REGISTER_ACTION_KEY= "register_key";
    public static final String BROADCAST_ACTION_KEY = "register_broadcast";
    public static UserDatabase userDatabase;
    Toolbar toolbar;
    User user;
    MyRegisterReceiver myReceiver;
    EditText etName;
    EditText etUserName;
    EditText etPassword;
    EditText etConfirmPassword;
    Spinner spinnerAge;
    TextView textView;
    SpinnerAdapter spinnerAdapter;
    SHAHash encryption;
    Button btnRegister;
    UserRepository userRepository;
    Integer age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acivity);

        etName = (EditText) findViewById(R.id.et_Name);
        etUserName = (EditText) findViewById(R.id.et_UserName);
        etPassword = (EditText) findViewById(R.id.et_Password);
        etConfirmPassword = (EditText) findViewById(R.id.et_PasswordAgain);
        spinnerAge = (Spinner) findViewById(R.id.age_Spinner);
        textView = (TextView) findViewById(R.id.textV);
        btnRegister = (Button) findViewById(R.id.register_btn);
        encryption = new SHAHash();

        userRepository = new UserRepository(getApplicationContext());
        userDatabase = UserDatabase.getDatabase(getApplicationContext());
        setupToolbar();
        List age = new ArrayList<Integer>();
        for (int i = 1; i <= 100; i++) {
            age.add(Integer.toString(i));
        }
        final ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, age);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setAdapter(spinnerArrayAdapter);
        spinnerAge.setOnItemSelectedListener(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();

                final String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if(name.length() == 0){
                    etName.requestFocus();
                    etName.setError("Name field cannot be empty!");
                }/*else if(!name.matches("[a-zA-Z]")){
                    etName.requestFocus();
                    etName.setError("Name must be only alphabetical characters!");
                }*/else if(userName.length() == 0){
                    etUserName.requestFocus();
                    etUserName.setError("Username field cannot be empty!");
                } else if(password.length() < 8){
                    etPassword.requestFocus();
                    etPassword.setError("Password must be at least 8 characters");
                }else if (!password.equals(confirmPassword)) {
                    etPassword.requestFocus();
                    etPassword.setError("Passwords must be matching!");
                } else {
                    Integer ageSend = getAge();
                   // String hashedPassword = SHAHash.get_SHA_256_SecurePassword(password);
                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                byte[] secondEncoded = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                    final User user = new User(name, userName, password, ageSend);
                    setUser(user);
                    Intent intent = new Intent(RegisterActivity.this, RoomService.class);
                    intent.setAction(REGISTER_ACTION_KEY);
                    intent.putExtra("User",user);
                    startService(intent);



                /*Intent registerShow = new Intent(RegisterActivity.this, RegisterShowsActivity.class);
                registerShow.putExtra("UserName",userName);
                RegisterActivity.this.startActivity(registerShow);*/


                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        age = Integer.parseInt((String)parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public Integer getAge(){return age;}

    private class MyRegisterReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("UserName");
            if(message.equals("Invalid username")){
                etUserName.requestFocus();
                etUserName.setError("Username is already taken");
            }else{
                Intent userAreaIntent = new Intent(RegisterActivity.this, UserAreaActivity.class);
                userAreaIntent.putExtra("User", getUser());
                RegisterActivity.this.startActivity(userAreaIntent);
            }

        }
    }
    private void setReceiver(){
        myReceiver = new RegisterActivity.MyRegisterReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_KEY);
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
    public void setUser(User user){
        this.user = user;
    }
    private User getUser(){return user;}

    private void setupToolbar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Register");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                intent.putExtra("User", getUser());
                startActivity(intent);
            }
        });
    }

}
