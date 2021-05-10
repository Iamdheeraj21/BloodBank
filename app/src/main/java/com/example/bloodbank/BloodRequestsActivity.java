package com.example.bloodbank;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.BloodRequest;
import Adapter.BloodRequestAdapter;

public class BloodRequestsActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    BloodRequestAdapter bloodRequestAdapter;
    ArrayList<BloodRequest> bloodRequests;
    String currentUser="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_requests);
        initViews();
        getTheAlRequests();
    }

    private void getTheAlRequests()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists()){
                     String app_no=  snapshot.child("applicationo").getValue().toString();
                     String app_name=  snapshot.child("fullname").getValue().toString();
                     String app_bloodGroup= snapshot.child("bloodgroupname").getValue().toString();
                     String app_number= snapshot.child("phonenumber").getValue().toString();
                     String app_status= snapshot.child("status").getValue().toString();

                    bloodRequests.add(new BloodRequest(app_no,app_name,app_bloodGroup,app_number,app_status));
                    buildRecyclerView();
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(BloodRequestsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buildRecyclerView()
    {
        // initializing our adapter class.
        bloodRequestAdapter = new BloodRequestAdapter( BloodRequestsActivity.this,bloodRequests);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(bloodRequestAdapter);
    }

    private void initViews()
    {
        recyclerView=findViewById(R.id.requests_recyclerView);
        currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        bloodRequests=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("BloodRequest").child(currentUser);
    }

}