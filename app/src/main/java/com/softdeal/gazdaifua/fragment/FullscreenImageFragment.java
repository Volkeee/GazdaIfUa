package com.softdeal.gazdaifua.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.softdeal.gazdaifua.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Viktor on 19/01/2017.
 */

public class FullscreenImageFragment extends Fragment {
    public static final String ARGUMENT_IMAGE_LINK = "arg_page_number";
    public ImageView imageView;
    String mLink;

    public static FullscreenImageFragment newInstance(String imageLink) {
        FullscreenImageFragment pageFragment = new FullscreenImageFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_IMAGE_LINK, imageLink);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLink = getArguments().getString(ARGUMENT_IMAGE_LINK);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_imageholder, container, false);

        imageView = (ImageView) rootView.findViewById(R.id.fullscreen_imageview);
        Picasso.with(getContext()).load(mLink).into(imageView);
        return rootView;
    }
}
