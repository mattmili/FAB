package com.csci4100.fab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
 * Created by 100481892 on 11/29/2015.
 */
public class BookTitleHelper extends AppCompatActivity {

    private static final int LOADING_REQUEST_CODE = 0;
    String bookTitle;
    String isbnString;
    Bitmap bookCover = null;
    String[] isbns;
    Context context;
    int pos = 0;

    public BookTitleHelper(Context context, String bookTitle) {
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

        /*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Start loading screen activity
            Intent startLoadingScreenActivity = new Intent(BookTitleHelper.this, LoadingScreen.class);
            startLoadingScreenActivity.putExtra("load_message", "Retrieving book reviews");
            startActivityForResult(startLoadingScreenActivity, LOADING_REQUEST_CODE);
        }
        */

        @Override
        protected String doInBackground(String... args) {
            try {
                /*
                String key = args[0];
                String url_api = "https://damp-wildwood-1388.herokuapp.com/getbook?items="+key;
                Log.d("URL", url_api);
                URL url = new URL(url_api);
                HttpURLConnection conn;
                conn = (HttpURLConnection)url.openConnection();
                int result = conn.getResponseCode();
                if(result == HttpURLConnection.HTTP_OK){
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


                //Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                //return bmp;

            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonISBN) {
            // Close loading screen activity
            //finishActivity(LOADING_REQUEST_CODE);

            try {
                getBookISBN(jsonISBN);
            } catch (org.json.JSONException e) {
                Log.d("JSON Error with ISBNS", e.toString());
            }
            Log.d("Result", jsonISBN);
        }
    }

    public void getBookISBN(String jsonString) throws org.json.JSONException {
        JSONObject mainObject = new JSONObject(jsonString);
        JSONArray books = mainObject.getJSONArray("books");
        String isbnList = books.getJSONObject(0).getString("isbns");
        isbns = isbnList.toString().split(",");
        String coverURL = "http://covers.openlibrary.org/b/isbn/" + isbns[0] + "-S.jpg?default=false";
        Log.d("ISBN", isbns[0]);
        getCover(coverURL);
    }

    public void getCoverFromURL(String[] list, int pos) {
        String coverURL = "http://covers.openlibrary.org/b/isbn/" + list[pos] + "-S.jpg";
        Log.d("ISBN", list[pos]);
        getCover(coverURL);
    }

    public void getCover(String link) {
        new BookCover().execute(link);
    }

    class BookCover extends AsyncTask<String, Void, Bitmap> {

        Bitmap bmp;

        private Exception exception = null;

        /*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Start loading screen activity
            Intent startLoadingScreenActivity = new Intent(BookTitleHelper.this, LoadingScreen.class);
            startLoadingScreenActivity.putExtra("load_message", "Retrieving book reviews");
            startActivityForResult(startLoadingScreenActivity, LOADING_REQUEST_CODE);
        }
        */

        @Override
        protected Bitmap doInBackground(String... args) {
            try {
                /*
                String key = args[0];
                String url_api = "https://damp-wildwood-1388.herokuapp.com/getbook?items="+key;
                Log.d("URL", url_api);
                URL url = new URL(url_api);
                HttpURLConnection conn;
                conn = (HttpURLConnection)url.openConnection();
                int result = conn.getResponseCode();
                if(result == HttpURLConnection.HTTP_OK){
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
                */

                URL url = new URL(args[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;

            } catch (Exception e) {
                this.exception = e;
                if (pos < isbns.length) {
                    pos += 1;
                    String coverURL = "http://covers.openlibrary.org/b/isbn/" + isbns[pos] + "-S.jpg?default=false";
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
            // Close loading screen activity
            //finishActivity(LOADING_REQUEST_CODE);

            setBookCover(cover);
            Log.d("Result", "True");
        }
    }

    public void setBookCover(Bitmap temp) {

        bookCover = temp;
        Log.d("ISBNWorked", isbns[pos]);

    }
}