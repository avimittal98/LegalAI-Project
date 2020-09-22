package com.technical.legalai;

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

public class SignupActivity extends AppCompatActivity {
    FloatingActionButton create;
    EditText email,pass,confirm_pass;
    String email1,pass1,c_pass1;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        create=(FloatingActionButton) findViewById(R.id.cardView);
        email=(EditText)findViewById(R.id.editText);
        pass=(EditText)findViewById(R.id.editText3);
        confirm_pass=(EditText)findViewById(R.id.editText8);

        mAuth = FirebaseAuth.getInstance();

        email.setText("");
        pass.setText("");
        confirm_pass.setText("");
        final Context c=this;

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email1=email.getText().toString();
                pass1=pass.getText().toString();
                c_pass1=confirm_pass.getText().toString();
                if(pass1.equals(c_pass1)==true)
                {
                    mAuth.createUserWithEmailAndPassword(email1, pass1)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SignupActivity", "createUserWithEmail:success");
                                        Toast.makeText(SignupActivity.this, "Account Created",Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(c,collectinfo.class);
                                        startActivity(i);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignupActivity.this, task.getException().toString(),Toast.LENGTH_SHORT).show(); //Expand this as per exception
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(SignupActivity.this, "Both passwords do not match!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
