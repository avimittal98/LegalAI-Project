package com.technical.legalai;

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

public class aboutlawyer extends AppCompatActivity {
    private static final String TAG = "aboutlawyer";
    Lawyer lawyer = new Lawyer();
    User user = new User();
    ImageView profile_pic;
    TextView name,phone,email,date_prac,won,add_info,charges,firm,ipc,address,courts,area,education,cases;
    LinearLayout add;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutlawyer);

        final Context c =this;

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

        Intent i = getIntent();
        String uid = i.getStringExtra("uid");
        final DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("lawyers/"+uid);
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
                Glide.with(aboutlawyer.this)
                        .asBitmap()
                        .load(lawyer.photo)
                        .into(profile_pic);
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:+91"+lawyer.phone));//change the number
                        startActivity(callIntent);
                    }
                });
                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{lawyer.email});
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(aboutlawyer.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        add = (LinearLayout)findViewById(R.id.layout_addlawyer);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference zonesRef1 = FirebaseDatabase.getInstance().getReference("users/"+currentUser.getUid());
                zonesRef1.child("request").setValue(lawyer.lawyer_UID);

                final DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("lawyers/"+lawyer.lawyer_UID);
                zonesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("new_case"))
                        {
                            String new_case = dataSnapshot.child("new_case").getValue().toString();
                            if(new_case == "")
                                new_case = currentUser.getUid();
                            else
                                new_case=new_case+";"+currentUser.getUid();
                            zonesRef.child("new_case").setValue(new_case);
                        }
                        else
                            zonesRef.child("new_case").setValue(currentUser.getUid());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "onCancelled", databaseError.toException());
                    }
                });

                Intent i = new Intent(c,DrawerActivity.class);
                startActivity(i);
            }
        });

    }
}
