package com.unknowncoder.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroActivity extends AppCompatActivity {
    Handler handler;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);
        handler=new Handler();

        handler.postDelayed(() -> {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if(firebaseUser != null && firebaseUser.isEmailVerified()){
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else
                startActivity(new Intent(this,StartActivity.class));
                finish();
        },2000);
    }
}