package com.technical.lawyerpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class myinfo extends AppCompatActivity {
    ImageView profile_pic;
    private FirebaseAuth mAuth;
    CardView new_btn,total_btn,logoutbtn;
    TextView new_count,total_count;
    FloatingActionButton myprof;
    String n_case[], t_case[];
    private static final String TAG = "MyInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        profile_pic = (ImageView)findViewById(R.id.profpiclaw);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        new_btn = (CardView)findViewById(R.id.newcasecard);
        total_btn = (CardView)findViewById(R.id.totalcard);
        myprof = (FloatingActionButton)findViewById(R.id.galleryBtn);
        new_count = (TextView)findViewById(R.id.numnewcase);
        total_count = (TextView)findViewById(R.id.numtotal);
        logoutbtn = (CardView)findViewById(R.id.logoutbtn);

        final DatabaseReference new_cases = FirebaseDatabase.getInstance().getReference("lawyers/"+currentUser.getUid());
        new_cases.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("new_case"))
                {
                    String n_cases = dataSnapshot.child("new_case").getValue().toString();
                    n_case = n_cases.split(";");
                    new_count.setText(Integer.toString(n_case.length));
                    new_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(n_case.length!=0)
                            {

                                final DatabaseReference client_list1 = FirebaseDatabase.getInstance().getReference("users/");
                                final ArrayList<User> client_list = new ArrayList<>();
                                client_list1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(String uid: n_case)
                                        {
                                            User u = dataSnapshot.child(uid).getValue(User.class);
                                            client_list.add(u);
                                        }
                                        Intent i = new Intent(myinfo.this, newcaselist.class);
                                        i.putExtra("list", client_list);
                                        startActivity(i);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w(TAG, "onCancelled", databaseError.toException());
                                    }
                                });

                            }
                            else
                                Toast.makeText(myinfo.this,"You have no new cases!",Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //do nothing
            }
        });

        final DatabaseReference total_cases = FirebaseDatabase.getInstance().getReference("lawyers/"+currentUser.getUid());
        total_cases.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("total_case"))
                {
                    String n_cases = dataSnapshot.child("total_case").getValue().toString();
                    t_case= n_cases.split(";");
                    total_count.setText(Integer.toString(t_case.length));
                    total_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(t_case.length!=0)
                            {
                                final DatabaseReference client_list1 = FirebaseDatabase.getInstance().getReference("users/");
                                final ArrayList<User> client_list = new ArrayList<>();
                                client_list1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(String uid: t_case)
                                        {
                                            User u = dataSnapshot.child(uid).getValue(User.class);
                                            client_list.add(u);
                                        }
                                        Intent i = new Intent(myinfo.this,totalcaselist.class);
                                        i.putExtra("list",client_list);
                                        startActivity(i);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w(TAG, "onCancelled", databaseError.toException());
                                    }
                                });
                                Intent i = new Intent(myinfo.this,totalcaselist.class);
                                i.putExtra("list",client_list);
                                startActivity(i);
                            }
                            else
                                Toast.makeText(myinfo.this,"You have no cases!",Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //do nothing
            }
        });



        Glide.with(this /* context */)
                .load(currentUser.getPhotoUrl())
                .into(profile_pic);

        myprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(myinfo.this,myprofile.class);
                startActivity(i);
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(myinfo.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

    }

}
