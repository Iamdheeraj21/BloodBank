package com.unknowncoder.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {
    TextView sign_button,create_account_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        setContentView(R.layout.activity_start);
        initViews();
        sign_button.setOnClickListener(view -> {
            startActivity(new Intent(StartActivity.this,
                    LoginActivity.class));
            finish();
        });
        create_account_button.setOnClickListener(view -> {
            startActivity(new Intent(StartActivity.this,RegisterActivity.class));
            finish();
        });
    }


    private void initViews() {
        create_account_button=findViewById(R.id.create_account_button);
        sign_button=findViewById(R.id.sign_button);
    }
}