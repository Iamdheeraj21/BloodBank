package com.example.bloodbank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText editText1, editText2, editText3, editText4, editText5, editText6,editText7;
    Button btn1;
    CheckBox checkBox;
    RadioGroup radioGroup;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressBar bar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        radioGroup.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    RadioButton radioButton = group.findViewById(checkedId);
                });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkBox.isChecked())
                checkBox.setText("Yes");
            if (!checkBox.isChecked())
                checkBox.setText("No");
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText1.getText().toString();
                String username = editText2.getText().toString();
                String email = editText3.getText().toString();
                String password = editText4.getText().toString();
                String dob = editText5.getText().toString();
                String bloodGroup = editText6.getText().toString();
                String number=editText7.getText().toString();
                String gender="";
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = radioGroup.findViewById(selectedId);
                if(selectedId==-1)
                    Toast.makeText(RegisterActivity.this, "please choose you gender", Toast.LENGTH_SHORT).show();
                else
                    gender=radioButton.getText().toString();

                if (name.equals("") || username.equals("") || email.equals("") || password.equals("") || dob.equals("")
                        || bloodGroup.equals("") || number.equals(""))
                    Toast.makeText(RegisterActivity.this, "Please fill the blank", Toast.LENGTH_SHORT).show();
                else if (!(bloodGroup.length() == 2))
                    Toast.makeText(RegisterActivity.this, "Blood group only 2 character only...", Toast.LENGTH_SHORT).show();
                else if(!(number.length() ==10))
                    Toast.makeText(RegisterActivity.this,"Phone number atleast 10 digits..",Toast.LENGTH_SHORT).show();
                else if (password.length() <= 7)
                    Alerter.create(RegisterActivity.this)
                            .setTitle("Alert")
                            .setText("Password at least 8 digits")
                            .setIcon(R.drawable.alerticon)
                            .setBackgroundColorRes(R.color.white)
                            .setDuration(2000).setOnClickListener(v1 -> {
                    }).setOnShowListener(() -> {
                    }).setOnHideListener(() -> {
                    });
                else if (checkBox.isChecked())
                    registerBloodDonor(name, username, email, password, dob, bloodGroup, gender,number);

                if (!checkBox.isChecked())
                    registerOnlyUser(name, username, email, password, dob, bloodGroup,gender,number);
            }
        });
    }

    //Initialize the all views
    private void initViews() {
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        editText7 =findViewById(R.id.editText7);
        radioGroup = findViewById(R.id.radiogroup);
        firebaseAuth = FirebaseAuth.getInstance();
        checkBox = findViewById(R.id.checkbox);
        btn1 = findViewById(R.id.button1);
        bar=findViewById(R.id.pogressBarSignUp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        radioGroup.clearCheck();
        bar.setVisibility(View.INVISIBLE);
    }

    private void registerOnlyUser(String name, String username, String email, String password, String dob, String bloodGroup,String gender,String number)
    {
        bar.setVisibility(View.VISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                databaseReference=FirebaseDatabase.getInstance().getReference("OnlyUser").child(firebaseUser.getUid());
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("id",firebaseUser.getUid());
                hashMap.put("fullname",name);
                hashMap.put("username",username);
                hashMap.put("email",email);
                hashMap.put("dob",dob);
                hashMap.put("gender",gender);
                hashMap.put("bloodgroup",bloodGroup);
                hashMap.put("mobilenumber",number);

                databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful())
                    {
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task11 -> {
                            if(task11.isSuccessful()) {
                                AllUser();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }else{
                                bar.setVisibility(View.INVISIBLE);
                                btn1.setVisibility(View.VISIBLE);
                                Toast.makeText(RegisterActivity.this, task11.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        bar.setVisibility(View.INVISIBLE);
                        btn1.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(e ->
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                bar.setVisibility(View.INVISIBLE);
                btn1.setVisibility(View.VISIBLE);
    }

    private void registerBloodDonor(String name, String username, String email, String password, String dob, String bloodGroup,String gender,String number)
    {
        bar.setVisibility(View.VISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                databaseReference=FirebaseDatabase.getInstance().getReference("BloodDonor").child(firebaseUser.getUid());
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("id",firebaseUser.getUid());
                hashMap.put("fullname",name);
                hashMap.put("username",username);
                hashMap.put("email",email);
                hashMap.put("dob",dob);
                hashMap.put("imageurl","default");
                hashMap.put("gender",gender);
                hashMap.put("bloodgroup",bloodGroup);
                hashMap.put("mobilenumber",number);

                databaseReference.setValue(hashMap).addOnCompleteListener(task12 -> {
                    if(task12.isSuccessful())
                    {
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                AllUser();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }else{
                                bar.setVisibility(View.INVISIBLE);
                                btn1.setVisibility(View.VISIBLE);
                                Toast.makeText(RegisterActivity.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        bar.setVisibility(View.INVISIBLE);
                        btn1.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this, task12.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(e -> {
            bar.setVisibility(View.INVISIBLE);
            btn1.setVisibility(View.VISIBLE);
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    private void AllUser()
    {
        String name = editText1.getText().toString();
        String username = editText2.getText().toString();
        String email = editText3.getText().toString();
        String dob = editText5.getText().toString();
        String bloodGroup = editText6.getText().toString();
        String number=editText7.getText().toString();
        String gender="";
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = radioGroup.findViewById(selectedId);
        if(selectedId==-1)
            Toast.makeText(RegisterActivity.this, "please choose you gender", Toast.LENGTH_SHORT).show();
        else
            gender=radioButton.getText().toString();

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        databaseReference=FirebaseDatabase.getInstance().getReference("AllUser").child(firebaseUser.getUid());
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("fullname",name);
        hashMap.put("email",email);
        hashMap.put("username",username);
        hashMap.put("dob",dob);
        hashMap.put("gender",gender);
        hashMap.put("bloodgroup",bloodGroup);
        hashMap.put("imageurl","default");
        hashMap.put("mobilenumber",number);
        
        databaseReference.setValue(hashMap).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(RegisterActivity.this, "Information Added..", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show());
    }
}