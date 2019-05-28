package com.example.teast.watchapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.teast.watchapplication.Data.Episode;
import com.example.teast.watchapplication.Data.Episodes;
import com.example.teast.watchapplication.Data.Season;
import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.RecyclerViewClickListener;

import org.w3c.dom.Text;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {
    private Context context;
    private List<Episode> episodes;
    private int numEpisode;

    public EpisodeAdapter(Context context, Episodes episodes,int numEpisode){
        this.context = context;
        this.episodes = episodes.getEpisodes();
        this.numEpisode = numEpisode;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View episodeView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.episode_list_item,viewGroup,false);
        EpisodeViewHolder episodeViewHolder = new EpisodeViewHolder(episodeView);
        return episodeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder episodeViewHolder, int position) {
        int num = position + 1;
        episodeViewHolder.textViewNumber.setText("Episode "+num);
        episodeViewHolder.textViewName.setText(episodes.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return numEpisode;
    }

    public class EpisodeViewHolder extends RecyclerView.ViewHolder{
        TextView textViewNumber;
        TextView textViewName;

        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = (TextView) itemView.findViewById(R.id.episodeNumber);
            textViewName = (TextView) itemView.findViewById(R.id.episodeName);
        }


    }

}
