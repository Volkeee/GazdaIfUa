package com.softdeal.gazdaifua.service;

import android.util.Log;

import com.softdeal.gazdaifua.model.Advertisement;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Viktor on 20/01/2017.
 */

public class ListFilter {
    private ArrayList<Advertisement> mOriginalList;
    private String priceOrder;
    private String dateOrder;

    /**
     * Class for sorting the Ad lists
     *
     * @param priceOrder ASC parameter for ascending sort; DESC - for descending
     * @param dateOrder  ASC parameter for ascending sort; DESC - for descending
     */
    public ListFilter(String priceOrder, String dateOrder) {
        this.priceOrder = priceOrder;
        this.dateOrder = dateOrder;
    }

    public ArrayList<Advertisement> applyFilter(ArrayList<Advertisement> listToFilter) {
        mOriginalList = new ArrayList<>(listToFilter);
        if (priceOrder != null)
            if (priceOrder.equals("ASC")) {
                Collections.sort(listToFilter, Advertisement.PriceComparatorASC);
            } else if (priceOrder.equals("DESC")) {
                Collections.sort(listToFilter, Advertisement.PriceComparatorDESC);
            }
        if (dateOrder != null)
            if (dateOrder.equals("ASC")) {
                Collections.sort(listToFilter, Advertisement.DateComparatorASC);
            } else if (dateOrder.equals("DESC")) {
                Collections.sort(listToFilter, Advertisement.DateComparatorDESC);
            }
        Log.d("Sorted list is", listToFilter.toString());
        return listToFilter;
    }

    public ArrayList<Advertisement> getOriginalList() {
        return mOriginalList;
    }
}
