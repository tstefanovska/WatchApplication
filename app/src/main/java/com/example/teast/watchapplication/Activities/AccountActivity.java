package com.example.teast.watchapplication.Activities;

import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.teast.watchapplication.Data.User;
import com.example.teast.watchapplication.R;

public class AccountActivity extends AppCompatActivity {
    EditText editTextName;
    EditText editTextUserName;
    EditText editTextAge;
    Toolbar toolbar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        editTextName = (EditText) findViewById(R.id.editTxtName);
        //editTextUserName = (EditText) findViewById(R.id.editTxtUserName);
        //editTextAge = (EditText) findViewById(R.id.editTxtAge);
        Intent intent = getIntent();
        this.user = intent.getParcelableExtra("User");
        char[] name = user.getName().toCharArray();
        char[] userName = user.getUserName().toCharArray();
        char[] age = String.valueOf(user.getAge()).toCharArray();
        editTextName.setInputType(InputType.TYPE_NULL);
        editTextName.setText(name,0,name.length);
      //  editTextUserName.setText(userName, 0 ,name.length);
       // editTextAge.setText(age,0,age.length);

        setupToolbar();

    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserAreaActivity.class);
                intent.putExtra("User", getUser());
                startActivity(intent);
            }
        });
    }
    private User getUser(){return user;}
}
