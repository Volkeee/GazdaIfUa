package com.softdeal.gazdaifua.model;

import android.util.Log;

import com.softdeal.gazdaifua.service.ConnectionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by Viktor on 06/01/2017.
 */

public class Advertisement implements Serializable {
    private Integer advertisementID;
    private Category category;
    private Integer userID;
    private String title;
    private String street;
    private String description;
    private Integer price;
    private String contact;
    private String mainPhoneNumber;
    private String additionalPhoneNumber;
    private String email;
    private String imageLink;
    private Integer views;
    private String address;
    private String creationDate;
    private Boolean active;

    public Advertisement(Integer advertisementID, Category category, Integer userID, String title, String street, String description, Integer price, String contact, String mainPhoneNumber, String additionalPhoneNumber, String email, String imageLink, Integer views, String creationDate, Boolean active) {
        this.advertisementID = advertisementID;
        this.category = category;
        this.userID = userID;
        this.title = title;
        this.street = street;
        this.description = description;
        this.price = price;
        this.contact = contact;
        this.mainPhoneNumber = mainPhoneNumber;
        this.additionalPhoneNumber = additionalPhoneNumber;
        this.email = email;
        this.imageLink = imageLink;
        this.views = views;
        this.creationDate = creationDate;
        this.active = active;
    }

    private Advertisement() {
    }

    public static ArrayList<Advertisement> parseJSON(String jsonString) {
        ArrayList<Advertisement> advertisements = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (Integer i = 0; i < jsonArray.length(); i++) {
                JSONObject advertisementJSON = jsonArray.getJSONObject(i);
                Advertisement advertisement = new Advertisement();

                advertisement.setAdvertisementID(advertisementJSON.getInt(advertisement.getKeyAdID()));
                advertisement.setCategory(new Category(advertisementJSON.getInt(advertisement.getKeyCategory())));
                advertisement.setUserID(advertisementJSON.getInt(advertisement.getKeyAduserID()));
                advertisement.setTitle(advertisementJSON.getString(advertisement.getKeyAdtitle()));
                advertisement.setStreet(advertisementJSON.getString(advertisement.getKeyAdstreet()));
                advertisement.setDescription(URLDecoder.decode(advertisementJSON.getString(advertisement.getKeyAddescription()).replace("%", " ")));
                advertisement.setPrice(advertisementJSON.getInt(advertisement.getKeyAdprice()));
                advertisement.setContact(URLDecoder.decode(advertisementJSON.getString(advertisement.getKeyAdcontact())));
                advertisement.setMainPhoneNumber(advertisementJSON.getString(advertisement.getKeyAdmainPhoneNumber()));
                advertisement.setAdditionalPhoneNumber(advertisementJSON.getString(advertisement.getKeyAdadditionalPhoneNumber()));
                advertisement.setEmail(advertisementJSON.getString(advertisement.getKeyAdemail()));
                if (!advertisementJSON.getString(advertisement.getKeyAdimageLink()).isEmpty())
                    advertisement.setImageLink((ConnectionManager.rootImagesUrl.
                            concat(advertisementJSON.getString(advertisement.getKeyAdimageLink()))));
                else advertisement.setImageLink(null);
                advertisement.setViews(advertisementJSON.getInt(advertisement.getKeyAdviews()));
                advertisement.setCreationDate(advertisementJSON.getString(advertisement.getKeyAdcreationDate()));
                advertisement.setActive(Boolean.getBoolean(advertisementJSON.getString(advertisement.getKeyAdactive())));
                advertisement.setAddress(advertisementJSON.getString(advertisement.getKeyAdAddress()));

                advertisements.add(advertisement);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return advertisements;
    }

    public static ArrayList<String> parseImagesJSON(String jsonString) {
        ArrayList<String> links = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("images");

            for (Integer i = 0; i < jsonArray.length(); i++) {
                links.add(ConnectionManager.rootImagesUrl.concat(jsonArray.getJSONObject(i).getString("file")));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return links;
    }

    public Integer getAdvertisementID() {
        return advertisementID;
    }

    private void setAdvertisementID(Integer advertisementID) {
        this.advertisementID = advertisementID;
    }

    public Category getCategory() {
        return category;
    }

    private void setCategory(Category category) {
        this.category = category;
    }

    public Integer getUserID() {
        return userID;
    }

    private void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStreet() {
        return street;
    }

    private void setStreet(String street) {
        this.street = street;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    private void setPrice(Integer price) {
        this.price = price;
    }

    public String getContact() {
        return contact;
    }

    private void setContact(String contact) {
        this.contact = contact;
    }

    public String getMainPhoneNumber() {
        return mainPhoneNumber;
    }

    private void setMainPhoneNumber(String mainPhoneNumber) {
        this.mainPhoneNumber = mainPhoneNumber;
    }

    public String getAdditionalPhoneNumber() {
        return additionalPhoneNumber;
    }

    private void setAdditionalPhoneNumber(String additionalPhoneNumber) {
        this.additionalPhoneNumber = additionalPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getImageLink() {
        return imageLink;
    }

    private void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Integer getViews() {
        return views;
    }

    private void setViews(Integer views) {
        this.views = views;
    }

    public String getCreationDate() {
        return creationDate;
    }

    private void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getActive() {
        return active;
    }

    private void setActive(Boolean active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    private String getKeyAdID() {
        return "id";
    }

    private String getKeyCategory() {
        return "cat_id";
    }

    private String getKeyAduserID() {
        return "user_id";
    }

    private String getKeyAdtitle() {
        return "title";
    }

    private String getKeyAdstreet() {
        return "street";
    }

    private String getKeyAddescription() {
        return "descr";
    }

    private String getKeyAdprice() {
        return "cena";
    }

    private String getKeyAdcontact() {
        return "contact";
    }

    private String getKeyAdmainPhoneNumber() {
        return "tel1";
    }

    private String getKeyAdadditionalPhoneNumber() {
        return "tel2";
    }

    private String getKeyAdemail() {
        return "email";
    }

    private String getKeyAdimageLink() {
        return "image";
    }

    private String getKeyAdviews() {
        return "prosmotrov";
    }

    private String getKeyAdcreationDate() {
        return "date_create";
    }

    private String getKeyAdactive() {
        return "active";
    }

    private String getKeyAdAddress() {
        return "street";
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "advertisementID=" + advertisementID +
                ", category=" + category +
                ", userID=" + userID +
                ", title='" + title + '\'' +
                ", street='" + street + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", contact='" + contact + '\'' +
                ", mainPhoneNumber='" + mainPhoneNumber + '\'' +
                ", additionalPhoneNumber='" + additionalPhoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", views=" + views +
                ", creationDate='" + creationDate + '\'' +
                ", active=" + active +
                '}';
    }

}
