package com.softdeal.gazdaifua.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.softdeal.gazdaifua.R;
import com.softdeal.gazdaifua.adapter.AdsImagesRecyclerAdapter;
import com.softdeal.gazdaifua.fragment.FullscreenImageFragment;

import java.util.ArrayList;

//import com.softdeal.gazdaifua.adapter.viewpager.FullscreenImageAdapter;

public class FullscreenImageActivity extends AppCompatActivity {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    public static FullscreenImageFragment mCurrentFragment;
    private final Handler mHideHandler = new Handler();
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
//            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private final View.OnTouchListener mDelayHideTouchListener = (view, motionEvent) -> {
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS);
        }
        return false;
    };
    private boolean mVisible;
    private ViewPager mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mHideRunnable = this::hide;
    private ViewPager mViewPager;
    private FullscreenImageAdapter mImagePager;
    private ArrayList<String> mLinks;
    private AdsImagesRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_image);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.title_activity_fullscreen_image));
        }
        Intent intent = getIntent();
        if (intent != null) {
            mLinks = (ArrayList<String>) intent.getSerializableExtra("imagesLinks");
            mImagePager = new FullscreenImageAdapter(getSupportFragmentManager(), mLinks);
            mVisible = true;
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_images);
            mContentView = (ViewPager) findViewById(R.id.viewpager);
            mContentView.setAdapter(mImagePager);

            mAdapter = new AdsImagesRecyclerAdapter(mLinks, mContentView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.setAdapter(mAdapter);
        }

        // Set up the user interaction to manually show or hide the system UI.
//        mContentView.setOnTouchListener((view, motionEvent) -> toggle());

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        findViewById(R.id.fullscreen_recycler_view).setOnTouchListener(mDelayHideTouchListener);
    }

    private boolean toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
        return true;
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public class FullscreenImageAdapter extends FragmentPagerAdapter {
        private ArrayList<String> mImages;

        public FullscreenImageAdapter(FragmentManager fm, ArrayList<String> mImages) {
            super(fm);
            this.mImages = mImages;
        }

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public Fragment getItem(int position) {
            FullscreenImageFragment fragment = FullscreenImageFragment.newInstance(mImages.get(position));
            mCurrentFragment = fragment;
            return fragment;
        }


    }
}
