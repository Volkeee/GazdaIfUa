package com.softdeal.gazdaifua.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdeal.gazdaifua.R;
import com.softdeal.gazdaifua.activity.AdDetailsActivity;
import com.softdeal.gazdaifua.model.Advertisement;
import com.softdeal.gazdaifua.service.ListFilter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Viktor on 10/01/2017.
 */

public class AdsRecyclerViewAdapter extends RecyclerView.Adapter<AdsRecyclerViewAdapter.ViewHolder> {
    public ArrayList<Advertisement> mAdvertisements;
    private ListFilter mFilter;

    public AdsRecyclerViewAdapter(ArrayList<Advertisement> mAdvertisements) {
        this.mAdvertisements = mAdvertisements;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        viewHolder = new ViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_ads_recycler_view, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdsRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        Advertisement advertisement = mAdvertisements.get(position);

        if (advertisement.getImageLink() != null) {
            Picasso.with(viewHolder.context).load(advertisement.getImageLink()).into(viewHolder.imageView);
            viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        viewHolder.txtViewAdPrice.setText(advertisement.getPrice().toString().concat("$"));
        viewHolder.txtViewAdDescription.setText(advertisement.getDescription());
        viewHolder.txtViewAdViews.setText(advertisement.getViews().toString());

        viewHolder.txtViewAdDetails.setOnClickListener(view -> {
            Intent intent = new Intent(viewHolder.context, AdDetailsActivity.class);
            intent.putExtra("advertisement", advertisement);

            viewHolder.context.startActivity(intent);
        });

        viewHolder.imageView.setOnClickListener(view -> {
            Intent intent = new Intent(viewHolder.context, AdDetailsActivity.class);
            intent.putExtra("advertisement", advertisement);

            viewHolder.context.startActivity(intent);
        });
    }

    public void swap(ArrayList<Advertisement> list) {
        if (mAdvertisements != null) {
            ArrayList<Advertisement> arrayList = new ArrayList<>(list);
            mAdvertisements.clear();
            mAdvertisements.addAll(arrayList);
        } else {
            mAdvertisements = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mAdvertisements.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public Context context;
        ImageView imageView;
        TextView txtViewAdPrice;
        TextView txtViewAdDescription;
        TextView txtViewAdViews;
        TextView txtViewAdDetails;

        ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.item_image_view);
            txtViewAdPrice = (TextView) itemView.findViewById(R.id.txtview_ad_price);
            txtViewAdDescription = (TextView) itemView.findViewById(R.id.txtview_ad_descr);
            txtViewAdViews = (TextView) itemView.findViewById(R.id.txtview_ad_views);
            txtViewAdDetails = (TextView) itemView.findViewById(R.id.txtview_ad_details);
            context = itemView.getContext();
        }
    }
}
