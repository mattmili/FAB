package com.csci4100.fab;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Result extends AppCompatActivity {

    private String textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent result = getIntent();
        textResult = result.getStringExtra("result");

        String book = textResult.replace(" ", "+");

        Log.d("BOOK TITLE", book);

        getReviews(book);
    }

    public void getReviews(String key){
        new getJSONData().execute(key);
    }

    private class getJSONData extends AsyncTask<String, Void, String>{

        private Exception exception = null;

        @Override
        protected String doInBackground(String... args) {
            try{
                String JSONResult ="";
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
            }catch (Exception e){
                this.exception = e;
                return null;
            }

        }

        @Override
        protected void onPostExecute(String json) {
            try {
                populateView(json);
            } catch (org.json.JSONException e) {
                Log.d("JSON Error", e.toString());
            }
            Log.d("Result", json);
        }
    }

    public void populateView(String jsonString) throws org.json.JSONException {
        ParseDisplay display = new ParseDisplay(jsonString);

        TextView title = (TextView) findViewById(R.id.book_title);
        TextView author = (TextView) findViewById(R.id.name_of_author);
        TextView reviewLabel = (TextView) findViewById(R.id.review_label);
        ListView reviews = (ListView) findViewById(R.id.review_list);

        title.setText(display.getTitle());
        author.setText(display.getAuthor());
        reviewLabel.setText("Displaying " + display.getReviews().size() + " of " + display.getNumReviews());

        /**
         * Temp array for only review values, add sources after
         */
        ArrayList<String> tempReview = new ArrayList<>();
        for (int i = 0; i < display.getReviews().size(); i++) {
            tempReview.add(display.getReviews().get(i).get(0));
        }

        ListAdapter listAdapter = new ArrayAdapter<>(this, R.layout.row_layout, tempReview);
        reviews.setAdapter(listAdapter);
    }


}
