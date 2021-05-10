package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    BottomNavigationView bottomNavigationView;
    DatabaseReference databaseReference;
    TextView BG1,BG2,BG3,BG4,BG5,BG6,BG7,BG8;
    Button send_request;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getBloodUnitData();
        getTheUserInformation();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int item_Id=item.getItemId();
            switch (item_Id) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    return true;
                case R.id.bottom_blood_donors:
                    startActivity(new Intent(getApplicationContext(), FindBloodDonorsActivity.class));
                    return true;
                case R.id.bottom_settings:
                    startActivity(new Intent(getApplicationContext(),Personal_Details_Activity.class));
                    return true;
                default:
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    break;
            }
            return false;
        });
        send_request.setOnClickListener(v ->{
            AlertDialog.Builder alertDialogBox;
            alertDialogBox=new AlertDialog.Builder(this);
            alertDialogBox.setTitle("Alert");
            alertDialogBox.setMessage("1. If your information is wrong and fake then take the action against you by the Police!\n" +
                    "2. So Fill the Correct information!");
            alertDialogBox.setPositiveButton("Okay", (dialog, which) ->
            {
                startActivity(new Intent(MainActivity.this,Send_Blood_Request.class));
            }).setNegativeButton("Cancel",null);
            alertDialogBox.show();
        });
    }

    private void initViews()
    {
        BG1=findViewById(R.id.bloodGroup_AP);
        BG2=findViewById(R.id.bloodGroup_AN);
        BG3=findViewById(R.id.bloodGroup_BP);
        BG4=findViewById(R.id.bloodGroup_BN);
        BG5=findViewById(R.id.bloodGroup_OP);
        BG6=findViewById(R.id.bloodGroup_ON);
        BG7=findViewById(R.id.bloodGroup_ABP);
        BG8=findViewById(R.id.bloodGroup_ABN);
        send_request=findViewById(R.id.blood_request);
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("BloodGroupUnit");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_Id=item.getItemId();
        if(item_Id == R.id.personal_details){
            startActivity(new Intent(MainActivity.this,BloodRequestsActivity.class));
            return true;
        }else if(item_Id ==R.id.about_us){
            startActivity(new Intent(MainActivity.this,About_Us.class));
            return true;
        }else if(item_Id == R.id.logout){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Do you want to logout your account?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.setTitle("Account Logout!");
            alert.show();
            return true;
        }
        return false;
    }
    private void getBloodUnitData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists()){
                    String AP=snapshot.child("A+").getValue().toString();
                    String AN=snapshot.child("A-").getValue().toString();
                    String BP=snapshot.child("B+").getValue().toString();
                    String BN=snapshot.child("B-").getValue().toString();
                    String OP=snapshot.child("O+").getValue().toString();
                    String ON=snapshot.child("O-").getValue().toString();
                    String ABP=snapshot.child("AB+").getValue().toString();
                    String ABN=snapshot.child("AB-").getValue().toString();

                    BG1.setText("A+ :"+AP);
                    BG2.setText("A- :"+AN);
                    BG3.setText("B+ :"+BP);
                    BG4.setText("B- :"+BN);
                    BG5.setText("O+ :"+OP);
                    BG6.setText("O- :"+ON);
                    BG7.setText("AB+ :"+ABP);
                    BG8.setText("AB- :"+ABN);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTheUserInformation(){
        String currentUSerId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("AllUser");
        databaseReference.child(currentUSerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String imageUrl=snapshot.child("imageurl").getValue().toString();
                    if(imageUrl.equals("default")){
                        Toast.makeText(MainActivity.this, "Please upload your Passport size photo!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}