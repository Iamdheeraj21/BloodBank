package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Forget_Password extends AppCompatActivity
{
    EditText editText1,editText2;
    Button btn1;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initViews();


    }

    private void initViews()
    {
        editText1=findViewById(R.id.forget_email_password);
        editText2=findViewById(R.id.random_recaptcha);
        btn1=findViewById(R.id.send_forget_password);
        textView=findViewById(R.id.random_number);
    }
}