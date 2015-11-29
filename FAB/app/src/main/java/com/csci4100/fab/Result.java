package com.csci4100.fab;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Result extends AppCompatActivity {

    private String textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent result = getIntent();
        textResult = result.getStringExtra("result");
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
            Log.d("Result", json);
        }
    }


}
