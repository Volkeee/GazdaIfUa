package com.softdeal.gazdaifua.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.softdeal.gazdaifua.R;
import com.softdeal.gazdaifua.model.Advertisement;
import com.softdeal.gazdaifua.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Viktor on 06/01/2017.
 */

public class ConnectionManager extends IntentService {
    public static final String ACTION_RETURNCATEGORIES = "com.softdeal.gazdaif.RETURNCATEGORIES";
    public static final String ACTION_RETURNADS = "com.softdeal.gazdaif.RETURNADS";
    public static final String ACTION_RETURNIMAGESLINKS = "com.softdeal.gazdaif.RETURNIMAGESLINKS";
    public static String TAG = "Connection Manager";
    public static String rootImagesUrl = "http://gazda.if.ua/images/flats/";
    public static String linkBuilder = "http://gazda.if.ua/posts/zabudovniki";
    public static String linkNotary = "http://gazda.if.ua/posts/notarius";
    public static String linkEstimations = "http://gazda.if.ua/posts/ocenka";
    public static String linkRepair = "http://gazda.if.ua/posts/remont";
    public static String linkWebsite = "http://gazda.if.ua/";
    private String rootUrl;
    private String adsByCategoryRoute;
    private String adRoute;
    private String categoriesRoute;
    private String latestAdsRoute;

    private Context mContext;
    private RequestQueue mRequestQueue;

    public ConnectionManager() {
        super("GazdaIfUaService");
    }

    public ConnectionManager(Context context) {
        super("GazdaIfUaService");
        rootUrl = " http://gazda.if.ua/api/";
        adsByCategoryRoute = "get_ads_by_category";
        adRoute = "get_one_ads";
        categoriesRoute = "get_category";
        latestAdsRoute = "get_last_ads_post";
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public void requestAdsForCategory(Category category) {
        StringRequest request = new StringRequest(Request.Method.POST, rootUrl.trim().concat(adsByCategoryRoute), response -> {
            if (response.equals("[]")) {
                Toast.makeText(mContext, mContext.getString(R.string.error_no_ads_in_category), Toast.LENGTH_LONG).show();
            } else {
                Intent serviceIntent = new Intent(mContext, this.getClass());
                serviceIntent.setData(Uri.parse(response)).putExtra("type", "adsforcategory");

                mContext.startService(serviceIntent);
            }
        }, error -> Log.e(TAG, error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cat_id", category.getCategoryID().toString());
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    public void requestAdvertisementsImages(Advertisement advertisement) {
        StringRequest request = new StringRequest(Request.Method.POST, rootUrl.trim().concat(adRoute), response -> {
            Intent serviceIntent = new Intent(mContext, this.getClass());
            if (response.contains("\"images\":[]")) {
                Toast.makeText(mContext, "Response is empty! No photos!", Toast.LENGTH_LONG).show();

                serviceIntent.putExtra("type", "NO_IMAGES");
            } else {
                serviceIntent.setData(Uri.parse(response)).putExtra("type", "imagesforad");
            }
            mContext.startService(serviceIntent);
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ads_id", advertisement.getAdvertisementID().toString());
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    public void requestCategories() {
        StringRequest request = new StringRequest(Request.Method.POST, rootUrl.trim().concat(categoriesRoute), response -> {
            if (response.equals("[]")) {
                Toast.makeText(mContext, "Cannot get categories!", Toast.LENGTH_LONG).show();
            } else {
//                Log.d("Categories response:", response);
                Intent serviceIntent = new Intent(mContext, this.getClass());
                serviceIntent.setData(Uri.parse(response)).putExtra("type", "categories");

                mContext.startService(serviceIntent);
            }
        }, error -> Log.e(TAG, error.toString())
        );
        mRequestQueue.add(request);
    }

    public void requestlatestAds(Integer quantity) {
        StringRequest request = new StringRequest(Request.Method.POST, rootUrl.trim().concat(latestAdsRoute), response -> {
            Intent serviceIntent = new Intent(mContext, this.getClass());
            serviceIntent.setData(Uri.parse(response)).putExtra("type", "latestAds");

            mContext.startService(serviceIntent);
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("limit", quantity.toString());
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String type = intent.getStringExtra("type");
        Intent response = new Intent();
        String dataString = intent.getDataString();

        if (type.equals("categories")) {
            ArrayList<Category> categories = Category.parseJSON(dataString);
            response.setAction(ACTION_RETURNCATEGORIES);
            response.putExtra("categories", categories);
            response.addCategory(Intent.CATEGORY_DEFAULT);

            sendBroadcast(response);
        } else if (type.equals("adsforcategory")) {
            ArrayList<Advertisement> advertisements = Advertisement.parseJSON(dataString);
            response.setAction(ACTION_RETURNADS);
            response.putExtra("advertisements", advertisements);
            response.addCategory((Intent.CATEGORY_DEFAULT));

            sendBroadcast(response);
        } else if (type.equals("imagesforad")) {
            ArrayList<String> imagesLinks = Advertisement.parseImagesJSON(dataString);
            response.setAction(ACTION_RETURNIMAGESLINKS);
            response.putExtra("imagesLinks", imagesLinks);
            response.addCategory(Intent.CATEGORY_DEFAULT);

            sendBroadcast(response);
        } else if (type.equals("latestAds")) {
            ArrayList<Advertisement> advertisements = Advertisement.parseJSON(dataString);
            response.setAction(ACTION_RETURNADS);
            response.putExtra("advertisements", advertisements);
            response.addCategory((Intent.CATEGORY_DEFAULT));

            sendBroadcast(response);
        } else if (type.equals("NO_IMAGES")) {
            response.addCategory(Intent.CATEGORY_DEFAULT);
            response.setAction(ACTION_RETURNIMAGESLINKS);
            response.putExtra("ERROR", "NO_IMAGES");

            sendBroadcast(response);
        }
    }
}
