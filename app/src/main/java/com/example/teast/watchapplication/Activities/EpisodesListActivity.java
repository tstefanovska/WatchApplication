package com.example.teast.watchapplication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.teast.watchapplication.Adapters.EpisodeAdapter;
import com.example.teast.watchapplication.Data.Episodes;
import com.example.teast.watchapplication.GetDataService;
import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.RetrofitClientInstance;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodesListActivity extends AppCompatActivity {
    public static String API_KEY="f753674b0ff25f45fe8397f355b693bb";
    public static String LANGUAGE="en-US";
    Button btnClose;
    RecyclerView episodesList;
    int seasonNumber;
    int tvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes_list);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));
        btnClose = (Button) findViewById(R.id.btn_close_episodes);
        Intent intent = getIntent();
        seasonNumber = intent.getIntExtra("SeasonNumber",0);
        tvId = intent.getIntExtra("TvId",0);
        makeCallEpisodes(tvId,seasonNumber);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void makeCallEpisodes(int tvId,int seasonNumber) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Episodes> call = service.getEpisodes(tvId,seasonNumber,API_KEY,LANGUAGE);
        call.enqueue(new Callback<Episodes>() {
            @Override
            public void onResponse(Call<Episodes> call, Response<Episodes> response) {
                displayEpisodes(response.body());
            }

            @Override
            public void onFailure(Call<Episodes> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(EpisodesListActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(EpisodesListActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
            }
        });
    }

    private void displayEpisodes(Episodes episodes) {
        episodesList = (RecyclerView) findViewById(R.id.recyclerViewEpisodesList);
        EpisodeAdapter episodeAdapter = new EpisodeAdapter(getApplicationContext(),episodes,episodes.getEpisodes().size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        episodesList.setLayoutManager(linearLayoutManager);
        episodesList.setAdapter(episodeAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(episodesList.getContext(), linearLayoutManager.getOrientation());
        episodesList.addItemDecoration(dividerItemDecoration);
    }
}
