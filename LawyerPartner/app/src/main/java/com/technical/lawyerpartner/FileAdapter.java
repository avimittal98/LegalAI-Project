package com.technical.lawyerpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;


public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private ArrayList<String> mFileNames = new ArrayList<>();
    private ArrayList<String> mFileUrls = new ArrayList<>();
    private Context mContext;
    private String path;
    private StorageReference mStorageRef;
    private long downloadID;


    public FileAdapter(Context mContext , ArrayList<String> mFileNames, ArrayList<String> mFileUrls,String path) {
        this.mContext = mContext;
        this.mFileUrls = mFileUrls;
        this.mFileNames = mFileNames;
        this.path = path;
    }
    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.file_item,parent,false);
        FileViewHolder holder = new FileViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, final int position) {
        Log.d("FileAdapter", "onBindViewHolder: called.");
        holder.mTextView1.setText(mFileNames.get(position));
        holder.mDeleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(mContext);
                confirm.setTitle("Confirm Delete");
                confirm.setMessage("Are you sure you want to delete this file?");
                confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mStorageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference master = mStorageRef.child(path+"/"+mFileNames.get(position));
                        master.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(mContext,"Delete succesful!",Toast.LENGTH_SHORT).show();
                                mFileNames.remove(position);
                                mFileUrls.remove(position);
                                notifyItemRemoved(position);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext,"Error:"+e,Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                confirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing
                    }
                });
                confirm.show();

            }
        });
        holder.mDownloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(mContext.getExternalFilesDir(null),mFileNames.get(position));
                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(mFileUrls.get(position)))
                        .setTitle(mFileNames.get(position))// Title of the Download Notification
                        .setDescription("Downloading")// Description of the Download Notification
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                        .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                        .setRequiresCharging(false)// Set if charging is required to begin the download
                        .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                        .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
                DownloadManager downloadManager= (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                downloadID = downloadManager.enqueue(request);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mFileNames.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView1;
        public ImageView mDeleteFile;
        public ImageView mDownloadFile;



        public FileViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView100);
            mDownloadFile = itemView.findViewById(R.id.file_download);
            mDeleteFile = itemView.findViewById(R.id.file_delete);



        }
    }
}

