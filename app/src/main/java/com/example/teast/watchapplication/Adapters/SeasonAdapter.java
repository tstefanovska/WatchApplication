package com.example.teast.watchapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.teast.watchapplication.Activities.SingleShowActivity;
import com.example.teast.watchapplication.Data.Season;
import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder> {
    int numSeasons;
    Context context;
    List<Season> seasons;
    public List<Boolean> isClicked = new ArrayList<>(); //Returns a value indicating if the item is clicked or not


    public SeasonAdapter(Context context,int numSeasons,List<Season> seasons){
        this.context = context;
        this.numSeasons = numSeasons;
        for(int i=0;i<numSeasons;i++){ isClicked.add(false);}
        this.seasons = seasons;
    }

    @NonNull
    @Override
    public SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View seasonView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_season_item,viewGroup,false);
        SeasonViewHolder seasonViewHolder = new SeasonViewHolder(seasonView);
        return seasonViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SeasonViewHolder seasonViewHolder, int position) {
        int num = position + 1;
        seasonViewHolder.button.setText("Season "+num);

    }

    @Override
    public int getItemCount() {
        return numSeasons;
    }

    public class SeasonViewHolder extends RecyclerView.ViewHolder {
        Button button;


        public SeasonViewHolder(@NonNull View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.btnSeasonNumber);

        }


    }
}
