package com.csci4100.reviewr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class BookEntry extends AppCompatActivity {

    TextView bookToSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_entry);

        TextView bookToSearch = (TextView) findViewById(R.id.query);
        bookToSearch.setHint(R.string.input_hint);
        bookToSearch.setHintTextColor(getResources().getColor(R.color.icons));

    }

    public void searchAgain(View view) {
        bookToSearch = (TextView) findViewById(R.id.query);
        String query = bookToSearch.getText().toString();

        Intent startResultIntent = new Intent(BookEntry.this, Result.class);
        startResultIntent.putExtra("result", query);
        startActivity(startResultIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
