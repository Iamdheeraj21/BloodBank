package com.unknowncoder.bloodbank.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unknowncoder.bloodbank.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import Notifications.Notification;
import Notifications.NotificationAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    RecyclerView notification_recyclerView;
    FloatingActionButton send_request;
    NotificationAdapter adapter;
    DatabaseReference notificationRef;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    DatabaseReference infoRef;
    String current_user;
    TextView toastName;
    MediaPlayer mediaPlayer;
    CircleImageView circleImageView;
    LayoutInflater layoutInflater;
    Toast toast;
    AlertDialog alertDialog;
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
        View view=LayoutInflater.from(getContext()).inflate(R.layout.activity_send_blood_request,null,false);
        alertDialog.setView(view);
        alertDialog.show();

        EditText name,phone_number;
        TextView blood_group;
        TextView submit;
        final String[] bloodgroup = {""};

        name=view.findViewById(R.id.editText7);
        phone_number=view.findViewById(R.id.editText8);
        blood_group=view.findViewById(R.id.textview4);
        submit=view.findViewById(R.id.submit_request);
        String phonenumber=sharedPreferences.getString("mobilenumber","");
        String name_user=sharedPreferences.getString("fullname","");
        phone_number.setText(phonenumber);
        name.setText(name_user);

        blood_group.setOnClickListener(v->{
            PopupMenu popupMenu=new PopupMenu(getContext(),blood_group);
            popupMenu.getMenuInflater().inflate(R.menu.blood_groups, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    bloodgroup[0] = (String) menuItem.getTitle();
                    blood_group.setText(menuItem.getTitle());
                    return true;
                }
            });
            popupMenu.show();
        });
        submit.setOnClickListener(v->{
            String nString=name.getText().toString().trim();
            String phoneString=phone_number.getText().toString().trim();

            if(nString.equals("")){
                Toast.makeText(getContext(),"Enter the name...",Toast.LENGTH_SHORT).show();
            }else if(phoneString.equals("")){
                Toast.makeText(getContext(), "Fill the phone number", Toast.LENGTH_SHORT).show();
            }else if(bloodgroup[0].equals("")){
                Toast.makeText(getContext(), "Select the blood group..", Toast.LENGTH_SHORT).show();
            }else{
                submitBloodRequest(nString,phoneString,bloodgroup[0],current_user);
            }
        });

    }

    private void initViews(View view){
        alertDialog=new AlertDialog.Builder(getActivity()).create();
        progressDialog=new ProgressDialog(getActivity());
        layoutInflater=getLayoutInflater();
        View customView=layoutInflater.inflate(R.layout.customtoast,(ViewGroup) view.findViewById(R.id.custom_toast));
        circleImageView=customView.findViewById(R.id.tick);
        toastName=customView.findViewById(R.id.toast_textview);
        toast=new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customView);
        send_request=view.findViewById(R.id.blood_request);
        notification_recyclerView=view.findViewById(R.id.notification_recyclerView);
        notification_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRef=FirebaseDatabase.getInstance().getReference().child("BankNotification");
        sharedPreferences= requireContext().getSharedPreferences("MyData",MODE_PRIVATE);
        current_user=sharedPreferences.getString("id","");
        Log.i("ghdf",current_user);
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.submitringtone);
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

    private void submitBloodRequest(String name, String phoneNumber, String bloodGroupName, String id)
    {
        if(id.equalsIgnoreCase("") || id.isEmpty()){
            Toast.makeText(getActivity(), "Invalid Id...", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.show();
            Random random = new Random();
            int recaptcha_num = random.nextInt(2500) + 5000;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String dateData = formatter.format(date);
            String application_number = String.valueOf(recaptcha_num);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("applicationo", application_number);
            hashMap.put("fullname", name);
            hashMap.put("phonenumber", phoneNumber);
            hashMap.put("bloodgroupname", bloodGroupName);
            hashMap.put("date", dateData);
            hashMap.put("status", "Active");
            DatabaseReference databaseReference1;
            databaseReference1 = FirebaseDatabase.getInstance().getReference();
            databaseReference1.child("BloodBankRequest").child(id).child(application_number).setValue(hashMap).addOnCompleteListener(task ->
            {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    mediaPlayer.start();
                    alertDialog.dismiss();
                    toast.show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                alertDialog.dismiss();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}