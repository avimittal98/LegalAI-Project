package com.technical.legalai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private TextView profmDisplayDate,profemail;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private TextView profname,profphone,profcity;
    FloatingActionButton edit;
    ImageView profdisplay;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        profmDisplayDate = (TextView)view.findViewById(R.id.txtvprofdob);
        profemail = (TextView)view.findViewById(R.id.txtvprofmail);
        profname =(TextView) view.findViewById(R.id.txtvprofilename);
        profphone =(TextView) view.findViewById(R.id.txtvprofmob);
        profcity = (TextView)view.findViewById(R.id.txtvprofcity);
        edit = (FloatingActionButton)view.findViewById(R.id.editbtn);
        profdisplay = (ImageView)view.findViewById(R.id.profpic);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        final DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("users/"+currentUser.getUid());
        zonesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                profname.setText(user.username);
                profphone.setText(user.phone);
                profemail.setText(user.email);
                profmDisplayDate.setText(user.dob);
                profcity.setText(user.city);
                Glide.with(ProfileFragment.this)
                        .asBitmap()
                        .load(currentUser.getPhotoUrl().toString())
                        .into(profdisplay);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(),collectinfo.class);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        return view;
    }
}
