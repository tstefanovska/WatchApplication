package com.example.teast.watchapplication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import com.example.teast.watchapplication.Adapters.PopularShowAdapter;
import com.example.teast.watchapplication.GetDataService;
import com.example.teast.watchapplication.GridItemView;
import com.example.teast.watchapplication.Data.PopularShows;
import com.example.teast.watchapplication.R;
import com.example.teast.watchapplication.Data.Result;
import com.example.teast.watchapplication.RetrofitClientInstance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterShowsActivity extends AppCompatActivity {
    public static String API_KEY="f753674b0ff25f45fe8397f355b693bb";
    public static String LANGUAGE="en-US";
    GridView gridView;
    List<Result> mList;
    ArrayList<Result> selectedItems;
    Button btnSkip;
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shows);
        btnSkip = (Button)findViewById(R.id.skip_btn);
        btnContinue = (Button)findViewById(R.id.continue_btn);
        makeCall();
    }

    private void makeCall() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<PopularShows> call = service.getAllShows(API_KEY,LANGUAGE,1);
        call.enqueue(new Callback<PopularShows>() {
            @Override
            public void onResponse(Call<PopularShows> call, Response<PopularShows> response) {
                generateShowsList(response.body());
            }

            @Override
            public void onFailure(Call<PopularShows> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(RegisterShowsActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(RegisterShowsActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                // Toast.makeText(RegisterShowsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateShowsList(PopularShows body) {
        mList = body.getResults();
        selectedItems = new ArrayList<>();

        gridView = (GridView)findViewById(R.id.gridView);

        final PopularShowAdapter showsAdapter = new PopularShowAdapter(this, mList);
        gridView.setAdapter(showsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedIndex = showsAdapter.selectedPositions.indexOf(position);
                if(selectedIndex > -1){
                 showsAdapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView)view).display(false);
                    selectedItems.remove((Result)parent.getItemAtPosition(position));
                }else{
                    showsAdapter.selectedPositions.add(position);
                    ((GridItemView) view).display(true);
                    Object result = parent.getItemAtPosition(position);
                    selectedItems.add((Result)parent.getItemAtPosition(position));

                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = false;
                Intent userAreaIntent = new Intent(RegisterShowsActivity.this,UserAreaActivity.class);
                userAreaIntent.putExtra("isSelected",selected);
                RegisterShowsActivity.this.startActivity(userAreaIntent);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = true;
                Intent userAreaIntent = new Intent(RegisterShowsActivity.this, UserAreaActivity.class);
                userAreaIntent.putParcelableArrayListExtra("SelectedItems", selectedItems);
                userAreaIntent.putExtra("isSelected",selected);
                RegisterShowsActivity.this.startActivity(userAreaIntent);
            }
        });
    }
   // The Skip button is clicked
    /*public void onClickSkip(View view) {
        boolean selected = false;
        Intent userAreaIntent = new Intent(RegisterShowsActivity.this,UserAreaActivity.class);
        userAreaIntent.putExtra("isSelected",selected);
        RegisterShowsActivity.this.startActivity(userAreaIntent);
    }
    // The Continue button is clicked
    public void onClickContinue(View view) {
        boolean selected = true;
        Intent userAreaIntent = new Intent(RegisterShowsActivity.this, UserAreaActivity.class);
        userAreaIntent.putExtra("SelectedItems", (Serializable) selectedItems);
        userAreaIntent.putExtra("isSelected",selected);
        RegisterShowsActivity.this.startActivity(userAreaIntent);
    }*/
}
