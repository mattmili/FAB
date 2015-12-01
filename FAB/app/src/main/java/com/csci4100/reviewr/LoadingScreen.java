package com.csci4100.reviewr;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mattmili.
 */
public class LoadingScreen extends AppCompatActivity {

    MediaPlayer flip;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);

        Intent caller = getIntent();
        String message = caller.getStringExtra("load_message");
        TextView loadMessageTextView  = (TextView) findViewById(R.id.loadMessage);
        loadMessageTextView.setText(message);


        this.flip = MediaPlayer.create(this, R.raw.page_flip);
        /**
         * 3 Options:
         *  1. setLooping(true) - Possible error that causes infinite loop
         *  2. play once - Possible down time between end of audio/done loading
         *  3. play it 2 times - even ground and safe bet
         */
        //this.flip.setLooping(true);
        this.flip.start();

        while (this.flip.isPlaying()) {

        }

        this.flip.start();
    }

    /**
     * Called when Async Task is complete
     */
    @Override
    protected void onDestroy() {
        this.flip.stop();
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);
        super.onDestroy();
    }

}
