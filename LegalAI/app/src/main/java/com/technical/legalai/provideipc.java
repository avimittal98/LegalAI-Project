package com.technical.legalai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class provideipc extends AppCompatActivity {
    String story1, IPC1;
    EditText story;
    TextView ipc1;
    int activity_code;
    FloatingActionButton getres,getlawyer;
    private static final String TAG = "ProvideIpc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provideipc);


        ipc1 = (TextView)findViewById(R.id.ipcsection);
        story = (EditText)findViewById(R.id.editstory);
        getres = (FloatingActionButton)findViewById(R.id.getipcBtn);
        getlawyer = (FloatingActionButton)findViewById(R.id.getrelevantlawyer);
        OkHttpClient client = new OkHttpClient();
        getres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                story1 = story.getText().toString();
                if(story1.equals(""))
                    Toast.makeText(provideipc.this, "Please enter the case story!", Toast.LENGTH_SHORT).show();
                else
                {
                    RequestBody postBody = new FormBody.Builder()
                            .add("story", story1)
                            .build();
                    postRequest("http://192.168.1.208:5000/findipc/",postBody);
                }
            }
        });



    }
    void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(provideipc.this,"Failure to connect to server",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IPC1 = response.body().string();
                            ipc1.setText(IPC1);
                            if(IPC1 != "None")
                            {
                                final String IPC_list[] = IPC1.split(",");
                                final DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("lawyers");
                                final ArrayList<Lawyer> lawyer_list = new ArrayList<>();
                                zonesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {
                                            Lawyer l = zoneSnapshot.getValue(Lawyer.class);
                                            for(String ipc : IPC_list)
                                            {
                                                if(l.IPC.contains(ipc))
                                                {
                                                    lawyer_list.add(l);
                                                    break;
                                                }
                                            }

                                        }

                                getlawyer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(lawyer_list.size()!=0) {
                                            Intent i = new Intent(provideipc.this, lawyerlist.class);
                                            i.putExtra("list", lawyer_list);
                                            startActivity(i);
                                        }
                                        else
                                            Toast.makeText(provideipc.this,"No relevant lawyers found!",Toast.LENGTH_SHORT).show();

                                    }
                                });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w(TAG, "onCancelled", databaseError.toException());
                                    }
                                });
                            }
                            else
                                Toast.makeText(provideipc.this, "Please use the normal search function!", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
