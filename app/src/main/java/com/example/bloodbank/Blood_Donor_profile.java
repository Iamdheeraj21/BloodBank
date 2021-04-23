package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Blood_Donor_profile extends AppCompatActivity
{
    String blood_donor_id="";
    DatabaseReference databaseReference;
    TextView textView_name,textView_email,textView_bloodGroup;
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donor_profile);
        initViews();
        blood_donor_id=getIntent().getExtras().get("blood_donor_id").toString();
        getBloodDonorDetails();

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
    }
}