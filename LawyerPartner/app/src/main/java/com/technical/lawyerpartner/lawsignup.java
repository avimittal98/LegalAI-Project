package com.technical.lawyerpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class lawsignup extends AppCompatActivity {
    FloatingActionButton lawcreate;
    EditText lawemail,lawpass,lawconfirm_pass;
    String lawemail1,lawpass1,lawc_pass1;

    private FirebaseAuth lawmAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawsignup);

        lawcreate=(FloatingActionButton)findViewById(R.id.cardView);
        lawemail=(EditText)findViewById(R.id.editText);
        lawpass=(EditText)findViewById(R.id.editText3);
        lawconfirm_pass=(EditText)findViewById(R.id.editText8);

        lawmAuth = FirebaseAuth.getInstance();

        lawemail.setText("");
        lawpass.setText("");
        lawconfirm_pass.setText("");
        final Context c=this;

        lawcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lawemail1=lawemail.getText().toString();
                lawpass1=lawpass.getText().toString();
                lawc_pass1=lawconfirm_pass.getText().toString();
                if(lawpass1.equals(lawc_pass1)==true)
                {
                    lawmAuth.createUserWithEmailAndPassword(lawemail1, lawpass1)
                            .addOnCompleteListener(lawsignup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SignupActivity", "createUserWithEmail:success");
                                        Toast.makeText(lawsignup.this, "Account Created",Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(c,lawyercollectinfo.class);
                                        startActivity(i);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("createUserWithEmail:failure", task.getException());
                                        Toast.makeText(lawsignup.this, task.getException().toString(),Toast.LENGTH_SHORT).show(); //Expand this as per exception
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(lawsignup.this, "Both passwords do not match!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}

