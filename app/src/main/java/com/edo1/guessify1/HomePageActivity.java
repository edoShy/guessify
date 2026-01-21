package com.edo1.guessify1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class HomePageActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String accessToken; // נשמור את הטוקן כמשתנה גלובלי לשימוש עתידי

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // 1. חיבור רכיבי ה-UI
        tvWelcome = findViewById(R.id.tvWelcome);
        Button btnTopSongs = findViewById(R.id.btnTopSongs);
        Button btnTopArtists = findViewById(R.id.btnTopArtists);
        Button btnMinutes = findViewById(R.id.btnListeningMinutes);
        Button btnGame = findViewById(R.id.btnGuessingGame);
        Button btnRate = findViewById(R.id.btnRateSongs);

        // 2. שליפת הטוקן מה-Intent
        accessToken = getIntent().getStringExtra("ACCESS_TOKEN");

        if (accessToken != null) {
            // קריאה לפונקציה שמושכת את השם מספוטיפיי
            fetchUserProfile(accessToken);
        } else {
            tvWelcome.setText("שלום משתמש ספוטיפיי!");
        }

        // 3. הגדרת כפתורים (מה שהיה לך קודם)
        btnTopSongs.setOnClickListener(v -> {
            Toast.makeText(this, "בקרוב: 5 השירים המושמעים", Toast.LENGTH_SHORT).show();
        });

        btnTopArtists.setOnClickListener(v -> {
            Toast.makeText(this, "בקרוב: 10 האומנים המושמעים", Toast.LENGTH_SHORT).show();
        });

        // כאן אפשר להוסיף מאזינים לשאר הכפתורים...
    }

    private void fetchUserProfile(String token) {
        // בקשה ל-Endpoint של ספוטיפיי לקבלת פרטי משתמש
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("SpotifyAPI", "שגיאה בחיבור: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        String displayName = jsonObject.getString("display_name");

                        // עדכון הטקסט במסך (חייב להתבצע על ה-Main Thread)
                        runOnUiThread(() -> {
                            tvWelcome.setText("שלום, " + displayName + "!");
                        });
                    } catch (JSONException e) {
                        Log.e("SpotifyAPI", "שגיאה בפענוח הנתונים");
                    }
                } else {
                    Log.e("SpotifyAPI", "תגובה לא תקינה מהשרת");
                }
            }
        });
    }
}