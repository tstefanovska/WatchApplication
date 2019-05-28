package com.example.teast.watchapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.Data.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrendingShowAdapter extends RecyclerView.Adapter<TrendingShowAdapter.TrendingShowViewHolder> {
    public String IMAGE_BASE_URL="https://image.tmdb.org/t/p/w300";
    private List<Result> shows;
    public List<Boolean> isClicked = new ArrayList<>(); //Returns a value indicating if the item is clicked or not
    private int position;
    Context context;

    public TrendingShowAdapter(List<Result> shows, Context context){
        this.shows = shows;
        this.context = context;
        for(int i=0;i<shows.size();i++){ isClicked.add(false);}
    }

    @NonNull
    @Override
    public TrendingShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View trendingShowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_show_item,parent,false);
        TrendingShowViewHolder trendingShowViewHolder= new TrendingShowViewHolder(trendingShowView);
        return trendingShowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrendingShowViewHolder viewHolder, int position) {
        String imgUrl=IMAGE_BASE_URL+shows.get(position).getPosterPath();
        Picasso.get().load(imgUrl).into(viewHolder.imageView);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = shows.get(viewHolder.getAdapterPosition()).getName();
                if(isClicked.get(viewHolder.getAdapterPosition())) { //If the item is clicked, it is added to a watch-list
                    Toast.makeText(context, productName + " is added to your watch-list!", Toast.LENGTH_SHORT).show();
                }
                viewHolder.imageView.setImageAlpha(isClicked.get(viewHolder.getAdapterPosition()) ? 127 : 255);
                // Changes the transparency of the photo based on if it's clicked or not
            }
        });
        viewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(viewHolder.getAdapterPosition());
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return shows.size();
    }
    public class TrendingShowViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView imageView;
        Button btnAdd;
        public TrendingShowViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.imageTrending);
            view.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE,R.id.toWatchList, Menu.NONE,R.string.add_to_to_watch_list);
            menu.add(Menu.NONE,R.id.toWatchedList, Menu.NONE,R.string.add_to_watched_list);
            menu.add(Menu.NONE,R.id.toWatchingList, Menu.NONE,R.string.add_to_watching_list);
        }

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
