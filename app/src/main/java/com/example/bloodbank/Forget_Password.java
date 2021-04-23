package com.example.bloodbank;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class Forget_Password extends AppCompatActivity
{
    EditText editText1,editText2;
    Button btn1;
    TextView textView;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder alertDialog;
    ProgressDialog progressDialog;
    Random random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initViews();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure your email-address is correct?");
        btn1.setOnClickListener(v -> {
            String email,recaptcha_number,recaptcha_textview;
            email=editText1.getText().toString();
            recaptcha_number=editText2.getText().toString();
            recaptcha_textview=textView.getText().toString();

            if(email.equals("") || recaptcha_number.equals(""))
                Toast.makeText(Forget_Password.this, "Please fill the blank...", Toast.LENGTH_SHORT).show();
            else if (!recaptcha_number.equals(recaptcha_textview))
                Toast.makeText(Forget_Password.this, "Please enter correct recaptcha..", Toast.LENGTH_SHORT).show();
            else
                alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                    progressDialog.setMessage("Please wait few minutes...");
                    progressDialog.show();
                    forgetPasswordOfId(email);
                }).setNegativeButton("No",null);
            alertDialog.show();
        });
    }

    private void forgetPasswordOfId(String email)
    {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                progressDialog.dismiss();
                Toast.makeText(Forget_Password.this,"Please check your email!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Forget_Password.this,LoginActivity.class));
            }else {
                progressDialog.dismiss();
                String error=task.getException().getMessage();
                Toast.makeText(Forget_Password.this,error,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews()
    {
        editText1=findViewById(R.id.forget_email_password);
        editText2=findViewById(R.id.random_recaptcha);
        btn1=findViewById(R.id.send_forget_password);
        textView=findViewById(R.id.random_number);
        firebaseAuth= FirebaseAuth.getInstance();
        random=new Random();
        int recaptcha_num=random.nextInt(2500)+5000;
        String recaptcha_number=String.valueOf(recaptcha_num);
        textView.setText(recaptcha_number);
    }
}