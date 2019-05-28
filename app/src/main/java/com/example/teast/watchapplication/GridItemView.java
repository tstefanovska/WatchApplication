package com.example.teast.watchapplication;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GridItemView extends FrameLayout {
    private TextView textView;
    private ImageView imageView;
    public GridItemView(Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.popular_shows_item,this);
        textView =(TextView)getRootView().findViewById(R.id.showName);
        imageView = (ImageView)getRootView().findViewById(R.id.showImage);
    }
    public void display(String name,String imgUrl,boolean isSelected){
        textView.setText(name);
        Picasso.get().load(imgUrl).into(imageView);
        display(isSelected);
    }

    public void display(boolean isSelected) {
        textView.setTextColor(isSelected ? getResources().getColor(R.color.darkGray) : getResources().getColor(R.color.black));
        imageView.setImageAlpha(isSelected ? 127 : 255);
    }

}
