package com.edo1.guessify1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1337;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
  viewModel=new ViewModelProvider(this).get(LoginViewModel.class);

        Button btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthorizationClient.openLoginActivity(MainActivity.this, REQUEST_CODE, viewModel.getLoginRequest());
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

                    // --- הקוד החדש מתחיל כאן ---
                    // אנחנו יוצרים "כוונה" (Intent) לעבור למסך הבית
                    Intent intentToHome = new Intent(MainActivity.this, HomePageActivity.class);

                    // אנחנו שולחים את הטוקן למסך הבא כדי שיוכל להשתמש בו
                    intentToHome.putExtra("ACCESS_TOKEN", response.getAccessToken());

                    // הפעלת המסך הבא
                    startActivity(intentToHome);

                    // סגירת המסך הנוכחי (כדי שאם יעשו "אחורה" לא יחזרו למסך התחברות)
                    finish();
                    // --- הקוד החדש נגמר כאן ---

                    break;
            }
        }
    }
}