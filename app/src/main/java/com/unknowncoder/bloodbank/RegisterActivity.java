package com.unknowncoder.bloodbank;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    EditText editText1, editText2, editText3, editText4,editText7;
    TextView btn1,dobText,bloodGroup;
    CheckBox checkBox;
    String gender="";
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    RelativeLayout gMale,gFemale;
    ProgressBar bar;
    String dob="",blood_Group="";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkBox.isChecked())
                checkBox.setText("Yes");
            if (!checkBox.isChecked())
                checkBox.setText("No");
        });

        bloodGroup.setOnClickListener(view -> genderPopupShow());
        dobText.setOnClickListener(view -> showDOB());
        gMale.setOnClickListener(view -> {
            gender="male";
            gMale.setBackgroundResource(R.drawable.white_background);
            gFemale.setBackgroundResource(R.drawable.gender_bck);
        });

        gFemale.setOnClickListener(view -> {
            gender="female";
            gMale.setBackgroundResource(R.drawable.gender_bck);
            gFemale.setBackgroundResource(R.drawable.white_background);
        });

        btn1.setOnClickListener(v -> {
            String name = editText1.getText().toString();
            String username = editText2.getText().toString();
            String email = editText3.getText().toString();
            String password = editText4.getText().toString();
            String number=editText7.getText().toString();

            if(gender.equals("")){
                Snackbar.make(findViewById(android.R.id.content), "Please select your gender..",
                        Snackbar.LENGTH_LONG).show();
            }
            else if (name.equals("") || username.equals("") || email.equals("") || password.equals("")|| number.equals(""))
                Snackbar.make(findViewById(android.R.id.content), "Please fill the blank",
                        Snackbar.LENGTH_LONG).show();
            else if(dob.equals("")){
                Snackbar.make(findViewById(android.R.id.content), "Please select DOB",
                        Snackbar.LENGTH_LONG).show();
            }
            else if (blood_Group.equals(""))
                Snackbar.make(findViewById(android.R.id.content), "Please select the blood group",
                        Snackbar.LENGTH_LONG).show();
            else if(!(number.length() ==10))
                Snackbar.make(findViewById(android.R.id.content), "Phone number atleast 10 digits..",
                        Snackbar.LENGTH_LONG).show();
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
                registerBloodDonor(name, username, email, password, dob, blood_Group, gender,number);
            else if (!checkBox.isChecked())
            {
                registerOnlyUser(name, username, email, password, dob, blood_Group,gender,number);}
        });
    }

    //Initialize the all views
    private void initViews() {
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        dobText = findViewById(R.id.dobText);
        bloodGroup = findViewById(R.id.bloodGroup);
        editText7 =findViewById(R.id.editText7);
        gMale=findViewById(R.id.gMale);
        gFemale=findViewById(R.id.gFeMale);
        firebaseAuth = FirebaseAuth.getInstance();
        checkBox = findViewById(R.id.checkbox);
        btn1 = findViewById(R.id.button1);
        bar=findViewById(R.id.pogressBarSignUp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this,StartActivity.class));
        finish();
    }

    private void registerOnlyUser(String name, String username, String email, String password, String dob, String bloodGroup, String gender, String number)
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
                hashMap.put("user_type","user");
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
                hashMap.put("user_type","blood_donor");
                hashMap.put("imageurl","default");
                hashMap.put("gender",gender);
                hashMap.put("bloodgroup",blood_Group);
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
        String number=editText7.getText().toString();
        String gender="";

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        databaseReference=FirebaseDatabase.getInstance().getReference("AllUser").child(firebaseUser.getUid());
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("fullname",name);
        hashMap.put("email",email);
        hashMap.put("username",username);
        hashMap.put("dob",dob);
        hashMap.put("gender",gender);
        hashMap.put("bloodgroup",blood_Group);
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

    private void genderPopupShow(){
        PopupMenu popupMenu=new PopupMenu(this,bloodGroup);
        popupMenu.getMenuInflater().inflate(R.menu.blood_groups, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                bloodGroup.setText(menuItem.getTitle());
                blood_Group= String.valueOf(menuItem.getTitle());
                return true;
            }
        });
        popupMenu.show();
    }

    private void showDOB(){
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd MMMM yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                dob=sdf.format(myCalendar.getTime());
                dobText.setText(sdf.format(myCalendar.getTime()));
            }
        };
        new DatePickerDialog(RegisterActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar
                .get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}