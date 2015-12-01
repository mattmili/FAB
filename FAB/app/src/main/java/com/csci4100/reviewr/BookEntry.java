package com.csci4100.reviewr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mattmili
 * This Activity is called when a query is not recognized through a photo prompting the user to enter the title or ISBN manually
 */
public class BookEntry extends AppCompatActivity {

    TextView bookToSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_entry);

        final TextView bookToSearch = (TextView) findViewById(R.id.query);
        bookToSearch.setHint(R.string.input_hint);
        bookToSearch.setHintTextColor(getResources().getColor(R.color.icons));

        /**
         * Start Result Activity
         */
        Button searchAgain = (Button) findViewById(R.id.searchAgainButton);
        searchAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startResultIntent = new Intent(BookEntry.this, Result.class);
                startResultIntent.putExtra("result", bookToSearch.getText().toString());
                startActivity(startResultIntent);
            }
        });

    }

}
