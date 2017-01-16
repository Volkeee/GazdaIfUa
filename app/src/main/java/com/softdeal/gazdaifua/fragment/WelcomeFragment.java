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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.softdeal.gazdaifua.R;
import com.softdeal.gazdaifua.adapter.AdsRecyclerViewAdapter;
import com.softdeal.gazdaifua.model.Advertisement;
import com.softdeal.gazdaifua.service.ConnectionManager;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WelcomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends Fragment {
    private static final String ARG_QUANTITY = "ads_quantity";

    private Integer mQuantityOfAds;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Advertisement> mAdsList;
    private AdsRecyclerViewAdapter mAdapter;
    private BroadcastReceiver mAdsBroadcastReceiver;
    private ConnectionManager mConnectionManager;
    private RecyclerView mRecyclerView;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    public static WelcomeFragment newInstance(Integer quantity) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUANTITY, quantity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdsBroadcastReceiver = new LatestAdsBroadcastReceiver();
        mConnectionManager = new ConnectionManager(getContext());

        if (getArguments() != null) {
            mQuantityOfAds = getArguments().getInt(ARG_QUANTITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        mConnectionManager.requestlatestAds(mQuantityOfAds);

        IntentFilter adsIntentFilter = new IntentFilter(ConnectionManager.ACTION_RETURNADS);
        adsIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mAdsBroadcastReceiver, adsIntentFilter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdsList = new ArrayList<>();
        mAdapter = new AdsRecyclerViewAdapter(mAdsList);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_latest_ads);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class LatestAdsBroadcastReceiver extends BroadcastReceiver {

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
