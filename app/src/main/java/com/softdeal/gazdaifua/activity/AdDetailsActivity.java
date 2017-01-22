package com.softdeal.gazdaifua.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdeal.gazdaifua.R;
import com.softdeal.gazdaifua.model.Advertisement;
import com.softdeal.gazdaifua.service.ConnectionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdDetailsActivity extends AppCompatActivity {
    public ArrayList<String> mLinks;
    public AdView mAdView;
    public ImagesLinksBroadcastReceiver mImagesLinksBroadcastReceiver;
    public ConnectionManager mConnectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details2);
        mConnectionManager = new ConnectionManager(this);
        mImagesLinksBroadcastReceiver = new ImagesLinksBroadcastReceiver();
        mLinks = new ArrayList<>();
        mAdView = new AdView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_activity_ad_details));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent != null) {
            IntentFilter imagesLinksFilter = new IntentFilter(ConnectionManager.ACTION_RETURNIMAGESLINKS);
            imagesLinksFilter.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(mImagesLinksBroadcastReceiver, imagesLinksFilter);

            Advertisement advertisement = (Advertisement) intent.getSerializableExtra("advertisement");

            mConnectionManager.requestAdvertisementsImages(advertisement);

            if (advertisement.getImageLink() != null) {
                mAdView.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.with(this).load(advertisement.getImageLink()).into(mAdView.imageView);
                mAdView.imageView.setOnClickListener(view -> {
                    Intent fullscreenActivityIntent = new Intent(this, FullscreenImageActivity.class);
                    fullscreenActivityIntent.putExtra("imagesLinks", mLinks);

                    startActivity(fullscreenActivityIntent);
                });
            }

            mAdView.textViewAdDescription.setText(advertisement.getDescription());
            mAdView.textViewAdContactName.setText(advertisement.getContact());
            mAdView.textViewAdContact1.setText(advertisement.getMainPhoneNumber());
            mAdView.textViewAdContact2.setText(advertisement.getAdditionalPhoneNumber());
            mAdView.textViewAdPrice.setText(advertisement.getPrice().toString().concat("$"));
            mAdView.textViewAdAddress.setText(advertisement.getAddress());

            fab.setOnClickListener(view -> {
                        if (advertisement.getAdditionalPhoneNumber().isEmpty()) {
                            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + advertisement.getMainPhoneNumber()));
                            startActivity(dialIntent);
                        } else {
                            String[] numbers = new String[]{advertisement.getMainPhoneNumber(), advertisement.getAdditionalPhoneNumber()};
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle(R.string.title_alert_call).setItems(numbers, (dialogInterface, i) -> {
                                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numbers[i]));
                                startActivity(dialIntent);
                            });
                            builder.show();
                        }
                    }
            );
        }
    }

    class AdView {
        ImageView imageView;
        RecyclerView recyclerView;
        TextView textViewAdDescription;
        TextView textViewAdPrice;
        TextView textViewAdContact1;
        TextView textViewAdContact2;
        TextView textViewAdContactName;
        TextView textViewAdAddress;

        AdView() {
            imageView = (ImageView) findViewById(R.id.appbar_image_ad);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_images);
            textViewAdDescription = (TextView) findViewById(R.id.txtView_ad_description);
            textViewAdPrice = (TextView) findViewById(R.id.txtview_ad_price);
            textViewAdContact1 = (TextView) findViewById(R.id.txtview_ad_contact_phone1);
            textViewAdContact2 = (TextView) findViewById(R.id.txtview_ad_contact_phone2);
            textViewAdContactName = (TextView) findViewById(R.id.txtview_ad_contact_name);
            textViewAdAddress = (TextView) findViewById(R.id.txtview_ad_address);
        }
    }

    public class ImagesLinksBroadcastReceiver extends BroadcastReceiver {

        @SuppressWarnings("unchecked")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("ERROR") == null) {
                mAdView.imageView.setVisibility(View.VISIBLE);
                mLinks = (ArrayList<String>) intent.getSerializableExtra("imagesLinks");
                try {
                    unregisterReceiver(mImagesLinksBroadcastReceiver);
                } catch (IllegalArgumentException e) {
                    Log.i("Unregister", e.toString());
                }
            }
        }
    }
}
