package com.technical.legalai;

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
import com.technical.legalai.R;
import com.technical.legalai.LawyerAdapter;


import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class lawyerlist extends AppCompatActivity {

    private static final String TAG  = "lawyerlist";
    //vars
    private ArrayList<Lawyer> mLawyer = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lawyerlist);

        Log.d(TAG, "onCreate:started.");
        ArrayList<Lawyer> myList = (ArrayList<Lawyer>) getIntent().getSerializableExtra("list");
        initLawyerDetails(myList);
        Toast.makeText(lawyerlist.this,"These are the Lawyers",Toast.LENGTH_LONG).show();


    }


    void initLawyerDetails(ArrayList<Lawyer> list){
        Log.d(TAG, "initlawyerBitmaps:preparing lawyer details");
        //Firebase result here
        mLawyer = list;
        initLawyerRecyclerView();

    }
    private LawyerAdapter initLawyerRecyclerView() {

        Log.d(TAG, "initLawyerRecyclerView:init recyclerview.");
        RecyclerView lawrecyclerView = findViewById(R.id.recyclerview);
        LawyerAdapter adapter = new LawyerAdapter(this, mLawyer);
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
                if (initLawyerRecyclerView() != null){
                    initLawyerRecyclerView().getFilter().filter(newText);
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }
}
