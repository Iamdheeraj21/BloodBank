package com.example.bloodbank;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class LoginActivity extends AppCompatActivity
{
    Button btn1,btn2,btn3;
    TextView recaptcha;
    FirebaseAuth firebaseAuth;
    EditText email,password,editText_recaptcha;
    Random random;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        btn2.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this,Forget_Password.class)
                )
        );
        btn1.setOnClickListener(v -> {
            String email_edittext,password_edittext,recaptcha_edittext;
            String random_number;
            random_number=recaptcha.getText().toString();
            email_edittext=email.getText().toString();
            password_edittext=password.getText().toString();
            recaptcha_edittext=editText_recaptcha.getText().toString();
            if(email_edittext.equals(""))
                email.setError("Fill the blank!");
            else if(password_edittext.equals(""))
                password.setError("Fill the blank");
            else if(recaptcha_edittext.equals(""))
                editText_recaptcha.setError("Fill the blank");
            else if(!recaptcha_edittext.equals(random_number))
                editText_recaptcha.setError("Please enter correct recaptcha!");
            else{
                loginProcess(email_edittext,password_edittext);
            }
        });

        btn3.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        });
    }

    private void loginProcess(String email_edittext, String password_edittext)
    {
        progressBar.setVisibility(View.VISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email_edittext,password_edittext)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.INVISIBLE);
                        btn1.setVisibility(View.VISIBLE);
                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);}
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            btn1.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this,"Please verify emailAddress",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                btn1.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView()
    {
        btn1=findViewById(R.id.login_button);
        btn2=findViewById(R.id.forget_password);
        btn3=findViewById(R.id.register_button);
        recaptcha=findViewById(R.id.recaptcha);
        email=findViewById(R.id.Login_edittext1);
        password=findViewById(R.id.Login_edittext2);
        editText_recaptcha=findViewById(R.id.edittext_recaptcha);
        firebaseAuth= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressbar_signIn);
        random=new Random();
        int recaptcha_num=random.nextInt(2500)+5000;
        String recaptcha_number=String.valueOf(recaptcha_num);
        recaptcha.setText(recaptcha_number);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AlertDialog.Builder alert=new AlertDialog.Builder(LoginActivity.this);
        alert.setMessage("If you are new user then first verify your email(Check your email)")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoginActivity.this,"Thank you",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=alert.create();
        alertDialog.setTitle("Notice");
        alertDialog.show();
    }
}