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


public class PoliceStationAdapter extends RecyclerView.Adapter<PoliceStationAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "PoliceStationAdapter";

    private ArrayList<PoliceList> mPolice = new ArrayList<>();
    private ArrayList<PoliceList> mPoliceFiltered = new ArrayList<>();
    private Context mContext;

    public PoliceStationAdapter(Context mContext ,ArrayList<PoliceList> mPolice) {
        this.mPolice = mPolice;
        this.mContext = mContext;
        this.mPoliceFiltered = new ArrayList<>(mPolice);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        /*Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);*/
        PoliceList a = mPolice.get(position);
        String name = a.getName();
        final String loc = a.getCoordinates();
        holder.imageName.setText(name);

        holder.image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse(loc);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                mContext.startActivity(mapIntent);
            }

        });
    }



    @Override
    public int getItemCount() {

        return mPolice.size();

    }


    @Override
    public Filter getFilter()

    {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            mPolice = mPoliceFiltered;
            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (Exception e)
            {
                Toast.makeText(mContext,"Error:"+e,Toast.LENGTH_LONG);
            }

            ArrayList<PoliceList> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(mPoliceFiltered);
            } else {
                for (PoliceList entry : mPoliceFiltered) {
                    if (entry.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {

                        filteredList.add(entry);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mPolice.clear();
            mPolice.addAll((Collection<? extends PoliceList>) filterResults.values);
            notifyDataSetChanged();


        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName= itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);






        }
    }
}
