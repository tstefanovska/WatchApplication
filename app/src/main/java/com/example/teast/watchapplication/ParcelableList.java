package com.example.teast.watchapplication;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.teast.watchapplication.Data.Result;

import java.util.ArrayList;

public class ParcelableList implements Parcelable{
    ArrayList<Result> selectedItems;
    String name;

    public ParcelableList(ArrayList<Result> selectedItems,String name){
        this.selectedItems = selectedItems;
        this.name = name;
    }

    public ParcelableList(Parcel in) {
       //selectedItems = in.readArrayList(null);
       name=in.readString();
    }

    public static final Creator<ParcelableList> CREATOR = new Creator<ParcelableList>() {
        @Override
        public ParcelableList createFromParcel(Parcel in) {
            return new ParcelableList(in);
        }

        @Override
        public ParcelableList[] newArray(int size) {
            return new ParcelableList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(selectedItems);
        dest.writeString(name);
    }
   public ArrayList<Result> getSelectedItems() {
        return selectedItems;
    }

}
