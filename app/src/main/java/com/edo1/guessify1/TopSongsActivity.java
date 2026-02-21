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
        ImageButton btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        token = getIntent().getStringExtra("ACCESS_TOKEN");

        btnShort.setOnClickListener(v -> { updateSongs("short_term"); highlightButton(btnShort); });
        btnMedium.setOnClickListener(v -> { updateSongs("medium_term"); highlightButton(btnMedium); });
        btnLong.setOnClickListener(v -> { updateSongs("long_term"); highlightButton(btnLong); });

        updateSongs("medium_term");
        highlightButton(btnMedium);
    }

    private void updateSongs(String range) {
        if (token == null || token.isEmpty()) {
            tvSongsList.setText("שגיאה: אין טוקן");
            return;
        }

        // התיקון הקריטי: כתובת נקייה לחלוטין בלי שרשורים מיותרים
        // שים לב: שיניתי מ-artists ל-tracks כי אנחנו במסך שירים!
        String url = "https://api.spotify.com/v1/me/top/tracks?time_range=" + range + "&limit=10";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> tvSongsList.setText("שגיאת רשת: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "";
                if (response.isSuccessful()) {
                    runOnUiThread(() -> displayJson(responseBody));
                } else {
                    runOnUiThread(() -> {
                        tvSongsList.setText("קוד שגיאה: " + response.code() + "\nהשרת לא קיבל את הבקשה. וודא שה-URL תקין.");
                    });
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
                JSONObject track = items.getJSONObject(i);
                String songName = track.getString("name");
                String artistName = track.getJSONArray("artists").getJSONObject(0).getString("name");
                sb.append(i + 1).append(". ").append(songName).append("\n   ").append(artistName).append("\n\n");
            }
            if (items.length() == 0) sb.append("הרשימה ריקה.");
            tvSongsList.setText(sb.toString());
        } catch (Exception e) {
            runOnUiThread(() -> tvSongsList.setText("שגיאה בעיבוד הנתונים"));
        }
    }

    private void highlightButton(Button selectedButton) {
        Button[] buttons = {btnShort, btnMedium, btnLong};
        for (Button b : buttons) {
            int color = (b == selectedButton) ? Color.parseColor("#1DB954") : Color.parseColor("#282828");
            b.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }
}