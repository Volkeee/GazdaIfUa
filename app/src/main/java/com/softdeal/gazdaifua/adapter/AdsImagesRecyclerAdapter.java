package com.softdeal.gazdaifua.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.softdeal.gazdaifua.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Viktor on 11/01/2017.
 */

public class AdsImagesRecyclerAdapter extends RecyclerView.Adapter<AdsImagesRecyclerAdapter.ViewHolder> {
    private ArrayList<String> mLinks;
    private ImageView mainImageView;

    public AdsImagesRecyclerAdapter(ArrayList<String> links, ImageView mainImageView) {
        this.mLinks = links;
        this.mainImageView = mainImageView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_horizontal_recycler_view_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageLink = mLinks.get(position);

        Picasso.with(holder.context).load(imageLink).into(holder.imageView);
        holder.imageView.setOnClickListener(view -> {
            Picasso.with(holder.context).load(imageLink).into(mainImageView);
        });
        /*Open fullscreen image activity with selected image*/
//        mainImageView.setOnClickListener(view1 -> {
//            Intent intent = new Intent(holder.context, FullscreenImageActivity.class);
//            intent.putExtra("link", imageLink);
//
//            holder.context.startActivity(intent);
//        });

    }

    public void swap(ArrayList<String> list) {
        if (mLinks != null) {
            mLinks.clear();
            mLinks.addAll(list);
        } else {
            mLinks = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mLinks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Context context;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            Configuration configuration = itemView.getResources().getConfiguration();
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageView = (ImageView) itemView.findViewById(R.id.item_recyclerview_image_land);
            } else {
                imageView = (ImageView) itemView.findViewById(R.id.item_recyclerview_image);
            }

            context = itemView.getContext();
        }
    }
}
