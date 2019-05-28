package com.example.teast.watchapplication.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;
import java.util.Date;

public class MyEventDay extends EventDay{
    private String mNote;

    public MyEventDay(Calendar day, int imageResource, String note) {
        super(day, imageResource);
        mNote = note;
    }

    public String getNote() {
        return mNote;
    }





}
