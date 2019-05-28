package com.example.teast.watchapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teast.watchapplication.Data.MenuModel;
import com.example.teast.watchapplication.R;

public class DrawerItemCustomAdapter extends ArrayAdapter<MenuModel> {

    Context mContext;
    int layoutResourceId;
    MenuModel data[] = null;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, MenuModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.drawerIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.drawerName);

        MenuModel folder = data[position];


        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        return listItem;
    }
}
