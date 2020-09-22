package com.technical.lawyerpartner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class lawyergetimage extends AppCompatActivity {
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage;
    FloatingActionButton lawuploadBtn,galleryBtn;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    Uri contentUri;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyergetimage);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentuser = mAuth.getCurrentUser();
        flag = false;

        selectedImage = findViewById(R.id.lawdisplayImageView);
        lawuploadBtn = findViewById(R.id.lawuploadBtn);
        //lawuploadBtn.setVisibility(View.INVISIBLE);
        galleryBtn = findViewById(R.id.lawgalleryBtn);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        if(currentuser.getPhotoUrl()!=null)
        {
            Glide.with(this /* context */)
                    .load(currentuser.getPhotoUrl())//https://firebasestorage.googleapis.com/v0/b/legalai-b7856.appspot.com/o/Profile_Pic%2Fn6JTJYl2TfZMiZ8VIigux8aVd942.jpg?alt=media&token=e7546de2-7173-4a1c-9fc0-558462f04c6b
                    .into(selectedImage);
        }

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });
        final Context c = this;
        lawuploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StorageReference PicRef = mStorageRef.child("Profile_Pic/Lawyer/"+currentuser.getUid()+".jpg");
                PicRef.putFile(contentUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                flag = true;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(lawyergetimage.this,"Upload Failed: "+exception.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                if(flag)
                {

                    //Toast.makeText(getBaseContext(), "Reached Here" , Toast.LENGTH_SHORT).show();
                    PicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl.toString() , Toast.LENGTH_SHORT).show();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUrl)
                                    .build();
                            currentuser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("getimage", "User profile updated.");
                                                Intent i = new Intent(c,lawyercollectinfo.class);
                                                startActivity(i);
                                            }
                                        }
                                    });
                        }
                    });
                }
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                contentUri = data.getData();
                selectedImage.setImageURI(contentUri);
                lawuploadBtn.setVisibility(View.VISIBLE);
            }

        }


    }







}
