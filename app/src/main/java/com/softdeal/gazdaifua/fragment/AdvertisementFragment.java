package com.softdeal.gazdaifua.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softdeal.gazdaifua.R;
import com.softdeal.gazdaifua.adapter.AdsRecyclerViewAdapter;
import com.softdeal.gazdaifua.model.Advertisement;
import com.softdeal.gazdaifua.model.Category;
import com.softdeal.gazdaifua.service.ConnectionManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdvertisementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdvertisementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdvertisementFragment extends Fragment {
    private static final String ARG_ADVERTISEMENT = "advertisement_parameter";
    private Category mCategory;
    private RecyclerView mRecyclerView;
    private ConnectionManager mConnectionManager;
    private AdsBroadcastReceiver mAdsBroadcastReceiver;
    private AdsRecyclerViewAdapter mAdapter;
    private ArrayList<Advertisement> mAdsList;
    private OnFragmentInteractionListener mListener;

    public AdvertisementFragment() {
        // Required empty public constructor
    }

    public static AdvertisementFragment newInstance(Category category) {
        AdvertisementFragment fragment = new AdvertisementFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ADVERTISEMENT, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnectionManager = new ConnectionManager(getContext());
        mAdsBroadcastReceiver = new AdsBroadcastReceiver();

        if (getArguments() != null) {
            mCategory = (Category) getArguments().getSerializable(ARG_ADVERTISEMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_advertisement, container, false);

        mConnectionManager.requestAdsForCategory(mCategory);

        IntentFilter adsIntentFilter = new IntentFilter(ConnectionManager.ACTION_RETURNADS);
        adsIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mAdsBroadcastReceiver, adsIntentFilter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdsList = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_ads);
        mAdapter = new AdsRecyclerViewAdapter(mAdsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class AdsBroadcastReceiver extends BroadcastReceiver {

        @SuppressWarnings("unchecked")
        @Override
        public void onReceive(Context context, Intent intent) {
            mAdsList = (ArrayList<Advertisement>) intent.getSerializableExtra("advertisements");

            mAdapter.swap(mAdsList);

            if (mAdsBroadcastReceiver != null && getActivity() != null)
                try {
                    getActivity().unregisterReceiver(mAdsBroadcastReceiver);
                } catch (IllegalArgumentException e) {
                    Log.i("Unregister", e.toString());
                }
        }
    }

}
