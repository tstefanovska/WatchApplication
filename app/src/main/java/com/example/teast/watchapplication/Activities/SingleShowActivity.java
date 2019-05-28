package com.example.teast.watchapplication.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teast.watchapplication.Adapters.EpisodeAdapter;
import com.example.teast.watchapplication.Adapters.SeasonAdapter;
import com.example.teast.watchapplication.Data.Episode;
import com.example.teast.watchapplication.Data.Episodes;
import com.example.teast.watchapplication.Data.SingleShow;
import com.example.teast.watchapplication.GetDataService;
import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.Data.Result;
import com.example.teast.watchapplication.RecyclerTouchListener;
import com.example.teast.watchapplication.RecyclerViewClickListener;
import com.example.teast.watchapplication.RetrofitClientInstance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleShowActivity extends AppCompatActivity  {
    public String IMAGE_BASE_URL="https://image.tmdb.org/t/p/w500";
    public static String API_KEY="f753674b0ff25f45fe8397f355b693bb";
    public static String LANGUAGE="en-US";

    TextView textViewName;
    TextView textViewGenres;
    TextView textViewNumSeasons;
    TextView textViewCreated;
    TextView textViewFirstAirDate;
    ImageView imageViewShow;
    Toolbar toolbar;
    RecyclerView recyclerView;
    MenuPopupWindow.MenuDropDownListView dropDownListView;
    Episodes episodes;
    SingleShow show;
    Button btnOriginCountry;
    int tvId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_show);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewGenres = (TextView) findViewById(R.id.textViewGenres);
        textViewNumSeasons = (TextView) findViewById(R.id.textViewNumSeasons);
        textViewCreated = (TextView) findViewById(R.id.textViewCreated);
        textViewFirstAirDate = (TextView) findViewById(R.id.textViewFirstAirDate);
        imageViewShow = (ImageView) findViewById(R.id.imageShow);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewHorizontalSeasons);
        btnOriginCountry = (Button) findViewById(R.id.btnOriginCountry);

        setupToolbar();
        Intent intent = getIntent();
        final Result show = intent.getParcelableExtra("Show");
        this.tvId = show.getId();
        makeCall(show.getId());
        btnOriginCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent countryIntent = new Intent(SingleShowActivity.this,MapsActivity.class);
                countryIntent.putExtra("Country", (Parcelable) show.getOriginCountry());
                startActivity(countryIntent);
            }
        });


    }

    private void makeCall(Integer id) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<SingleShow> call = service.getSingleShow(id,API_KEY,LANGUAGE);
        call.enqueue(new Callback<SingleShow>() {
            @Override
            public void onResponse(Call<SingleShow> call, Response<SingleShow> response) {
                displayShow(response.body());
            }

            @Override
            public void onFailure(Call<SingleShow> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(SingleShowActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(SingleShowActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void displayShow(SingleShow show) {
        this.show = show;
      if(show.getGenres().size() > 0){
          String genres = "";
          for(int i=0;i<show.getGenres().size();i++){
              genres+=show.getGenres().get(i).getName() + ", ";
          }
          genres = genres.substring(0,genres.length()-2);
          textViewGenres.setText(genres);
      }
      textViewName.setText(show.getName());
      String imgUrl=IMAGE_BASE_URL+show.getPosterPath();
      Picasso.get().load(imgUrl).into(imageViewShow);
      textViewNumSeasons.setText(getString(R.string.num_seasons) + " " + show.getSeasons().size());

      if(show.getCreatedBy().size() > 0){
          String createdBy="";
          for(int i=0;i<show.getCreatedBy().size();i++){
              createdBy+=show.getCreatedBy().get(i).getName() + ", ";
          }
          createdBy = createdBy.substring(0,createdBy.length()-2);
          textViewCreated.setText("Created by: "+ createdBy);
          String dateFirstAired = parseDate(show.getFirstAirDate());
          textViewFirstAirDate.setText("First episode aired on: " + dateFirstAired);
          setupSeasonList(show);
      }
    }


    private void setupSeasonList(final SingleShow show) {
        int numSeasons = (show.getSeasons().get(0).getName().equals("Specials") ? show.getSeasons().size() - 1 : show.getSeasons().size());
        final SeasonAdapter seasonAdapter = new SeasonAdapter(getApplicationContext(),numSeasons,show.getSeasons());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(SingleShowActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(seasonAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), horizontalLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
             int num = (show.getSeasons().get(0).getName().equals("Specials") ? position + 1 : position);
             Intent intent = new Intent(SingleShowActivity.this,EpisodesListActivity.class);

             intent.putExtra("SeasonNumber",num);

             intent.putExtra("TvId",show.getId());
             startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }



    private String parseDate(String firstAirDate) {
        String[] parts = firstAirDate.split("-");
        String result = parts[2] + "/" + parts[1] + "/" + parts[0];
        return result;
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),SearchableActivity.class));
            }
        });*/
    }
    private int getTvId(){return tvId;}


    private SingleShow getShow(){
        return show;
    }


}
