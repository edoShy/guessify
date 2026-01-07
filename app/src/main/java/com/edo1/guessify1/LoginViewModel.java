package com.edo1.guessify1;

import androidx.lifecycle.ViewModel;

import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class LoginViewModel extends ViewModel {

    // הגדרות חובה (תחליף במספרים שלך!)
    private static final String CLIENT_ID = "155e7ecccd1c47919e2bdddce534dd61";
    private static final String REDIRECT_URI = "guessify://callback";

    public AuthorizationRequest getLoginRequest() {
        // פותח את ספוטיפיי
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-top-read"});
        builder.setShowDialog(true);

        AuthorizationRequest request = builder.build();
        return request;

    }
}
