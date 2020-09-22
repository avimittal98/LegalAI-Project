package com.technical.lawyerpartner;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.concurrent.TimeUnit;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technical.lawyerpartner.R;
import com.technical.lawyerpartner.R;

import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;


public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "TotalAdapter";

    private ArrayList<User> mClient= new ArrayList<>();
    private Context mContext;
    private ArrayList<User> mClientFiltered;

    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    public NewAdapter(Context mContext ,ArrayList<User> mClient) {
        this.mClient = mClient;
        this.mContext = mContext;
        this.mClientFiltered = new ArrayList<>(mClient);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.new_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final User a = mClient.get(position);
        String name = a.username;
        String phone = a.phone;
        String email = a.email;
        String photo = a.photo;
        Log.d(TAG, "onBindViewHolder: called.");
        Glide.with(mContext)
                .asBitmap()
                .load(photo)
                .into(holder.clientimage);
        holder.clientimageName.setText(name);
        holder.clientphone.setText(phone);
        holder.clientmail.setText(email);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("lawyers/"+currentUser.getUid());
                zonesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("new_case"))
                        {
                            String total_case = dataSnapshot.child("new_case").getValue().toString();
                            String all_case[] = total_case.split(";");
                            String new_total ="";
                            for(String case1 : all_case)
                            {
                                if(!case1.equals(a.Uid))
                                    new_total = new_total+";"+case1;
                            }
                            if(new_total==" "||new_total==""||new_total==";")
                                zonesRef.child("new_case").removeValue();
                            else
                                zonesRef.child("new_case").setValue(new_total);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "onCancelled", databaseError.toException());
                    }
                });

                final DatabaseReference zonesRef1 = FirebaseDatabase.getInstance().getReference("lawyers/"+currentUser.getUid());
                zonesRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("total_case"))
                        {
                            String new_total = dataSnapshot.child("total_case").getValue().toString();
                            new_total = new_total+";"+a.Uid;
                            zonesRef.child("total_case").setValue(new_total);
                        }
                        else
                            zonesRef.child("total_case").setValue(a.Uid);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "onCancelled", databaseError.toException());
                    }
                });
                final DatabaseReference zonesRef2 = FirebaseDatabase.getInstance().getReference("user/"+a.Uid);
                zonesRef2.child("lawyer").setValue(currentUser.getUid());
                zonesRef2.child("request").removeValue();
                mClient.remove(position);
                notifyDataSetChanged();


            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("lawyers/"+currentUser.getUid());
                zonesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("new_case"))
                        {
                            String total_case = dataSnapshot.child("new_case").getValue().toString();
                            String all_case[] = total_case.split(";");
                            String new_total ="";
                            for(String case1 : all_case)
                            {
                                if(!case1.equals(a.Uid))
                                    new_total = new_total+";"+case1;

                            }
                            mClient.remove(position);
                            if(new_total==" "||new_total==""||new_total==";")
                                zonesRef.child("new_case").removeValue();
                            else
                                zonesRef.child("new_case").setValue(new_total);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "onCancelled", databaseError.toException());
                    }
                });
                final DatabaseReference zonesRef1 = FirebaseDatabase.getInstance().getReference("users/"+a.Uid);
                zonesRef1.child("request").removeValue();
                notifyDataSetChanged();
            }
        });
    }



    @Override
    public int getItemCount() {

        return mClient.size();

    }


    @Override
    public Filter getFilter()

    {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            mClient = mClientFiltered;
            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (Exception e)
            {
                Toast.makeText(mContext,"Error:"+e,Toast.LENGTH_LONG);
            }

            ArrayList<User> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(mClientFiltered);
            } else {
                for (User mlawyernames : mClientFiltered) {
                    if (mlawyernames.username.toLowerCase().contains(charSequence.toString().toLowerCase())) {

                        filteredList.add(mlawyernames);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mClient.clear();
            mClient.addAll((Collection<? extends User>) filterResults.values);
            notifyDataSetChanged();


        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView clientimage;
        TextView clientimageName;
        TextView clientphone;
        TextView clientmail;
        RelativeLayout newparent;
        FloatingActionButton accept,reject;



        public ViewHolder(View itemView) {
            super(itemView);
            clientimage = itemView.findViewById(R.id.newclientpic);
            clientimageName= itemView.findViewById(R.id.newtxtvclientname);
            clientphone= itemView.findViewById(R.id.newtxtvclientmob);
            clientmail= itemView.findViewById(R.id.newtxtvclientmail);
            newparent = itemView.findViewById(R.id.newparent);
            accept =itemView.findViewById(R.id.acceptbtn);
            reject =itemView.findViewById(R.id.refusebtn);


        }
    }
}