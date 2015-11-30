package com.csci4100.reviewr;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AlexanderGladu.
 */
public class ParseDisplay {
    /**
     * Creates global variables to define a book by.
     */
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

    /**
     * Error values to find if there is no result or no reviews.
     */
    String noBookResult = "-";
    String noReviewResult = "-";

    public ParseDisplay(String jsonString) throws org.json.JSONException {
        /**
         * A JSON string is passed in, made into an object, and the first "book"
         *  in that object is used.
         */
        this.mainObject = new JSONObject(jsonString);

        String test = mainObject.getString("total_results");
        /**
         * Error checking for no book.
         */
        if (test.equals("0")) {
            noBookResult = "No Books";
        } else {
            parseObject(mainObject);
        }

    }

    public void parseObject(JSONObject object) throws org.json.JSONException {
        /**
         * Assigns the values of the book from the object.
         */
        book = object.getJSONObject("book");
        title = book.getString("title");
        author = book.getString("author");
        genre = book.getString("genre");
        numReviews = book.getInt("review_count");
        /**
         * Error catching in case there is no rating category.
         */
        try {
            rating = book.getInt("rating");
        } catch (org.json.JSONException e){
            rating = 0;
        }

        fullLink = book.getString("detail_link");
        shortReviews = book.getJSONArray("critic_reviews");

        /**
         * Error checking for if there are no reviews on the book.
         */
        if (shortReviews.length() == 0) {
            noReviewResult = "No Reviews";
        } else {
            for (int i = 0; i < shortReviews.length(); i++) {
                /**
                 * The array list is composed of:
                 *      Pos[0]: review snippet
                 *      Pos[1]: review source
                 *      Pos[2]: link to the review's home page
                 *      Pos[3]: the rating the review gave the book (X/5)
                 */
                ArrayList<String> temp = new ArrayList<>();
                temp.add(shortReviews.getJSONObject(i).getString("snippet"));
                temp.add(shortReviews.getJSONObject(i).getString("source"));
                temp.add(shortReviews.getJSONObject(i).getString("review_link"));
                temp.add(shortReviews.getJSONObject(i).getString("star_rating"));
                /**
                 * This is placed in the hashMap for reference of number of reviews.
                 */
                reviews.put(i, temp);
            }
        }
    }

    /**
     * General get methods.
     */
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

    public Map<Integer, ArrayList<String>> getReviews() {
        return reviews;
    }

    /**
     * Error message gets;
     */
    public String getNoBookResult() {
        return noBookResult;
    }

    public String getNoReviewResult() {
        return noReviewResult;
    }
}