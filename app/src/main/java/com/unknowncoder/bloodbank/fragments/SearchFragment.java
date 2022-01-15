package com.unknowncoder.bloodbank.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.unknowncoder.bloodbank.Blood_Donor_profile;
import com.unknowncoder.bloodbank.R;
import com.unknowncoder.bloodbank.Utils.BloodDonor;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFragment extends Fragment {
    EditText editText;
    RecyclerView recyclerView;
    String string="";
    ImageView button;
    DatabaseReference databaseReference;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        button.setOnClickListener(v -> {
            string=editText.getText().toString();
            if(editText.getText().toString().equals(""))
                showSnackbar("Enter the name");
            else
                searchBloodDonor(string);
        });
        return view;
    }

    private void initViews(View view)
    {
        recyclerView=view.findViewById(R.id.recycler_view);
        button=view.findViewById(R.id.search_donor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        editText=view.findViewById(R.id.search_blood_donor);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("AllUser");
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

        FirebaseRecyclerAdapter<BloodDonor, BloodDonorViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<BloodDonor, BloodDonorViewHolder>(bloodDonorFirebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull BloodDonorViewHolder holder, int position, @NonNull BloodDonor model)
            {
                holder.textView.setText(model.getFullname());
                if(model.getImageurl().equals("default"))
                    holder.circleImageView.setImageResource(R.drawable.ic_baseline_person_24);
                else
                    Glide.with(getActivity()).load(model.getImageurl()).into(holder.circleImageView);

                holder.itemView.setOnClickListener(v -> {
                    String blood_donor_id=getRef(position).getKey();
                    Intent intent=new Intent(getContext(), Blood_Donor_profile.class);
                    intent.putExtra("blood_donor_id",blood_donor_id);
                    startActivity(intent);
                });
            }
            @NonNull
            @Override
            public BloodDonorViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_donor,parent,false);
                return new BloodDonorViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
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

    private void showSnackbar(String msg){
        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                msg, Snackbar.LENGTH_LONG);
        snackBar.show();
    }
}