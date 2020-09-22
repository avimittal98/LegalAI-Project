package com.technical.lawyerpartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myprofile extends AppCompatActivity {
    private static final String TAG = "myprofile";
    Lawyer lawyer = new Lawyer();
    ImageView profile_pic;
    TextView name,phone,email,date_prac,won,add_info,charges,firm,ipc,address,courts,area,education,cases;
    LinearLayout remove;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        final Context c = this;
        profile_pic = (ImageView)findViewById(R.id.lawprofilepic);
        name = (TextView)findViewById(R.id.lawyername);
        phone = (TextView)findViewById(R.id.lawmob);
        email = (TextView)findViewById(R.id.lawemail);
        date_prac = (TextView)findViewById(R.id.lawpracyears);
        won = (TextView)findViewById(R.id.lawcasewon);
        add_info = (TextView)findViewById(R.id.additional);
        charges = (TextView)findViewById(R.id.charges);
        firm = (TextView)findViewById(R.id.lawfirm);
        ipc = (TextView)findViewById(R.id.ipclist);
        address = (TextView)findViewById(R.id.officeaddr);
        courts = (TextView)findViewById(R.id.courtsofprac);
        area = (TextView)findViewById(R.id.areaofprac);
        education = (TextView)findViewById(R.id.education);
        cases = (TextView)findViewById(R.id.casehandle);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        final DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("lawyers/"+currentUser.getUid());
        zonesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lawyer = dataSnapshot.getValue(Lawyer.class);
                name.setText(lawyer.lawyer_name);
                phone.setText(lawyer.phone);
                email.setText(lawyer.email);
                date_prac.setText(lawyer.dop);
                won.setText(lawyer.no_wins);
                add_info.setText(lawyer.more_info);
                charges.setText(lawyer.charges);
                firm.setText(lawyer.lawyer_firm);
                ipc.setText(lawyer.IPC);
                address.setText(lawyer.address);
                courts.setText(lawyer.courts_prac);
                area.setText(lawyer.areas_prac);
                education.setText(lawyer.education);
                cases.setText(lawyer.no_cases);
                Glide.with(myprofile.this)
                        .asBitmap()
                        .load(lawyer.photo)
                        .into(profile_pic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });


        LinearLayout editprofile = (LinearLayout)findViewById(R.id.editprofile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,lawyercollectinfo.class);
                startActivity(i);

            }
        });


    }
}
