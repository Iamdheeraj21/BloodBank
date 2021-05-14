package com.example.bloodbank;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Notifications.Notification;
import Notifications.NotificationAdapter;

public class MainActivity extends AppCompatActivity
{
    BottomNavigationView bottomNavigationView;
    DatabaseReference databaseReference,notificationRef;
    RecyclerView notification_recyclerView;
    FloatingActionButton send_request;
    NotificationAdapter adapter;
    CardView cardView_notification;
    String CurrentUSerId;
    TextView textView_Notification;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
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
                    startActivity(new Intent(MainActivity.this,Send_Blood_Request.class))).setNegativeButton("Cancel",null);
            alertDialogBox.show();
        });
    }

    private void initViews()
    {
        send_request=findViewById(R.id.blood_request);
        notification_recyclerView=findViewById(R.id.notification_recyclerView);
        notification_recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("BloodGroupUnit");
        notificationRef=FirebaseDatabase.getInstance().getReference().child("BankNotification");
        textView_Notification=findViewById(R.id.notification_heading);
        cardView_notification=findViewById(R.id.cardView_Notification);
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
            Intent intent=new Intent(MainActivity.this,BloodRequestsActivity.class);
            CurrentUSerId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            intent.putExtra("uid",CurrentUSerId);
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

    private void getAllNotifications()
    {
        FirebaseRecyclerOptions<Notification> options=new FirebaseRecyclerOptions.Builder<Notification>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("BloodBankDepartmentNotification"),Notification.class)
                .build();
        adapter=new NotificationAdapter(options);
        notification_recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllNotifications();
        adapter.startListening();
    }
}