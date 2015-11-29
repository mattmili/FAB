package com.csci4100.fab;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 100481892 on 11/28/2015.
 */
public class ParseDisplay {
    JSONObject mainObject;
    JSONObject book;
    String title;
    String author;
    int numReviews;
    int rating;
    String fullLink;

    JSONArray shortReviews;
    Map<Integer, ArrayList<String>> reviews;


    public ParseDisplay(String jsonString) throws org.json.JSONException {
        this.mainObject = new JSONObject(jsonString);

        parseObject(mainObject);
    }

    public void parseObject(JSONObject object) throws org.json.JSONException {
        book = object.getJSONObject("book");
        title = book.getString("title");
        author = book.getString("author");
        numReviews = book.getInt("review_count");
        rating = book.getInt("rating");
        fullLink = book.getString("detail_link");

        shortReviews = book.getJSONArray("critic_reviews");
        for (int i = 0; i < shortReviews.length(); i++) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(shortReviews.getJSONObject(i).getString("snippet"));
            temp.add(shortReviews.getJSONObject(i).getString("source"));
            reviews.put(i, temp);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public int getRating() {
        return rating;
    }

    public String getFullLink() {
        return fullLink;
    }

    public Map<Integer, ArrayList<String>> getReviews() {
        return reviews;
    }
}
