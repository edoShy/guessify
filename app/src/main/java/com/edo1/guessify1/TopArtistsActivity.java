package com.edo1.guessify1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

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

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        token = getIntent().getStringExtra("ACCESS_TOKEN");

        // הגדרת המאזינים בדיוק כמו בשירים
        btnShort.setOnClickListener(v -> { updateArtists("short_term"); highlightButton(btnShort); });
        btnMedium.setOnClickListener(v -> { updateArtists("medium_term"); highlightButton(btnMedium); });
        btnLong.setOnClickListener(v -> { updateArtists("long_term"); highlightButton(btnLong); });

        // טעינה ראשונית - בדיוק כמו בשירים
        updateArtists("medium_term");
        highlightButton(btnMedium);
    }

    private void updateArtists(String range) {
        if (token == null) {
            tvArtistsList.setText("שגיאה: חסר טוקן");
            return;
        }

        String url = "https://api.spotify.com/v1/me/top/artists" + "?time_range=" + range + "&limit=10";

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
                } else {
                    // אם עדיין יש שגיאה, נראה את הקוד שלה
                    runOnUiThread(() -> tvArtistsList.setText("שגיאה מהשרת: " + response.code()));
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
                String name = items.getJSONObject(i).getString("name");
                sb.append(i + 1).append(". ").append(name).append("\n\n");
            }

            tvArtistsList.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            tvArtistsList.setText("שגיאה בעיבוד הנתונים");
        }
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