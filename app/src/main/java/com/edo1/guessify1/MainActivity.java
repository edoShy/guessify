package com.edo1.guessify1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;
public class MainActivity extends AppCompatActivity {

    // הגדרות חובה (תחליף במספרים שלך!)
    private static final String CLIENT_ID = "155e7ecccd1c47919e2bdddce534dd61";
    private static final String REDIRECT_URI = "guessify://callback";
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // פותח את ספוטיפיי
                AuthorizationRequest.Builder builder =
                        new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

                builder.setScopes(new String[]{"user-top-read"});
                builder.setShowDialog(true);

                AuthorizationRequest request = builder.build();

                AuthorizationClient.openLoginActivity(MainActivity.this, REQUEST_CODE, request);
            }
        });
    }

    // מה קורה כשחוזרים מספוטיפיי?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    // הצלחה!
                    Log.d("Spotify", "הטוקן הוא: " + response.getAccessToken());
                    Toast.makeText(this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                    break;

                case ERROR:
                    // כישלון
                    Toast.makeText(this, "שגיאה: " + response.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}