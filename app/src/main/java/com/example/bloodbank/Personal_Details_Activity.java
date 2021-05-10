package com.example.bloodbank;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;
import de.hdodenhof.circleimageview.CircleImageView;

public class Personal_Details_Activity extends AppCompatActivity
{
    TextView textView_email,textView_name,textView_gender,textView_number,textView_bloodgroup,textView_dob;
    CircleImageView circleImageView;
    DatabaseReference databaseReference;
    String CurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        initViews();
        getInformationOfUser();
    }

    private void getInformationOfUser()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                    if(snapshot.exists())
                    {
                        String email=snapshot.child("email").getValue().toString();
                        String fullname=snapshot.child("fullname").getValue().toString();
                        String imageUrl=snapshot.child("imageurl").getValue().toString();
                        String dob=snapshot.child("dob").getValue().toString();
                        String phonenumber=snapshot.child("mobilenumber").getValue().toString();
                        String bloodGroup=snapshot.child("bloodgroup").getValue().toString();
                        String gender=snapshot.child("gender").getValue().toString();
                        if(imageUrl.equals("default"))
                            circleImageView.setImageResource(R.drawable.ic_baseline_person_24);
                        else
                            Glide.with(getApplicationContext()).load(imageUrl).into(circleImageView);
                        textView_email.setText(email);
                        textView_name.setText(fullname);
                        textView_bloodgroup.setText("BloodGroup :-"+bloodGroup);
                        textView_gender.setText("Gender :-"+gender);
                        textView_dob.setText("Date of Birth :-"+dob);
                        textView_number.setText("Phone Number :-"+phonenumber);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Alerter.create(Personal_Details_Activity.this)
                        .setTitle("Alert")
                        .setText(error.getMessage())
                        .setIcon(R.drawable.alerticon)
                        .setBackgroundColorRes(R.color.black)
                        .setDuration(2000)
                        .setOnClickListener(v -> {
                        }).setOnShowListener(() -> {
                        }).setOnHideListener(() -> {
                        }).show();
            }
        });
    }

    private void initViews()
    {
         textView_email=findViewById(R.id.textView_email);
         textView_name=findViewById(R.id.textView_name);
         textView_dob=findViewById(R.id.textView_dob);
         circleImageView=findViewById(R.id.imageview2);
         textView_bloodgroup=findViewById(R.id.textView_bloodGroup);
         textView_gender=findViewById(R.id.text_gender);
         textView_number=findViewById(R.id.textView_phone);
         CurrentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
         databaseReference= FirebaseDatabase.getInstance().getReference("AllUser").child(CurrentUser);
    }
}