package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Adapter.BloodDonor;
import de.hdodenhof.circleimageview.CircleImageView;

public class FindBloodDonorsActivity extends AppCompatActivity
{
    EditText editText;
    RecyclerView recyclerView;
    String string="";
    Button button;
    DatabaseReference databaseReference;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_blood_donors);
        recyclerView=findViewById(R.id.recycler_view);
        button=findViewById(R.id.search_donor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        editText=findViewById(R.id.search_blood_donor);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("AllUser");

        button.setOnClickListener(v -> {
            string=editText.getText().toString();
            if(editText.getText().toString().equals(""))
                Toast.makeText(FindBloodDonorsActivity.this, "Please enter name!", Toast.LENGTH_SHORT).show();
            else
                searchBloodDonor(string);
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void searchBloodDonor(String s)
    {
        FirebaseRecyclerOptions<BloodDonor> bloodDonorFirebaseRecyclerOptions=null;
        if(s.equals("")){
            bloodDonorFirebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<BloodDonor>()
                    .setQuery(databaseReference,BloodDonor.class)
                    .build();
        }
        if(!s.equals("")){
            bloodDonorFirebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<BloodDonor>()
                    .setQuery(databaseReference.orderByChild("bloodgroup")
                                    .startAt(s)
                                    .endAt(s+"\uf8ff")
                            ,BloodDonor.class)
                    .build();
        }

        FirebaseRecyclerAdapter<BloodDonor,BloodDonorViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<BloodDonor, BloodDonorViewHolder>(bloodDonorFirebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull BloodDonorViewHolder holder, int position, @NonNull BloodDonor model)
            {
                holder.textView.setText(model.getFullname());
                if(model.getImageurl().equals("default"))
                    holder.circleImageView.setImageResource(R.drawable.ic_baseline_person_24);
                else
                    Glide.with(FindBloodDonorsActivity.this).load(model.getImageurl()).into(holder.circleImageView);

                holder.itemView.setOnClickListener(v -> {
                    String blood_donor_id=getRef(position).getKey();
                    Intent intent=new Intent(FindBloodDonorsActivity.this,Blood_Donor_profile.class);
                    intent.putExtra("blood_donor_id",blood_donor_id);
                    startActivity(intent);
                });
            }
            @NonNull
            @Override
            public BloodDonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_donor,parent,false);
                return new BloodDonorViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<BloodDonor> bloodDonorFirebaseRecyclerOptions=null;
        if(string.equals("")){
           bloodDonorFirebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<BloodDonor>()
                   .setQuery(databaseReference,BloodDonor.class)
                   .build();
        }
        if(!string.equals("")){
            bloodDonorFirebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<BloodDonor>()
                    .setQuery(databaseReference.orderByChild("bloodgroup")
                            .startAt(string)
                            .endAt(string+"\uf8ff")
                            ,BloodDonor.class)
                    .build();
        }

        FirebaseRecyclerAdapter<BloodDonor,BloodDonorViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<BloodDonor, BloodDonorViewHolder>(bloodDonorFirebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull BloodDonorViewHolder holder, int position, @NonNull BloodDonor model)
            {
                holder.textView.setText(model.getFullname());
                if(model.getImageurl().equals("default"))
                    holder.circleImageView.setImageResource(R.drawable.ic_baseline_person_24);
                else
                    Glide.with(FindBloodDonorsActivity.this).load(model.getImageurl()).into(holder.circleImageView);

                holder.itemView.setOnClickListener(v -> {
                    String blood_donor_id=getRef(position).getKey();
                    Intent intent=new Intent(FindBloodDonorsActivity.this,Blood_Donor_profile.class);
                    intent.putExtra("blood_donor_id",blood_donor_id);
                    startActivity(intent);
                });
            }
            @NonNull
            @Override
            public BloodDonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_donor,parent,false);
                return new BloodDonorViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }*/

    public static class BloodDonorViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView circleImageView;
        TextView textView;
        public BloodDonorViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.blood_donor_image);
            textView=itemView.findViewById(R.id.blood_donor_name);
        }
    }
}