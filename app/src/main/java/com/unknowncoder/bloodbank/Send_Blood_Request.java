package com.unknowncoder.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Send_Blood_Request extends AppCompatActivity
{
    Spinner spinner;
    Toast toast;
    TextView toastName,bandepartmentuid;
    EditText editText1,editText2;
    CircleImageView circleImageView;
    Button button;
    Random random;
    String bloodBankUid;
    MediaPlayer mediaPlayer;
    String[] BloodGroup={"Select One","A+","A-","B+","B-","O+","O-","AB+","AB-"};
    LayoutInflater layoutInflater;
    String bloodGroupName="";
    ProgressDialog progressDialog;
    DatabaseReference infoRef;
    String current_user="";
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_blood_request);
        initViews();
        getInformation();
        mediaPlayer = MediaPlayer.create(this, R.raw.submitringtone);
        arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BloodGroup);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              if(BloodGroup[position].equals("Select One"))
                  Toast.makeText(Send_Blood_Request.this, "Please select correct Blood Group!", Toast.LENGTH_SHORT).show();
              else
                  bloodGroupName=BloodGroup[position];
          }
          @Override
          public void onNothingSelected(AdapterView<?> parent) {
          }
      });

        button.setOnClickListener(v -> {
            String name=editText1.getText().toString();
            String phoneNumber=editText2.getText().toString();

            if(name.equals("") || phoneNumber.equals(""))
                Alerter.create(Send_Blood_Request.this)
                        .setTitle("Alert")
                        .setText("Please fill the blank")
                        .setIcon(R.drawable.alerticon)
                        .setBackgroundColorRes(R.color.black)
                        .setDuration(2000)
                        .setOnClickListener(v1 -> {
                        }).setOnShowListener(() -> {
                }).setOnHideListener(() -> {
                }).show();
            else
                submitBloodRequest(name,phoneNumber,bloodGroupName);

        });

    }

    private void submitBloodRequest(String name, String phoneNumber, String bloodGroupName)
    {
        progressDialog.show();
        bloodBankUid=bandepartmentuid.getText().toString();
        int recaptcha_num=random.nextInt(2500)+5000;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateData=formatter.format(date);
        String application_number=String.valueOf(recaptcha_num);
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("applicationo",application_number);
        hashMap.put("fullname",name);
        hashMap.put("phonenumber",phoneNumber);
        hashMap.put("bloodgroupname",bloodGroupName);
        hashMap.put("date",dateData);
        hashMap.put("status","Active");
        DatabaseReference databaseReference1;
        databaseReference1=FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("BloodBankRequest").child(current_user).setValue(hashMap).addOnCompleteListener(task ->
        {
            if(task.isSuccessful()){
                mediaPlayer.start();
                progressDialog.dismiss();
                startActivity(new Intent(Send_Blood_Request.this,MainActivity.class));
                toast.show();
            }else {
                progressDialog.dismiss();
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(Send_Blood_Request.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void getInformation()
    {
        infoRef.child(current_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
                    String phonenumber=sharedPreferences.getString("mobilenumber","");
                    String name=sharedPreferences.getString("fullname","");
                    editText2.setText(phonenumber);
                    editText1.setText(name);
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {
                Toast.makeText(Send_Blood_Request.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews()
    {
        layoutInflater=getLayoutInflater();
        View customView=layoutInflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.custom_toast));
        //Set the custom progress dialog box
        progressDialog=new ProgressDialog(this);
        // Set the Custom Toast View
        circleImageView=customView.findViewById(R.id.tick);
        toastName=customView.findViewById(R.id.toast_textview);
        toast=new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customView);
        spinner=findViewById(R.id.blood_group_spinner);
        editText1=findViewById(R.id.editText7);
        editText2=findViewById(R.id.editText8);
        button=findViewById(R.id.submit_request);
        bandepartmentuid=findViewById(R.id.bandeparmentuid);
        random=new Random();
        current_user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        infoRef= FirebaseDatabase.getInstance().getReference().child("AllUser");
        getAndSetUid();
    }

    private void getAndSetUid()
    {
        DatabaseReference databaseReference2;
        databaseReference2=FirebaseDatabase.getInstance().getReference().child("BankDepartmentUid");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    bloodBankUid=snapshot.child("uid").getValue().toString();
                    bandepartmentuid.setText(bloodBankUid);

                }else Toast.makeText(Send_Blood_Request.this, "Not available", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Send_Blood_Request.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}