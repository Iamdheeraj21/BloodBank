package com.unknowncoder.bloodbank;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class Forget_Password extends AppCompatActivity
{
    EditText editText1,editText2;
    TextView textView,btn1;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder alertDialog;
    Random random;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        setContentView(R.layout.activity_forget_password);
        initViews();
        alertDialog=new AlertDialog.Builder(this);
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
                    forgetPasswordOfId(email);
        });
    }

    private void forgetPasswordOfId(String email)
    {
        progressBar.setVisibility(View.VISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(Forget_Password.this,"Please check your email!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Forget_Password.this,LoginActivity.class));
            }else {
                progressBar.setVisibility(View.INVISIBLE);
                btn1.setVisibility(View.VISIBLE);
                String error=task.getException().getMessage();
                Toast.makeText(Forget_Password.this,error,Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.INVISIBLE);
            btn1.setVisibility(View.VISIBLE);
        });
    }

    private void initViews()
    {
        editText1=findViewById(R.id.forget_email_password);
        editText2=findViewById(R.id.random_recaptcha);
        btn1=findViewById(R.id.send_forget_password);
        textView=findViewById(R.id.random_number);
        progressBar=findViewById(R.id.progressbar_Forget);
        firebaseAuth= FirebaseAuth.getInstance();
        random=new Random();
        int recaptcha_num=random.nextInt(2500)+5000;
        String recaptcha_number=String.valueOf(recaptcha_num);
        textView.setText(recaptcha_number);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Forget_Password.this,LoginActivity.class));
        finish();
    }
}