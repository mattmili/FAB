package com.csci4100.reviewr;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 100481892 on 11/28/2015.
 */
public class ParseDisplay {
    JSONObject mainObject;
    JSONObject book;
    String title;
    String author;
    String genre;
    int numReviews;
    int rating;
    String fullLink;

    JSONArray shortReviews;
    HashMap<Integer, ArrayList<String>> reviews = new HashMap<>();

    String noBookResult = "-";
    String noReviewResult = "-";

    public ParseDisplay(String jsonString) throws org.json.JSONException {
        this.mainObject = new JSONObject(jsonString);

        String test = mainObject.getString("total_results");
        if (test.equals("0")) {
            noBookResult = "No Books";
        } else {
            parseObject(mainObject);
        }

    }

    public void parseObject(JSONObject object) throws org.json.JSONException {
        book = object.getJSONObject("book");
        title = book.getString("title");
        author = book.getString("author");
        genre = book.getString("genre");
        numReviews = book.getInt("review_count");
        try {
            rating = book.getInt("rating");
        } catch (org.json.JSONException e){
            rating = 0;
        }

        fullLink = book.getString("detail_link");

        shortReviews = book.getJSONArray("critic_reviews");

        if (shortReviews.length() == 0) {
            noReviewResult = "No Reviews";
        } else {
            for (int i = 0; i < shortReviews.length(); i++) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(shortReviews.getJSONObject(i).getString("snippet"));
                temp.add(shortReviews.getJSONObject(i).getString("source"));
                temp.add(shortReviews.getJSONObject(i).getString("review_link"));
                temp.add(shortReviews.getJSONObject(i).getString("star_rating"));
                reviews.put(i, temp);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
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

    public String getNoBookResult() {
        return noBookResult;
    }

    public String getNoReviewResult() {
        return noReviewResult;
    }
}