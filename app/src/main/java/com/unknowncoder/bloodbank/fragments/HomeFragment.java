package com.unknowncoder.bloodbank.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unknowncoder.bloodbank.R;

import java.util.Objects;

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
                    showBloodBankRequest())
                    .setNegativeButton("Cancel",null);
            alertDialogBox.show();
        });
        return view;
    }

    private void showBloodBankRequest()
    {
        AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
        View view=LayoutInflater.from(getContext()).inflate(R.layout.activity_send_blood_request,null,false);
        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.show();

        EditText name,phone_number;
        TextView blood_group;
        Button submit;

        name=view.findViewById(R.id.editText7);
        phone_number=view.findViewById(R.id.editText8);
        blood_group=view.findViewById(R.id.textview4);
        submit=view.findViewById(R.id.submit_request);
        SharedPreferences sharedPreferences= requireContext().getSharedPreferences("MyData",MODE_PRIVATE);
        String phonenumber=sharedPreferences.getString("mobilenumber","");
        String name_user=sharedPreferences.getString("fullname","");
        phone_number.setText(phonenumber);
        name.setText(name_user);
        submit.setOnClickListener(v->{
            String nString=name.getText().toString().trim();
            String phoneString=phone_number.getText().toString().trim();
            String bloodGorup=getBloodGroup(blood_group);

            if(nString.equals("")){
                Toast.makeText(getContext(),"Enter the name...",Toast.LENGTH_SHORT).show();
            }else if(phoneString.equals("")){
                Toast.makeText(getContext(), "Fill the phone number", Toast.LENGTH_SHORT).show();
            }else if(bloodGorup.equals("")){
                Toast.makeText(getContext(), "Select the blood group..", Toast.LENGTH_SHORT).show();
            }else{
                 //Todo
            }
        });

    }

    private String getBloodGroup(TextView blood) {
        final String[] blood_group = {""};
        PopupMenu popupMenu=new PopupMenu(getContext(),blood);
        popupMenu.getMenuInflater().inflate(R.menu.blood_groups, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                blood_group[0] = (String) menuItem.getTitle();
                return true;
            }
        });
        popupMenu.show();
        return blood_group[0];
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