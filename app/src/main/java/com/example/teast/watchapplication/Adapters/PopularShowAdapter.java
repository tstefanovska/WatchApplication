package com.example.teast.watchapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teast.watchapplication.GridItemView;
import com.example.teast.watchapplication.Data.Result;

import java.util.ArrayList;
import java.util.List;

public class PopularShowAdapter extends BaseAdapter {
    public String IMAGE_BASE_URL="https://image.tmdb.org/t/p/w300";
    private  Context mContext;
    private  List<Result> results;
    public List<Integer> selectedPositions;
    LayoutInflater layoutInflater;

    // 1
    public PopularShowAdapter(Context context, List<Result> results) {
        this.mContext = context;
        this.results= results;
        this.selectedPositions = new ArrayList<>();
        layoutInflater = (LayoutInflater.from(context));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        ImageView imageView;
        GridItemView gridView;
        String imgUrl=IMAGE_BASE_URL+results.get(position).getPosterPath();
        if (convertView == null) {
            /*gridView  = new View(mContext);
            gridView=layoutInflater.inflate(R.layout.popular_shows_item,null);
            textView= (TextView)gridView.findViewById(R.id.showName);
             imageView= (ImageView)gridView.findViewById(R.id.showImage);

            String imgUrl=IMAGE_BASE_URL+results.get(position).getPosterPath();
            Picasso.get().load(imgUrl).into(imageView);
            textView.setText(results.get(position).getName());*/
            gridView = new GridItemView(mContext);

        }
        else
        {
            gridView = (GridItemView) convertView;
        }
         gridView.display(results.get(position).getName(),imgUrl,selectedPositions.contains(position));
        return gridView;
    }
    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
