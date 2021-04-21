package com.example.bloodbank;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    TextView textView_email,textView_name;
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
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                    if(snapshot.exists())
                    {
                        String email=snapshot.child("email").getValue().toString();
                        String fullname=snapshot.child("fullname").getValue().toString();
                        String imageUrl=snapshot.child("imageurl").getValue().toString();

                        if(imageUrl.equals("default"))
                            circleImageView.setImageResource(R.drawable.ic_baseline_person_24);
                        else
                            Glide.with(getApplicationContext()).load(imageUrl).into(circleImageView);

                        textView_email.setText(email);
                        textView_name.setText(fullname);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Alerter.create(Personal_Details_Activity.this)
                        .setTitle("Alert")
                        .setText(error.getMessage())
                        .setIcon(R.drawable.alerticon)
                        .setBackgroundColorRes(R.color.white)
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
         circleImageView=findViewById(R.id.imageview2);
         CurrentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
         databaseReference= FirebaseDatabase.getInstance().getReference("AllUser").child(CurrentUser);
    }
}