package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Send_Blood_Request extends AppCompatActivity
{
    Spinner spinner;
    Toast toast;
    TextView toastName;
    EditText editText1,editText2;
    CircleImageView circleImageView;
    Button button;
    MediaPlayer mediaPlayer;
    String[] BloodGroup={"Select One","A+","A-","B+","B-","O+","O-","AB+","AB-"};
    LayoutInflater layoutInflater;
    String bloodGroupName="";
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
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
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("fullname",name);
        hashMap.put("phonenumber",phoneNumber);
        hashMap.put("bloodgroupname",bloodGroupName);
        DatabaseReference databaseReference1 ;
        databaseReference1=FirebaseDatabase.getInstance().getReference().child("BloodRequest");
        String RequestKey=databaseReference1.push().getKey();
        assert RequestKey != null;
        databaseReference1.child(current_user).child(RequestKey).setValue(hashMap).addOnCompleteListener(task ->
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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void getInformation()
    {
        databaseReference.child(current_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String name=snapshot.child("fullname").getValue().toString();
                    //String phonenumber=snapshot.child("phonenumber").getValue().toString();
                    //editText2.setText(phonenumber);
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
        current_user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("AllUser");
    }
}