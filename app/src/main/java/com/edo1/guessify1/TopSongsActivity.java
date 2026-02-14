package com.edo1.guessify1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.*;
import java.io.IOException;

public class TopSongsActivity extends AppCompatActivity {

    private String token;
    private TextView tvSongsList;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private Button btnShort, btnMedium, btnLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_songs);

        tvSongsList = findViewById(R.id.tvSongsList);
        btnShort = findViewById(R.id.btnShort);
        btnMedium = findViewById(R.id.btnMedium);
        btnLong = findViewById(R.id.btnLong);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        token = getIntent().getStringExtra("ACCESS_TOKEN");

        btnShort.setOnClickListener(v -> { updateSongs("short_term"); highlightButton(btnShort); });
        btnMedium.setOnClickListener(v -> { updateSongs("medium_term"); highlightButton(btnMedium); });
        btnLong.setOnClickListener(v -> { updateSongs("long_term"); highlightButton(btnLong); });

        // טעינה ראשונית - חצי שנה
        updateSongs("medium_term");
        highlightButton(btnMedium);
    }

    private void updateSongs(String range) {
        String url = "https://api.spotify.com/v1/me/top/tracks?time_range=" + range + "&limit=5";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { e.printStackTrace(); }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    runOnUiThread(() -> displayJson(data));
                }
            }
        });
    }

    private void displayJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray items = jsonObject.getJSONArray("items");
            StringBuilder sb = new StringBuilder();

            int limit = Math.min(items.length(), 5);

            for (int i = 0; i < limit; i++) {
                JSONObject track = items.getJSONObject(i);
                String songName = track.getString("name");
                String artistName = track.getJSONArray("artists").getJSONObject(0).getString("name");

                sb.append(i + 1).append(". ").append(songName)
                        .append("\n   ").append(artistName).append("\n\n");
            }
            tvSongsList.setText(sb.toString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void highlightButton(Button selectedButton) {
        Button[] buttons = {btnShort, btnMedium, btnLong};
        for (Button b : buttons) {
            if (b == selectedButton) {
                b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1DB954")));
                b.setTextColor(android.graphics.Color.WHITE);
            } else {
                b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#282828")));
                b.setTextColor(android.graphics.Color.parseColor("#B3B3B3"));
            }
        }
    }
}