package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Displays splashscreen
 */
@SuppressWarnings("JavaDoc")
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    /**
     * Creates the splash screen layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //auth object;

        //current user object = it is the currently logged in user. If not logged in, this is null.
        final FirebaseUser currentUser= firebaseAuth.getCurrentUser();


        Handler handler = new Handler();

        handler.postDelayed(() -> {
            if(currentUser!=null) {
                //user is already logged in.
                Intent homeIntent = new Intent(SplashActivity.this, NoticeBoard.class);
                startActivity(homeIntent);
            }
            else {
                //user is not logged in, then show login activity.
                Intent loginIntent = new Intent(SplashActivity.this, LogIn.class);
                startActivity(loginIntent);
            }

        },3000); //3 sec

    }
}