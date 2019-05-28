package com.example.teast.watchapplication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.teast.watchapplication.Adapters.SearchedShowAdapter;
import com.example.teast.watchapplication.Adapters.WatchListAdapter;
import com.example.teast.watchapplication.Data.Result;
import com.example.teast.watchapplication.Data.SingleShow;
import com.example.teast.watchapplication.R;

import java.util.ArrayList;

public class WatchListActivity extends AppCompatActivity {
    TextView txtViewType;
    RecyclerView recyclerView;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        txtViewType = (TextView) findViewById(R.id.txtTypeList);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewWatchList);
        btnClose = (Button) findViewById(R.id.btn_close);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));
        Intent intent = getIntent();
        ArrayList<SingleShow> watchList = intent.getParcelableArrayListExtra("List");
        String type = intent.getStringExtra("Type");
        if (watchList.size() == 0) {
            txtViewType.setText("Sorry, your " + type + " is empty.");
        } else {
            txtViewType.setText("Your " + type);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            WatchListAdapter watchListAdapter = new WatchListAdapter(getApplicationContext(),watchList);
            recyclerView.setAdapter(watchListAdapter);

        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
