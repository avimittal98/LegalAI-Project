package com.technical.lawyerpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class lawyercollectinfo extends AppCompatActivity {

    private static final String TAG = "lawyercollectinfo";
    EditText name,phone,address,education,courts,area,charges,firm,cases,wins,ipc,more_info;
    String lawyer_name,phone1,dop1,address1,education1,courts1,area1,charges1,firm1,cases1,wins1,ipc1,more_info1;
    FloatingActionButton complete;
    private TextView mDisplayDate,email;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    ImageView lawyer_pic;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyercollectinfo);
        complete = (FloatingActionButton) findViewById(R.id.cardView800);
        name = (EditText)findViewById(R.id.editText31);
        phone = (EditText)findViewById(R.id.editText32);
        email = (TextView)findViewById(R.id.editText33);
        mDisplayDate = (TextView)findViewById(R.id.editText34);
        address = (EditText)findViewById(R.id.editText35);
        education = (EditText)findViewById(R.id.editText800);
        courts = (EditText)findViewById(R.id.editText801);
        area = (EditText)findViewById(R.id.editText802);
        charges = (EditText)findViewById(R.id.editText803);
        firm = (EditText)findViewById(R.id.editText804);
        cases = (EditText)findViewById(R.id.editText805);
        wins = (EditText)findViewById(R.id.editText806);
        ipc = (EditText)findViewById(R.id.editText807);
        more_info = (EditText)findViewById(R.id.editText808);
        lawyer_pic = (ImageView)findViewById(R.id.lawpic1);

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
                        lawyercollectinfo.this,
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
                    .into(lawyer_pic);
        }
        final Context c = this;
        lawyer_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,lawyergetimage.class); //Add code to upload file pic here
                startActivity(i);
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lawyer_name = name.getText().toString();
                phone1 = phone.getText().toString();
                address1 = address.getText().toString();
                education1 = education.getText().toString();
                courts1 = courts.getText().toString();
                area1 = area.getText().toString();
                charges1 = charges.getText().toString();
                firm1 = firm.getText().toString();
                cases1 = cases.getText().toString();
                wins1 = wins.getText().toString();
                ipc1 = ipc.getText().toString();
                more_info1 = more_info.getText().toString();
                dop1 = mDisplayDate.getText().toString();

                if(currentUser.getPhotoUrl()==null)
                    Toast.makeText(c, "Please upload a photo", Toast.LENGTH_SHORT).show();

                if((!lawyer_name.equals(""))&&(!phone1.equals(""))&&(phone1.length()==10)&&(!address1.equals(""))&&(!education1.equals(""))&&(!courts1.equals(""))&&(!area1.equals(""))&&(!charges1.equals(""))&&(!cases1.equals(""))&&(!wins1.equals(""))&&(!ipc1.equals(""))&&(!more_info1.equals(""))&&(currentUser.getPhotoUrl()!=null))
                {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(lawyer_name)
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
                    Lawyer lawyer = new Lawyer(lawyer_name,currentUser.getEmail(),dop1,phone1,address1,education1,courts1,area1,charges1,firm1,cases1,wins1,ipc1,more_info1,currentUser.getUid(),currentUser.getPhotoUrl().toString());
                    database.child("lawyers").child(currentUser.getUid()).setValue(lawyer);

                    if(!currentUser.isEmailVerified())
                        currentUser.sendEmailVerification();

                    Intent i = new Intent(c,myinfo.class);
                    startActivity(i);
                }
                else
                    Toast.makeText(c,"Please enter all information!",Toast.LENGTH_LONG).show();

            }
        });

    }
}
