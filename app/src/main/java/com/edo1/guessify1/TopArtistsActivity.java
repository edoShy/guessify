package com.edo1.guessify1;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.*;
import java.io.IOException;

public class TopArtistsActivity extends AppCompatActivity {
    private String token;
    private TextView tvArtistsList;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private Button btnShort, btnMedium, btnLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_artists);

        tvArtistsList = findViewById(R.id.tvArtistsList);
        btnShort = findViewById(R.id.btnShort);
        btnMedium = findViewById(R.id.btnMedium);
        btnLong = findViewById(R.id.btnLong);
        ImageButton btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        token = getIntent().getStringExtra("ACCESS_TOKEN");

        btnShort.setOnClickListener(v -> { updateArtists("short_term"); highlightButton(btnShort); });
        btnMedium.setOnClickListener(v -> { updateArtists("medium_term"); highlightButton(btnMedium); });
        btnLong.setOnClickListener(v -> { updateArtists("long_term"); highlightButton(btnLong); });

        updateArtists("medium_term");
        highlightButton(btnMedium);
    }

    private void updateArtists(String range) {
        if (token == null) return;
        // התיקון הקריטי ב-URL: הוספת סימן השאלה
        String url = "https://api.spotify.com/v1/me/top/artists" + "?time_range=" + range + "&limit=10";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {}
            @Override public void onResponse(Call call, Response response) throws IOException {
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
            for (int i = 0; i < items.length(); i++) {
                sb.append(i + 1).append(". ").append(items.getJSONObject(i).getString("name")).append("\n\n");
            }
            tvArtistsList.setText(sb.toString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void highlightButton(Button selectedButton) {
        Button[] buttons = {btnShort, btnMedium, btnLong};
        for (Button b : buttons) {
            int color = (b == selectedButton) ? Color.parseColor("#1DB954") : Color.parseColor("#282828");
            b.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }
}