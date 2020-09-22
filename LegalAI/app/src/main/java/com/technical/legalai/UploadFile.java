package com.technical.legalai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class UploadFile extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    FirebaseUser currentuser;
    boolean flag1;
    ArrayList<String> mFileNames;
    ArrayList<String> mFileUrls;

    private RecyclerView mRecyclerView;
    private FileAdapter mAdapter;
    private TextView err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentuser = mAuth.getCurrentUser();
        flag1 = false;
        mFileNames = new ArrayList<>();
        mFileUrls = new ArrayList<>();

        StorageReference master = mStorageRef.child("Case_Docs/"+currentuser.getUid());
        master.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            flag1 = true;
                            //Toast.makeText(UploadFile.this,"Reached Here",Toast.LENGTH_SHORT).show();
                        }

                        for (StorageReference item : listResult.getItems()) {
                            flag1 = true;
                            mFileNames.add(item.getName());
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    flag1 = true;
                                    mFileUrls.add(uri.toString());
                                    System.out.println("Reached Here");
                                }
                            });
                        }
                        if(mFileNames.size()!= 0) //Files Found
                        {
                            Log.d("UploadFile", "initRecyclerView:init recyclerview.");
                            RecyclerView mRecyclerView = findViewById(R.id.filerecycle);
                            String path = "/Case_Docs/"+currentuser.getUid();
                            mRecyclerView.setVisibility(View.VISIBLE);
                            FileAdapter adapter = new FileAdapter(UploadFile.this,mFileNames,mFileUrls,path);
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(UploadFile.this));

                        }
                        else
                        {
                            TextView err = (TextView)findViewById(R.id.textView101);
                            err.setVisibility(View.VISIBLE);
                            err.setText("No files found!");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Toast.makeText(UploadFile.this, "Error:" + e,Toast.LENGTH_SHORT).show();
                    }
                });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.upload,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");      //all files
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
        return true;
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = this.getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
                    currentuser = mAuth.getCurrentUser();
                    StorageReference upload = mStorageRef.child("Case_Docs/"+currentuser.getUid()+"/"+displayName);
                    upload.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    Toast.makeText(UploadFile.this,"Upload Succesful",Toast.LENGTH_LONG).show();
                                    recreate();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    Toast.makeText(UploadFile.this,"Upload Failed: "+exception.toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}




