package com.csci4100.reviewr.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AlexanderGladu.
 */
public class BookTitleHelper extends AppCompatActivity {

    /**
     * Create global variable for traits of a book that is received.
     */
    String bookTitle;
    String isbnString;
    Bitmap bookCover = null;
    String[] isbns;
    Context context;
    int pos = 0;

    public BookTitleHelper(Context context, String bookTitle) {
        /**
         * Values for context are passed in, and the title of the book received
         *  in the previous step. The url is created to get the isbn values.
         */
        this.context = context;
        this.bookTitle = bookTitle.replace(" ", "+");
        isbnString = "http://idreambooks.com/api/books/show_features.json?q=" + bookTitle + "&key=69d520efe502ad40e3811139ca0071d0d447a16d";
        getISBN(isbnString);
    }

    public void getISBN(String link) {
        new BookISBN().execute(link);
    }

    class BookISBN extends AsyncTask<String, Void, String> {

        private Exception exception = null;

        @Override
        protected String doInBackground(String... args) {
            try {
                /**
                 * Sends the url defined above to receive a JSON string that defines the
                 *  isbns of the book, which are later used to find covers.
                 */
                String JSONResult = "";
                URL url = new URL(args[0]);
                HttpURLConnection conn;
                conn = (HttpURLConnection) url.openConnection();
                int result = conn.getResponseCode();
                if (result == HttpURLConnection.HTTP_OK) {
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder out = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                    }
                    JSONResult = out.toString();
                }
                return JSONResult;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonISBN) {
            /**
             * Try block catches error if there are no isbns available.
             */
            try {
                getBookISBN(jsonISBN);
            } catch (org.json.JSONException e) {
                Log.d("JSON Error with ISBNS", e.toString());
            }
            Log.d("Result", jsonISBN);
        }
    }

    public void getBookISBN(String jsonString) throws org.json.JSONException {
        /**
         * Creates a JSONObject for the book, and takes the string value
         *  for "isbns". This is then split at the "," character,
         *  and placed into a String array for later access. It also
         *  assigns the cover lookup url to a string value.
         */
        JSONObject mainObject = new JSONObject(jsonString);
        JSONArray books = mainObject.getJSONArray("books");
        String isbnList = books.getJSONObject(0).getString("isbns");
        isbns = isbnList.toString().split(",");
        String coverURL = "http://covers.openlibrary.org/b/isbn/" + isbns[0] + "-L.jpg?default=false";
        Log.d("ISBN", isbns[0]);
        getCover(coverURL);
    }

    public void getCover(String link) {
        new BookCover().execute(link);
    }

    class BookCover extends AsyncTask<String, Void, Bitmap> {

        Bitmap bmp;
        private Exception exception = null;

        @Override
        protected Bitmap doInBackground(String... args) {
            /**
             * The url returns an image if one is availble, and
             *  returns a 404 error if not. In the catch, if it receives a
             *  404 Error, it will go to the next isbn in the isbn list,
             *  and redo the URL access with the new URL.
             */
            try {
                URL url = new URL(args[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;

            } catch (Exception e) {
                this.exception = e;
                if ((pos + 1) < isbns.length) {
                    pos += 1;
                    String coverURL = "http://covers.openlibrary.org/b/isbn/" + isbns[pos] + "-L.jpg?default=false";
                    Log.d("ISBN", isbns[pos]);
                    this.doInBackground(coverURL);
                    return bmp;
                } else {
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(Bitmap cover) {
            setBookCover(cover);
            Log.d("Result", "True");
        }
    }

    public void setBookCover(Bitmap temp) {
        /**
         * Assigns the global Bitmap value to later be gotten with the getCoverBitmap
         *  method.
         */
        bookCover = temp;
        Log.d("ISBNWorked", isbns[pos]);

    }

    public Bitmap getCoverBitmap() {
        return bookCover;
    }
}