package com.softdeal.gazdaifua.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Viktor on 07/01/2017.
 */

public class Category implements Serializable {
    private Integer categoryID;
    private String name;

    public Category(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Category(Integer categoryID, String name) {
        this.categoryID = categoryID;
        this.name = name;
    }

    public Category() {
    }

    public static ArrayList<Category> parseJSON(String jsonString) {
        ArrayList<Category> categories = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (Integer i = 0; i < jsonArray.length(); i++) {
                JSONObject categoriesJSON = jsonArray.getJSONObject(i);
                Category category = new Category();

                category.setCategoryID(categoriesJSON.getInt(category.getKeyCategoryID()));
                category.setName(categoriesJSON.getString(category.getKeyCategoryName()));

                categories.add(category);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyCategoryID() {
        return "id";
    }

    public String getKeyCategoryName() {
        return "nazva";
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryID=" + categoryID +
                ", name='" + name + '\'' +
                '}';
    }
}
