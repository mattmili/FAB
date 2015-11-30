package com.csci4100.reviewr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class BookEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_entry);

        TextView bookToSearch = (TextView) findViewById(R.id.query);
        bookToSearch.setHint(R.string.input_hint);
        bookToSearch.setHintTextColor(getResources().getColor(R.color.icons));
        String query = bookToSearch.getText().toString();

        Intent startResultIntent = new Intent(BookEntry.this, Result.class);
        startResultIntent.putExtra("result", query);
        startActivity(startResultIntent);
    }
}
