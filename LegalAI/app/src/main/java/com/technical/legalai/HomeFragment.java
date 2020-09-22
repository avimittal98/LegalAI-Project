package com.technical.legalai;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private static final String TAG  = "HomeFragment";
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);
        CardView searchnamelawyer = (CardView) view.findViewById(R.id.lawsearchcard);
        CardView searchbyipc = (CardView) view.findViewById(R.id.ipcsearchcard);
        CardView viewcasedocs = (CardView) view.findViewById(R.id.casedoccard);
        CardView viewmylawyer = (CardView) view.findViewById(R.id.mylawyercard);
        FloatingActionButton helpcall = view.findViewById(R.id.helpcallbutton);
        final DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("lawyers");
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        searchnamelawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList <Lawyer> lawyer_list = new ArrayList<>();
                zonesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {
                            Lawyer l = zoneSnapshot.getValue(Lawyer.class);
                            lawyer_list.add(l);
                        }
                        Intent i = new Intent(getContext(),lawyerlist.class);
                        i.putExtra("list",lawyer_list);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "onCancelled", databaseError.toException());
                    }
                });

            }
        });

        searchbyipc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),provideipc.class);
                startActivity(i);

            }
        });

        viewcasedocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(),UploadFile.class);
                startActivity(i);
            }
        });

        viewmylawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference lawyer1 = FirebaseDatabase.getInstance().getReference("users/"+currentUser.getUid());
                lawyer1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("lawyer")&&(dataSnapshot.child("lawyer").getValue()!=""))
                        {
                            Intent i = new Intent(getContext(),mylawyer.class);
                            String uid = dataSnapshot.child("lawyer").getValue().toString();
                            i.putExtra("uid",uid);
                            startActivity(i);
                        }
                        else
                            Toast.makeText(getContext(),"You have no lawyer assigned to you!",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "onCancelled", databaseError.toException());
                    }
                });

                final DatabaseReference request1 = FirebaseDatabase.getInstance().getReference("users/"+currentUser.getUid());
                request1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("request")&&(dataSnapshot.child("request").getValue()!=""))
                        {
                            //Toast.makeText(getContext(),"You have requested for a lawyer! \nOnce the lawyer accepts, it will appear",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getContext(),"You have no lawyer assigned to you!",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "onCancelled", databaseError.toException());
                    }
                });


            }
        });

        helpcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+918108820002"));//change the number
                startActivity(callIntent);
            }
        });


        return view;

    }

}





