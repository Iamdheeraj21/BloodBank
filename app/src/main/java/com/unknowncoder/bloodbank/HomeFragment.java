package com.unknowncoder.bloodbank;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Notifications.Notification;
import Notifications.NotificationAdapter;

public class HomeFragment extends Fragment {
    RecyclerView notification_recyclerView;
    FloatingActionButton send_request;
    NotificationAdapter adapter;
    DatabaseReference notificationRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        getAllNotifications();
        send_request.setOnClickListener(v ->{
            AlertDialog.Builder alertDialogBox;
            alertDialogBox=new AlertDialog.Builder(getContext());
            alertDialogBox.setTitle("Alert");
            alertDialogBox.setMessage("1. If your information is wrong and fake then take the action against you by the Police!\n" +
                    "2. So Fill the Correct information!");
            alertDialogBox.setPositiveButton("Okay", (dialog, which) ->
                    startActivity(new Intent(getContext(),Send_Blood_Request.class))).setNegativeButton("Cancel",null);
            alertDialogBox.show();
        });
        return view;
    }
    private void initViews(View view){
        send_request=view.findViewById(R.id.blood_request);
        notification_recyclerView=view.findViewById(R.id.notification_recyclerView);
        notification_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRef=FirebaseDatabase.getInstance().getReference().child("BankNotification");
    }

    private void getAllNotifications()
    {
        FirebaseRecyclerOptions<Notification> options=new FirebaseRecyclerOptions.Builder<Notification>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("BloodBankDepartmentNotification"),Notification.class)
                .build();
        adapter=new NotificationAdapter(options);
        notification_recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}