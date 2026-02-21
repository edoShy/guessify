package com.edo1.guessify1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        accessToken = getIntent().getStringExtra("ACCESS_TOKEN");

        // כפתור אמנים
        View cardArtists = findViewById(R.id.cardTopArtists);
        if (cardArtists != null) {
            cardArtists.setOnClickListener(v -> {
                Intent intent = new Intent(this, TopArtistsActivity.class);
                intent.putExtra("ACCESS_TOKEN", accessToken);
                startActivity(intent);
            });
        }

        // כפתור שירים - זה היה חסר לך!
        View cardSongs = findViewById(R.id.cardTopSongs);
        if (cardSongs != null) {
            cardSongs.setOnClickListener(v -> {
                Intent intent = new Intent(this, TopSongsActivity.class);
                intent.putExtra("ACCESS_TOKEN", accessToken);
                startActivity(intent);
            });
        }
    }
}