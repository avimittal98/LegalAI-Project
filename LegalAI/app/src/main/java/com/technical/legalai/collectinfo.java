package com.technical.legalai;


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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;


public class collectinfo extends AppCompatActivity {

    private static final String TAG = "collectinfo";
    private TextView mDisplayDate,email;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    EditText name,phone,city;
    String name1,phone1,city1,date;
    FloatingActionButton complete;
    ImageView display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectinfo);
        mDisplayDate = (TextView) findViewById(R.id.editText34); //Actually TextView not editText
        email = (TextView)findViewById(R.id.editText33); //Actually TextView not editText
        name =(EditText)findViewById(R.id.editText31);
        phone =(EditText)findViewById(R.id.editText32);
        city = (EditText)findViewById(R.id.editText35);
        complete = (FloatingActionButton) findViewById(R.id.cardView);
        display = (ImageView)findViewById(R.id.defaultpic);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        email.setText(currentUser.getEmail());
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        collectinfo.this,
                        android.R.style.Theme_DeviceDefault_Dialog_Alert,
                        onDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        if(currentUser.getDisplayName()!=null)
            name.setText(currentUser.getDisplayName());
        if(currentUser.getPhotoUrl()!=null)
        {
            //System.out.println(currentUser.getPhotoUrl());
            Glide.with(this /* context */)
                    .load(currentUser.getPhotoUrl())
                    .into(display);
        }
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year);

                date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);

            }
        };
        final Context c = this;
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1 = name.getText().toString();
                phone1 = phone.getText().toString();
                city1 = city.getText().toString();
                if((!name1.equals(""))&&(!phone1.equals(""))&&(phone1.length()==10)&&(!city1.equals(""))&&(currentUser.getPhotoUrl()!=null))
                {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name1)
                            .build();
                    currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                }
                            });
                    if(!currentUser.isEmailVerified())
                        currentUser.sendEmailVerification();
                }
                User user = new User(name1,currentUser.getEmail(),phone1,city1,date,currentUser.getUid(),currentUser.getPhotoUrl().toString());
                database.child("users").child(currentUser.getUid()).setValue(user);
                Intent i = new Intent(c,DrawerActivity.class);
                startActivity(i);
            }
        });
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,getimage.class);
                startActivity(i);
            }
        });
    }
}
