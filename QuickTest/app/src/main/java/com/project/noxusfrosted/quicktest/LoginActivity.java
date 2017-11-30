package com.project.noxusfrosted.quicktest;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class LoginActivity extends AppCompatActivity {


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private ProfileTracker mProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {

            }
        };
        accessToken = AccessToken.getCurrentAccessToken();

        if (isLoggedIn()) {

            Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
            startActivity(intent);
            finish();

        }


        loginButton = findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {



            @Override
            public void onSuccess(LoginResult loginResult) {

                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                            Intent intent = new Intent(LoginActivity.this,
                                    IndexActivity.class);
                            startActivity(intent);
                            finish();

                            mProfileTracker.stopTracking();
                        }
                    };
                } else {

                    Profile.getCurrentProfile();
                    Intent intent = new Intent(LoginActivity.this,
                            IndexActivity.class);
                    startActivity(intent);
                    finish();

                }

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Try Again",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Error",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }




    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

}