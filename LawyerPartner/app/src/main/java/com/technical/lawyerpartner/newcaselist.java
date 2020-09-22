package com.technical.lawyerpartner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class newcaselist extends AppCompatActivity {
    private static final String TAG  = "newcaselist";
    //vars
    private ArrayList<User> mClient = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcaselist);

        Log.d(TAG, "onCreate:started.");
        ArrayList<User> myList = (ArrayList<User>) getIntent().getSerializableExtra("list");
        initUserDetails(myList);
        Toast.makeText(newcaselist.this,"These are the new clients",Toast.LENGTH_LONG).show();
    }
    void initUserDetails(ArrayList<User> list){
        Log.d(TAG, "initlawyerBitmaps:preparing lawyer details");
        //Firebase result here
        mClient = list;
        initUserRecyclerView();

    }
    private NewAdapter initUserRecyclerView() {

        Log.d(TAG, "initLawyerRecyclerView:init recyclerview.");
        RecyclerView lawrecyclerView = findViewById(R.id.newrecyclerview);
        NewAdapter adapter = new NewAdapter(this, mClient);
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
