package com.example.teast.watchapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teast.watchapplication.Activities.SingleShowActivity;
import com.example.teast.watchapplication.Data.Result;
import com.example.teast.watchapplication.Data.SingleShow;
import com.example.teast.watchapplication.GetDataService;
import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.RetrofitClientInstance;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.WatchListHolder> {
    public String IMAGE_BASE_URL="https://image.tmdb.org/t/p/w342";
    public static String API_KEY="f753674b0ff25f45fe8397f355b693bb";
    public static String LANGUAGE="en-US";
    Context context;
    List<SingleShow> results;

    public WatchListAdapter(Context context, List<SingleShow> results){
        this.context = context;
        this.results = results;}

    @NonNull
    @Override
    public WatchListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_watchlist, viewGroup,false);
       WatchListHolder watchListHolder = new WatchListHolder(view);
        return watchListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WatchListHolder watchListHolder, int position) {
        watchListHolder.textViewName.setText(results.get(position).getName());

        watchListHolder.textViewEpisodeAir.setText("Latest episode aired on " + parseDate(results.get(position).getLastAirDate()));
    }

    private String parseDate(String lastAirDate) {
        String[] date = lastAirDate.split("-");
        return date[2] + "/"+ date[1] + "/" +date[0];
    }


    @Override
    public int getItemCount() {
        return results.size();
    }


    public static class WatchListHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textViewName;
        TextView textViewEpisodeAir;

        public WatchListHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view_watchlist);
            textViewName = (TextView) itemView.findViewById(R.id.watchListShowName);
            textViewEpisodeAir = (TextView) itemView.findViewById(R.id.last_episode_aired);
        }
    }
}
