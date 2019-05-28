package com.example.teast.watchapplication;

import android.arch.persistence.room.TypeConverter;

import com.example.teast.watchapplication.Data.Result;
import com.example.teast.watchapplication.Data.SingleShow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public static ArrayList<SingleShow> fromString(String value){
        Type listType = new TypeToken<ArrayList<SingleShow>>(){}.getType();
        return new Gson().fromJson(value,listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<SingleShow> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
