package com.csci4100.reviewr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.csci4100.reviewr.helper.BookTitleHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Result extends AppCompatActivity {

    private String textResult;
    private static  final int LOADING_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent caller = getIntent();
        textResult = caller.getStringExtra("result");
        String book = textResult.replace(" ", "+");

        getReviews(book);
    }

    public void getReviews(String key){
        new getJSONData().execute(key);
    }

    private class getJSONData extends AsyncTask<String, Void, String>{

        private Exception exception = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Start loading screen activity
            Intent startLoadingScreenActivity = new Intent(Result.this, LoadingScreen.class);
            startLoadingScreenActivity.putExtra("load_message", "Retrieving book reviews");
            startActivityForResult(startLoadingScreenActivity, LOADING_REQUEST_CODE);
        }

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
            // Close loading screen activity
            finishActivity(LOADING_REQUEST_CODE);

            try {
                populateView(json);
            } catch (org.json.JSONException e) {
                Log.d("JSON Error", e.toString());
            }
            Log.d("Result", json);
        }
    }

    public void populateView(String jsonString) throws org.json.JSONException {
        final ParseDisplay display = new ParseDisplay(jsonString);

        final BookTitleHelper helperTitle = new BookTitleHelper(this, display.getTitle().replace(" ", "+"));

        TextView title = (TextView) findViewById(R.id.book_title);
        TextView author = (TextView) findViewById(R.id.name_of_author);

        ImageView bufferCover = (ImageView) findViewById(R.id.book_cover);
        bufferCover.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_sample));

        final ImageView cover = (ImageView) findViewById((R.id.book_cover));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Bitmap testTwo = helperTitle.getCoverBitmap();
                if (testTwo == null) {

                } else {
                    cover.setImageBitmap(testTwo);
                }
            }
        }, 3000);

        TextView reviewLabel = (TextView) findViewById(R.id.review_label);
        ListView reviews = (ListView) findViewById(R.id.review_list);

        title.setText(display.getTitle());
        author.setText(display.getAuthor());
        reviewLabel.setText("Displaying " + display.getReviews().size() + " of " + display.getNumReviews());

        class MySimpleArrayAdapter extends ArrayAdapter<String> {
            private final Context context;
            private final ArrayList<String> reviews;
            private final ArrayList<String> reviewers;

            public MySimpleArrayAdapter(Context context, ArrayList<String> reviews, ArrayList<String> reviewers) {
                super(context, R.layout.row_layout, reviews);
                this.context = context;
                this.reviews = reviews;
                this.reviewers = reviewers;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.row_layout, parent, false);
                TextView contentView = (TextView) rowView.findViewById(R.id.contentTextView);
                TextView reviewerView = (TextView) rowView.findViewById(R.id.reviewerTextView);

                contentView.setText(reviews.get(position));
                reviewerView.setText(getResources().getString(R.string.source).toString() + " " + reviewers.get(position));

                return rowView;
            }
        }

        final ArrayList<String> tempReview = new ArrayList<>();
        final ArrayList<String> tempReviewer = new ArrayList<>();
        for (int i = 0; i < display.getReviews().size(); i++) {
            tempReview.add(display.getReviews().get(i).get(0));
            tempReviewer.add(display.getReviews().get(i).get(1));
        }

        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, tempReview, tempReviewer);
        reviews.setAdapter(adapter);

        //ListAdapter listAdapter = new ArrayAdapter<>(this, R.layout.row_layout, tempReview);
        //reviews.setAdapter(listAdapter);
        reviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                try {
                    String link = display.getReviews().get(position).get(2);
                    Log.d("ListClick", link);
                    if (link != null) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(browserIntent);
                    }
                } catch (Exception ex) {
                    Log.println(1, "item-click-event", ex.getMessage());
                }
            }
        });
    }
}
