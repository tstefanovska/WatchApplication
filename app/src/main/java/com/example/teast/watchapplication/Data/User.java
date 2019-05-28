package com.example.teast.watchapplication.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

@Entity(tableName = "user_table")
public class User implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String userName;
    private String password;
    private int age;
    public ArrayList<SingleShow> toWatchList;
    public ArrayList<SingleShow> watchedList;
    public ArrayList<SingleShow> nowWatchingList;


    public User(String name, String userName, String password, int age){
        this. name = name;
        this.userName = userName;
        this.password = password;
        this.age = age;
        toWatchList = new ArrayList<>();
        watchedList = new ArrayList<>();
        nowWatchingList = new ArrayList<>();
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        userName = in.readString();
        password = in.readString();
        age = in.readInt();
        toWatchList = in.createTypedArrayList(SingleShow.CREATOR);
        watchedList = in.createTypedArrayList(SingleShow.CREATOR);
        nowWatchingList = in.createTypedArrayList(SingleShow.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getId() { return id; }

    public String getUserName() { return userName; }

    public String getPassword() { return password; }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<SingleShow> getToWatchList() {
        return toWatchList;
    }

    public void setToWatchList(ArrayList<SingleShow> toWatchList) {
        this.toWatchList = toWatchList;
    }

    public ArrayList<SingleShow> getWatchedList() {
        return watchedList;
    }

    public void setWatchedList(ArrayList<SingleShow> watchedList) {
        this.watchedList = watchedList;
    }

    public ArrayList<SingleShow> getNowWatchingList() {
        return nowWatchingList;
    }

    public void setNowWatchingList(ArrayList<SingleShow> nowWatchingList) {
        this.nowWatchingList = nowWatchingList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeInt(age);
        dest.writeTypedList(toWatchList);
        dest.writeTypedList(watchedList);
        dest.writeTypedList(nowWatchingList);
    }
}
