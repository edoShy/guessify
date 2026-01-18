package com.edo1.guessify1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // 1. הגדרת הכותרת (בינתיים נכתוב טקסט קבוע)
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("שלום משתמש ספוטיפיי!");

        // 2. חיבור הכפתורים מהעיצוב לקוד
        Button btnTopSongs = findViewById(R.id.btnTopSongs);
        Button btnTopArtists = findViewById(R.id.btnTopArtists);
        Button btnMinutes = findViewById(R.id.btnListeningMinutes);
        Button btnGame = findViewById(R.id.btnGuessingGame);
        Button btnRate = findViewById(R.id.btnRateSongs);

        // 3. הגדרה מה קורה כשלוחצים על כפתור (בינתיים רק הודעה קופצת)
        btnTopSongs.setOnClickListener(v -> {
            Toast.makeText(this, "בקרוב: 5 השירים המושמעים", Toast.LENGTH_SHORT).show();
        });

        btnTopArtists.setOnClickListener(v -> {
            Toast.makeText(this, "בקרוב: 10 האומנים המושמעים", Toast.LENGTH_SHORT).show();
        });

        // אפשר להוסיף ככה לכל שאר הכפתורים...
    }
}