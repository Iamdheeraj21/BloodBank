package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import com.unknowncoder.bloodbank.R;

public class Blood_Donor_profile extends AppCompatActivity
{
    String blood_donor_id="";
    DatabaseReference databaseReference;
    TextView textView_name,textView_email,textView_bloodGroup;
    CircleImageView circleImageView;
    FloatingActionButton call_btn,email_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donor_profile);
        initViews();
        blood_donor_id=getIntent().getExtras().get("blood_donor_id").toString();
        getBloodDonorDetails();

        call_btn.setOnClickListener(v-> {
            callTheBloodDonor();
        });
        email_btn.setOnClickListener(v->{
            emailTheBloodDonor();
        });
    }

    private void callTheBloodDonor()
    {
        databaseReference.child(blood_donor_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String phoneNumber=snapshot.child("mobilenumber").getValue().toString();
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        //Creating intents for making a call
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        callIntent.setData(Uri.parse("tel:"+phoneNumber.trim()));
                        getApplicationContext().startActivity(callIntent);

                    }else{
                        Toast.makeText(getApplicationContext(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(Blood_Donor_profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void emailTheBloodDonor()
    {
        databaseReference.child(blood_donor_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String email=snapshot.child("email").getValue().toString();
                    Intent intent=new Intent(Blood_Donor_profile.this,Send_Mail_To_Donor.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }else{
                    Toast.makeText(Blood_Donor_profile.this, "Email is not exists...", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(Blood_Donor_profile.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Get the Blood Donor Details
    private void getBloodDonorDetails()
    {
        databaseReference.child(blood_donor_id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String name=snapshot.child("fullname").getValue().toString();
                    String email=snapshot.child("email").getValue().toString();
                    String bloodGroup=snapshot.child("bloodgroup").getValue().toString();
                    String imageUrl=snapshot.child("imageurl").getValue().toString();

                    if(!imageUrl.equals("default"))
                        Glide.with(getApplicationContext()).load(imageUrl).into(circleImageView);
                    else
                        circleImageView.setImageResource(R.drawable.ic_baseline_person_24);

                    textView_name.setText("Name :"+name);
                    textView_email.setText("Email :" +email);
                    textView_bloodGroup.setText("Blood Group :"+bloodGroup);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Blood_Donor_profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews()
    {
        textView_name=findViewById(R.id.textview2);
        textView_email=findViewById(R.id.textview3);
        textView_bloodGroup=findViewById(R.id.textview5);
        circleImageView=findViewById(R.id.circle_Image_view1);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("AllUser");
        call_btn=findViewById(R.id.call_btn);
        email_btn=findViewById(R.id.email_btn);
    }
}