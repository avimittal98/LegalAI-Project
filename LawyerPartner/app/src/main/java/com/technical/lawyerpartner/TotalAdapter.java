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
import com.technical.lawyerpartner.R;
import com.technical.lawyerpartner.R;

import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;


public class TotalAdapter extends RecyclerView.Adapter<TotalAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "TotalAdapter";

    private ArrayList<User> mClient= new ArrayList<>();
    private Context mContext;
    private ArrayList<User> mClientFiltered;

    public TotalAdapter(Context mContext ,ArrayList<User> mClient) {
        this.mClient = mClient;
        this.mContext = mContext;
        this.mClientFiltered = new ArrayList<>(mClient);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.total_listitem,parent,false);
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

        holder.totalparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,UploadFile.class);
                i.putExtra("uid",a.Uid);
                mContext.startActivity(i);
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
        RelativeLayout totalparent;



        public ViewHolder(View itemView) {
            super(itemView);
            clientimage = itemView.findViewById(R.id.clientpic);
            clientimageName= itemView.findViewById(R.id.txtvclientname);
            clientphone= itemView.findViewById(R.id.txtvclientmob);
            clientmail= itemView.findViewById(R.id.txtvclientmail);
            totalparent = itemView.findViewById(R.id.totalparent);






        }
    }
}
