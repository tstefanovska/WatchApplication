package com.example.teast.watchapplication.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.example.teast.watchapplication.Adapters.SearchedShowAdapter;
import com.example.teast.watchapplication.GetDataService;
import com.example.teast.watchapplication.Data.PopularShows;
import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.Data.Result;
import com.example.teast.watchapplication.RetrofitClientInstance;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchableActivity extends AppCompatActivity {
    public static String API_KEY="f753674b0ff25f45fe8397f355b693bb";
     List<Result> resultShows;
     RecyclerView recyclerView;
     Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            makeCall(query);
        }
        setupToolbar();

    }


    private void makeCall(String query) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<PopularShows> call = service.getSearchedShows(API_KEY,query);
        call.enqueue(new Callback<PopularShows>() {
            @Override
            public void onResponse(Call<PopularShows> call, Response<PopularShows> response) {
                generateShowsList(response.body());
            }

            @Override
            public void onFailure(Call<PopularShows> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(SearchableActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(SearchableActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                // Toast.makeText(RegisterShowsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateShowsList(PopularShows shows) {
        resultShows = shows.getResults();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        SearchedShowAdapter searchedShowAdapter = new SearchedShowAdapter(resultShows);
        recyclerView.setAdapter(searchedShowAdapter);
        searchedShowAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int position = recyclerView.getChildAdapterPosition(v);
               Result show = resultShows.get(position);
               Intent singleShowIntent = new Intent(SearchableActivity.this, SingleShowActivity.class);
               singleShowIntent.putExtra("Show", show);
               SearchableActivity.this.startActivity(singleShowIntent);
            }
        });

    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
     /*   toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserAreaActivity.class));
            }
        });*/

    }
}
