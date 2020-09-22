package com.technical.legalai;

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
import com.technical.legalai.R;
import com.technical.legalai.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class LawyerAdapter extends RecyclerView.Adapter<LawyerAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "LawyerAdapter";

    private ArrayList<Lawyer> mLawyer= new ArrayList<>();
    private Context mContext;
    private ArrayList<Lawyer> mLawyerFiltered;

    public LawyerAdapter(Context mContext ,ArrayList<Lawyer> mLawyer) {
        this.mLawyer = mLawyer;
        this.mContext = mContext;
        this.mLawyerFiltered = new ArrayList<>(mLawyer);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.lawyer_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Lawyer a = mLawyer.get(position);
        String name = a.lawyer_name;
        String charges = a.charges;
        String dop = a.dop;
        String won = a.no_wins;
        String photo = a.photo;
        Log.d(TAG, "onBindViewHolder: called.");
        Glide.with(mContext)
                .asBitmap()
                .load(photo)
                .into(holder.lawyerimage);
        holder.lawimageName.setText(name);
        holder.lawcharge.setText(charges);
        holder.lawxp.setText(dop);
        holder.lawcasewon.setText(won);

        holder.lawyerparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,aboutlawyer.class);
                i.putExtra("uid",a.lawyer_UID);
                mContext.startActivity(i);
            }

        });
    }



    @Override
    public int getItemCount() {

        return mLawyer.size();

    }


    @Override
    public Filter getFilter()

    {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            mLawyer = mLawyerFiltered;
            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (Exception e)
            {
                Toast.makeText(mContext,"Error:"+e,Toast.LENGTH_LONG);
            }

            ArrayList<Lawyer> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(mLawyerFiltered);
            } else {
                for (Lawyer mlawyernames : mLawyerFiltered) {
                    if (mlawyernames.lawyer_name.toLowerCase().contains(charSequence.toString().toLowerCase())) {

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
            mLawyer.clear();
            mLawyer.addAll((Collection<? extends Lawyer>) filterResults.values);
            notifyDataSetChanged();


        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView lawyerimage;
        TextView lawimageName;
        TextView lawcharge;
        TextView lawxp;
        TextView lawcasewon;
        RelativeLayout lawyerparent;



        public ViewHolder(View itemView) {
            super(itemView);
            lawyerimage = itemView.findViewById(R.id.imgprofilepic);
            lawimageName= itemView.findViewById(R.id.txtvlawyername);
            lawcharge= itemView.findViewById(R.id.txtvcharges);
            lawcasewon= itemView.findViewById(R.id.txtvlawyerwon);
            lawxp= itemView.findViewById(R.id.txtvlawyerxp);
            lawyerparent = itemView.findViewById(R.id.lawyerparent);






        }
    }
}
