package com.technical.legalai;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.technical.legalai.R;
import com.technical.legalai.PoliceStationAdapter;


import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private static final String TAG  = "FullscreenActivity";
    //vars
    private ArrayList<PoliceList> mPolice = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_fullscreen);
        Log.d(TAG, "onCreate:started.");
        initPoliceList();
        Toast.makeText(FullscreenActivity.this,"Please tap on the Navigate Button To Navigate",Toast.LENGTH_LONG).show();


    }


    private void initPoliceList(){
        Log.d(TAG, "Police List Setting up");
        PoliceList a = new PoliceList("Bandra Police Station","577, SV Rd, Bandra West, Mumbai, Maharashtra 400050","google.navigation:q=19.054565,72.837955");
        mPolice.add(a);

        PoliceList b = new PoliceList("Khar Police Station","78, SV Rd, Khar, Ram Krishna Nagar, Khar West, Mumbai, Maharashtra 400052","google.navigation:q=19.072652,72.838267");
        mPolice.add(b);

        PoliceList c = new PoliceList("Santacruz Police Station","Juhu Tara Road, Linking Rd, BEST Colony, Santacruz West, Mumbai, Maharashtra 400054","google.navigation:q=19.084253,72.834841");
        mPolice.add(c);

        PoliceList d = new PoliceList("Vile Parle Police Station","Navpada, Vile Parle East, Vile Parle, Mumbai, Maharashtra 400099","google.navigation:q=19.096151,72.855211");
        mPolice.add(d);

        PoliceList e = new PoliceList("Juhu Police Station","V M Road, Near Kalaniketan, Vile Parle West, Yamuna Nagar, Nehru Nagar, Juhu, Mumbai, Maharashtra 400056","google.navigation:q=19.103172,72.832707");
        mPolice.add(e);

        PoliceList f = new PoliceList("Andheri Police Station","Sahar Road, Opp. Andheri Railway Station, Vertex Junction, Railway Colony, Andheri East, Mumbai, Maharashtra 400053","google.navigation:q=19.120527,72.848241");
        mPolice.add(f);

        PoliceList g = new PoliceList("Jogeshwari Police Station","Swami compound,Caves Road,Jogeshwari east, Mumbai, Maharashtra 400060","google.navigation:q=19.138112,72.854296");
        mPolice.add(g);

        PoliceList h = new PoliceList("Kandivali Police Station","Parekh Nagar Shatabdi Hospital, SV Rd, opp. Kandivali, Mumbai, Maharashtra 400067","google.navigation:q=19.209597,72.850172");
        mPolice.add(h);

        initRecyclerView();

    }
    private PoliceStationAdapter initRecyclerView() {

        Log.d(TAG, "initRecyclerView:init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        PoliceStationAdapter adapter = new PoliceStationAdapter(this, mPolice);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                    if (initRecyclerView() != null){
                        initRecyclerView().getFilter().filter(newText);
                    }

                    return false;
                }
            });

            return super.onCreateOptionsMenu(menu);


    }
}
