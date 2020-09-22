package com.technical.lawyerpartner;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technical.lawyerpartner.R;
import com.technical.lawyerpartner.TotalAdapter;
import com.technical.lawyerpartner.User;


import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class totalcaselist extends AppCompatActivity {

    private static final String TAG  = "totalcaselist";
    //vars
    private ArrayList<User> mClient = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_totalcaselist);
        Log.d(TAG, "onCreate:started.");
        ArrayList<User> myList = (ArrayList<User>) getIntent().getSerializableExtra("list");
        initUserDetails(myList);
        Toast.makeText(totalcaselist.this,"These are the Users",Toast.LENGTH_LONG).show();


    }


    void initUserDetails(ArrayList<User> list){
        Log.d(TAG, "initUserBitmaps:preparing User details");
        //Firebase result here
        mClient = list;
        initUserRecyclerView();

    }
    private TotalAdapter initUserRecyclerView() {

        Log.d(TAG, "initUserRecyclerView:init recyclerview.");
        RecyclerView lawrecyclerView = findViewById(R.id.recyclerview);
        TotalAdapter adapter = new TotalAdapter(this, mClient);
        lawrecyclerView.setAdapter(adapter);
        lawrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "oncreateOptionMenu:started.");
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (initUserRecyclerView() != null){
                    initUserRecyclerView().getFilter().filter(newText);
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, myinfo.class));
    }
}
