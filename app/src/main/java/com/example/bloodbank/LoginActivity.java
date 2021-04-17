package com.example.bloodbank;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    Button btn1,btn2;
    TextView recaptcha;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    EditText email,password,editText_recaptcha;
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

    }

    private void loginProcess(String email_edittext, String password_edittext)
    {
        firebaseAuth.signInWithEmailAndPassword(email_edittext,password_edittext)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful()){
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                            progressDialog.dismiss();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);}
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"Please verify emailAddress",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView()
    {
        btn1=findViewById(R.id.login_button);
        btn2=findViewById(R.id.forget_password);
        recaptcha=findViewById(R.id.recaptcha);
        email=findViewById(R.id.Login_edittext1);
        password=findViewById(R.id.Login_edittext2);
        editText_recaptcha=findViewById(R.id.edittext_recaptcha);
        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait few moments...");
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
        int min = 1000;
        int max = 9000;
        int recaptchaNumber=(int)Math.floor(Math.random()*(max-min+1)+min);
        recaptcha.setText(recaptchaNumber);
    }
}