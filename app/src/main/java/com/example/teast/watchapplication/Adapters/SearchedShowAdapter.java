package com.example.teast.watchapplication.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.Data.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchedShowAdapter extends RecyclerView.Adapter<SearchedShowAdapter.ShowViewHolder> {
    public String IMAGE_BASE_URL="https://image.tmdb.org/t/p/w342";
    List<Result> results;
    View.OnClickListener mClickListener;


     public SearchedShowAdapter(List<Result> results){
         this.results = results;
     }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_searched_item, viewGroup,false);
        ShowViewHolder showViewHolder = new ShowViewHolder(view);
        showViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(view);
            }
        });
        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShowViewHolder showViewHolder, final int position) {

        String imgUrl=IMAGE_BASE_URL+results.get(position).getPosterPath();
        Picasso.get().load(imgUrl).into(showViewHolder.imageView);
        showViewHolder.textViewName.setText(results.get(position).getName());
        showViewHolder.textViewAbout.setText(results.get(position).getOverview());
        showViewHolder.btnSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showViewHolder.btnSeeMore.getText().equals("See more")) {
                    showViewHolder.textViewAbout.setMaxLines(Integer.MAX_VALUE);
                    showViewHolder.textViewAbout.setText(results.get(showViewHolder.getAdapterPosition()).getOverview());
                    showViewHolder.btnSeeMore.setText("See less");
                }else{
                    showViewHolder.textViewAbout.setMaxLines(10);
                    showViewHolder.textViewAbout.setText(results.get(showViewHolder.getAdapterPosition()).getOverview());
                    showViewHolder.btnSeeMore.setText("See more");

                }
            }
        });
        if(showViewHolder.textViewAbout.getLineCount() > 10){
            showViewHolder.btnSeeMore.setVisibility(View.VISIBLE);
            showViewHolder.btnSeeMore.setClickable(true);
        }else{
            showViewHolder.btnSeeMore.setVisibility(View.INVISIBLE);
            showViewHolder.btnSeeMore.setClickable(false);
        }


    }

    @Override
    public int getItemCount() {
        return results.size();
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ShowViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textViewName;
        TextView textViewAbout;
        ImageView imageView;
        Button btnSeeMore;

        public ShowViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            textViewName = (TextView) itemView.findViewById(R.id.nameSearched);
            textViewAbout = (TextView) itemView.findViewById(R.id.aboutShow);
            imageView = (ImageView) itemView.findViewById(R.id.imageSearched);
            btnSeeMore = (Button) itemView.findViewById(R.id.see_more_btn);
        }
    }

    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }



}
