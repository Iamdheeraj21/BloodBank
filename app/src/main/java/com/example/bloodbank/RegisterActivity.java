package com.example.bloodbank;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
    EditText name,username,email,password;
    ProgressDialog progressDialog;
    Button submit_button;
    FirebaseAuth firebaseAuth;
    CheckBox checkBox;
    DatabaseReference databaseReference;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        submit_button.setOnClickListener(v ->
        {
            String edittext_name,edittext_user,edittext_email,edittext_password;
            edittext_name=name.getText().toString();
            edittext_email=email.getText().toString();
            edittext_user=username.getText().toString();
            edittext_password=password.getText().toString();

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(checkBox.isChecked()){
                    checkBox.setText("Yes");
                }else {
                    checkBox.setText("No");
                }
            });

            if(edittext_email.equals(""))
                email.setError("Fill the blank!");
            else if(edittext_name.equals(""))
                name.setError("Fill the blank!");
            else if(edittext_user.equals(""))
                username.setError("Fill the blank!");
            else if(edittext_password.equals(""))
                password.setError("Fill the blank!");
            else if(edittext_email.length() >=7)
                password.setError("Please enter password at-least 8 digits");
            else
                if(checkBox.isChecked())
                    registerBloodDonor(edittext_name,edittext_user,edittext_email,edittext_password);
                else
                    registerNotBloodDonor(edittext_name,edittext_user,edittext_email,edittext_password);
        });
    }

    private void registerNotBloodDonor(String edit_name,String edit_username,String edit_email,String edit_password)
    {
        progressDialog.setTitle("Please wait few minutes...");
        progressDialog.show();
        firebaseAuth
                .createUserWithEmailAndPassword(edit_email,edit_password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String UserId = firebaseUser.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("OnlyUser").child(firebaseUser.getUid());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", UserId);
                        hashMap.put("fullname",edit_name);
                        hashMap.put("username", edit_username);
                        hashMap.put("email", edit_email);
                        hashMap.put("gender","");
                        hashMap.put("dob","");
                        hashMap.put("imageurl","");
                        hashMap.put("bloodgroup","");

                        databaseReference.setValue(hashMap).addOnCompleteListener(task1 ->
                        {
                            if(task1.isSuccessful()){
                           firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task2 -> {
                               progressDialog.dismiss();
                               Toast.makeText(RegisterActivity.this, "Account successfully created and check you email for verification..", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                            }
                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }).addOnFailureListener(e -> {
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void registerBloodDonor(String edit_name,String edit_username,String edit_email,String edit_password)
    {
        progressDialog.setTitle("Please wait few minutes...");
        progressDialog.show();
        firebaseAuth
                .createUserWithEmailAndPassword(edit_email,edit_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String UserId = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("BloodDonors").child(firebaseUser.getUid());
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", UserId);
                    hashMap.put("fullname",edit_name);
                    hashMap.put("username", edit_username);
                    hashMap.put("email", edit_email);
                    hashMap.put("gender","");
                    hashMap.put("dob","");
                    hashMap.put("imageurl","");
                    hashMap.put("bloodgroup","");

                    databaseReference.setValue(hashMap).addOnCompleteListener(task1 ->
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Account successfully created..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void initView()
    {
        name=findViewById(R.id.edittext1);
        email=findViewById(R.id.edittext3);
        username=findViewById(R.id.edittext2);
        checkBox=findViewById(R.id.checkbox1);
        password=findViewById(R.id.edittext4);
        progressDialog=new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}