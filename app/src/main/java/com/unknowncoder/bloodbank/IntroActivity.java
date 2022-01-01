package com.unknowncoder.bloodbank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.Set;

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
            SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
            String check=sharedPreferences.getString("login","");
            if(check.equals("yes")){
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                startActivity(new Intent(this,StartActivity.class));
                finish();
            }


        },2000);
    }
}