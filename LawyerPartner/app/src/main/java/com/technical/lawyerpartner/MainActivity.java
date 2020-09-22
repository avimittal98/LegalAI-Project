package com.technical.lawyerpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    EditText email,pass;
    FloatingActionButton signin;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register=(TextView)findViewById(R.id.textView14);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();



        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);
        email.setText("");
        pass.setText("");
        signin = (FloatingActionButton)findViewById(R.id.email_sign_in_button);

        final Context c=this;

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
                Intent i=new Intent(c,lawsignup.class);
                startActivity(i);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //Toast.makeText(c,user.getDisplayName()+"\n"+user.getPhotoUrl(),Toast.LENGTH_SHORT).show();
                                    boolean emailVerified = user.isEmailVerified();
                                    if(emailVerified) {
                                        Intent i = new Intent(c, myinfo.class);
                                        startActivity(i);
                                    }
                                    if(user.getPhotoUrl()==null)
                                    {
                                        Intent i = new Intent(c,lawyercollectinfo.class);
                                        startActivity(i);
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, task.getException().toString(),
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });

    }
}
