package com.example.teast.watchapplication.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.teast.watchapplication.Adapters.TrendingShowAdapter;
import com.example.teast.watchapplication.Data.MyEventDay;
import com.example.teast.watchapplication.Data.SingleShow;
import com.example.teast.watchapplication.EventDecorator;
import com.example.teast.watchapplication.GetDataService;
import com.example.teast.watchapplication.Data.PopularShows;
import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.RecyclerTouchListener;
import com.example.teast.watchapplication.Data.Result;
import com.example.teast.watchapplication.RetrofitClientInstance;
import com.example.teast.watchapplication.Data.User;
import com.example.teast.watchapplication.RoomService;
import com.example.teast.watchapplication.SaveSharedPreference;
import com.example.teast.watchapplication.UserDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.teast.watchapplication.UserRepository.updateUser;


public class UserAreaActivity extends AppCompatActivity {
    private static final String CHANNEL_ID ="watch_ID";
    private static final int notification_id = 1;
    public static String API_KEY="f753674b0ff25f45fe8397f355b693bb";
    public static final String UPDATE_ACTION_KEY = "update_user_key";
    public static String LANGUAGE="en-US";
    TextView nameTv;
    RecyclerView recyclerView;
    TrendingShowAdapter showAdapter;
    UserDatabase userDatabase;
    boolean isSelected;
    List<Result> trendingShows;
    List<Result> watchingNow;
    List<SingleShow> trendingSingleShows;
    private MaterialCalendarView mCalendarView;
    private List<EventDay> mEventDays;
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;
    NavigationView mNavigationView;
    Button btnToWatch;
    Button btnNowWatching;
    Button btnWatched;
    User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        nameTv = (TextView) findViewById(R.id.name);
        btnToWatch = (Button) findViewById(R.id.buttonToWatch);
        btnNowWatching = (Button) findViewById(R.id.buttonWatchingNow);
        btnWatched = (Button) findViewById(R.id.buttonAlreadyWatched);
        mCalendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        isSelected = false;
        trendingSingleShows = new ArrayList<>();
        watchingNow = new ArrayList<>();
        mEventDays = new ArrayList<>();
        userDatabase = UserDatabase.getDatabase(getApplicationContext());
        Intent intent = getIntent();
        final User user = intent.getParcelableExtra("User");
        this.mUser = user;
        displayData(user);
        makeCall();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_user_area);
        setupToolbar();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.Open, R.string.Close);
        //addEvents();
        CalendarDay calendarDay = CalendarDay.from(Calendar.getInstance().getTime());
        List<CalendarDay> list = new ArrayList<>();
        list.add(calendarDay);
        mCalendarView.addDecorator(new EventDecorator(Color.RED,list));
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //NOTIFICATION CREATION

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_refresh_black_24dp)
                .setContentTitle("Watch Application")
                .setContentText("New episode!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notification_id, builder.build());


        //CALENDAR CREATION
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /* Date date = parseDate("2019-05-31");
        Calendar calendar = mCalendarView.getCurrentPageDate();
        calendar.setTime(date);
        EventDay eventDay = new EventDay(calendar,R.drawable.ic_add_box_black_24dp);
        List<EventDay> list = new ArrayList<>();
        list.add(eventDay);
        mCalendarView.setEvents(list);
        mCalendarView.showCurrentMonthPage();*/

        //NAVIGATION BAR
        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.account:
                        Intent accountIntent = new Intent(UserAreaActivity.this,AccountActivity.class);
                        accountIntent.putExtra("User",user);
                        startActivity(accountIntent);
                        break;
                    case R.id.settings:
                        Intent settingsIntent = new Intent(UserAreaActivity.this,SettingsActivity.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.mycart:
                        Toast.makeText(UserAreaActivity.this, "My Cart",Toast.LENGTH_SHORT).show();break;
                    case R.id.logout:
                        Intent intent = new Intent(UserAreaActivity.this, RoomService.class); //start Service to update User data
                        intent.setAction(UPDATE_ACTION_KEY); //use this action key in service
                        intent.putExtra("User",getUser());
                        startService(intent);

                        SaveSharedPreference.clearUserName(getApplicationContext(),user.getUserName());
                        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(loginIntent);
                        break;
                    default:
                        return true;
                }

                return true;

            }
        });

        //BUTTONS LISTS
        btnToWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intentToWatch = new Intent(UserAreaActivity.this,WatchListActivity.class);
             intentToWatch.putExtra("Type", "To-Watch List");
             intentToWatch.putExtra("List",getUser().toWatchList);
             startActivity(intentToWatch);
            }
        });

        btnNowWatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToWatch = new Intent(UserAreaActivity.this,WatchListActivity.class);
                intentToWatch.putExtra("Type", "Now-Watching List");
                intentToWatch.putExtra("List",getUser().nowWatchingList);
                startActivity(intentToWatch);
            }
        });
        btnWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToWatch = new Intent(UserAreaActivity.this,WatchListActivity.class);
                intentToWatch.putExtra("Type", "Watched List");
                intentToWatch.putExtra("List", getUser().watchedList);
                startActivity(intentToWatch);
            }
        });


    }

    @Override
    protected void onStop() {
        Intent intent = new Intent(UserAreaActivity.this, RoomService.class); //start Service to update User data
        intent.setAction(UPDATE_ACTION_KEY); //use this action key in service
        intent.putExtra("User",getUser());
        startService(intent);
        super.onStop();
    }



    @Override
    protected void onDestroy(){
        Intent intent = new Intent(UserAreaActivity.this, RoomService.class); //start Service to update User data
        intent.setAction(UPDATE_ACTION_KEY); //use this action key in service
        intent.putExtra("User",getUser());
        startService(intent);
        super.onDestroy();
    }


    private void makeCall() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<PopularShows> call = service.getTrendingShows(API_KEY);
        call.enqueue(new Callback<PopularShows>() {
            @Override
            public void onResponse(Call<PopularShows> call, Response<PopularShows> response) {
                generateShowsList(response.body());
            }

            @Override
            public void onFailure(Call<PopularShows> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(UserAreaActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(UserAreaActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                // Toast.makeText(RegisterShowsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateShowsList(PopularShows popularShows) {
        //GENERATE TRENDING SHOWS LIST
        this.trendingShows = popularShows.getResults();
        for (Result show:trendingShows) {
            getSingleShow(show);
        }
        Intent intent = getIntent();
        final User user = getUser();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewHorizontal);
        showAdapter = new TrendingShowAdapter(trendingShows,getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(UserAreaActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(showAdapter);
        registerForContextMenu(recyclerView);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), horizontalLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
              if(!showAdapter.isClicked.get(position)){
                  showAdapter.isClicked.set(position,true);
                  getUser().toWatchList.add(trendingSingleShows.get(position));
                  setUser(user);

              }else{
                  showAdapter.isClicked.set(position,false);
                  getUser().toWatchList.remove(trendingSingleShows.get(position));
                  setUser(user);
              }
            }
           // Make a list of To-Watch, Watching and Want-To-Watch
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void getSingleShow(Result show) {
        //GET SINGLE SHOW OBJECT
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<SingleShow> call = service.getSingleShow(show.getId(),API_KEY,LANGUAGE);
        call.enqueue(new Callback<SingleShow>() {
            @Override
            public void onResponse(Call<SingleShow> call, Response<SingleShow> response) {
                addSingleShow(response.body());
            }

            @Override
            public void onFailure(Call<SingleShow> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(UserAreaActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(UserAreaActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
            }
        });
    }

    private void addSingleShow(SingleShow show) {

        trendingSingleShows.add(show);
    }

    private void setUser(User user) {
        this.mUser = user;
    }

    public void displayData(User user){

        nameTv.setText(String.format("%s %s", getString(R.string.welcome), user.getName()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //CREATE SEARCH MENU
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_user_area,menu);

        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchableActivity.class)));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //ON SELECTED ITEM FROM TRENDING LIST (ADD TO TO-WATCH LIST)
        RecyclerView recyclerView = getRecyclerView();
        List<Result> trendingShows = getTrendingShows();
        Intent intent = getIntent();
        User user = getUser();

        int position = -1;
        try {
            position = ((TrendingShowAdapter)recyclerView.getAdapter()).getPosition();
        } catch (Exception e) {

            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.toWatchList:
                Toast.makeText(this, trendingShows.get(position).getName()+" is added to your To-Watch-list!", Toast.LENGTH_SHORT).show();
                user.toWatchList.add(trendingSingleShows.get(position));
                setUser(user);
                break;
            case R.id.toWatchedList:
                Toast.makeText(this, trendingShows.get(position).getName() + " is added to your Watched-list!", Toast.LENGTH_SHORT).show();
                user.watchedList.add(trendingSingleShows.get(position));
                setUser(user);
                break;
            case R.id.toWatchingList:
                Toast.makeText(this,trendingShows.get(position).getName() + " is added to your Now-Watching-list!", Toast.LENGTH_SHORT).show();
                watchingNow.add(trendingShows.get(position));
                user.nowWatchingList.add(trendingSingleShows.get(position));
                setUser(user);
                break;
        }
        return super.onContextItemSelected(item);
    }
    public RecyclerView getRecyclerView(){return recyclerView;}
    public List<Result> getTrendingShows() {return trendingShows;}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private User getUser(){
        return mUser;
    }


   /* private void addEvents() {
        for (SingleShow show:getUser().nowWatchingList) {
            if(show.getNextEpisodeToAir()!=null){
                Date myDate = parseDate(show.getNextEpisodeToAir().getAirDate());
                Calendar calendar= Calendar.getInstance();
                calendar.setTime(myDate);
                MyEventDay myEventDay = new MyEventDay(calendar,R.drawable.ic_notifications_black_24dp,"String");
                mEventDays.add(myEventDay);
            }
        }
        mCalendarView.setEvents(mEventDays);

    }*/
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}