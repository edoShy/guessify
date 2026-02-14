package com.edo1.guessify1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        tvWelcome = findViewById(R.id.tvWelcome);
        Button btnTopArtists = findViewById(R.id.btnTopArtists);
        Button btnTopSongs = findViewById(R.id.btnTopSongs);

        accessToken = getIntent().getStringExtra("ACCESS_TOKEN");
        if (accessToken != null) fetchUserProfile(accessToken);

        btnTopArtists.setOnClickListener(v -> {
            if (accessToken != null) fetchTopArtists(accessToken);
        });

        btnTopSongs.setOnClickListener(v -> {
            if (accessToken != null) {
                Intent intent = new Intent(HomePageActivity.this, TopSongsActivity.class);
                intent.putExtra("ACCESS_TOKEN", accessToken);
                startActivity(intent);
            }
        });
    }

    private void fetchUserProfile(String token) {
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {}
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String data = response.body().string();
                        JSONObject json = new JSONObject(data);
                        String name = json.getString("display_name");
                        runOnUiThread(() -> tvWelcome.setText("שלום, " + name + "!"));
                    } catch (JSONException e) {}
                }
            }
        });
    }

    private void fetchTopArtists(String token) {
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range=medium_term&limit=10") // הכתובת המקורית שלך
                .addHeader("Authorization", "Bearer " + token)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {}
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    runOnUiThread(() -> {
                        Intent intent = new Intent(HomePageActivity.this, TopArtistsActivity.class);
                        intent.putExtra("ARTISTS_DATA", data); // שולח את הנתונים הראשוניים

                        // --- התיקון הקריטי כאן! ---
                        intent.putExtra("ACCESS_TOKEN", accessToken);
                        // -------------------------

                        startActivity(intent);
                    });
                }
            }
        });

        }
    }