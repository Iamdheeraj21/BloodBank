package com.example.bloodbank;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import com.unknowncoder.bloodbank.R;
public class ProfileFragment extends Fragment {
    SharedPreferences sharedPreferences;
    TextView textView_email,textView_name,textView_gender,textView_number,textView_bloodgroup,textView_dob;
    CircleImageView circleImageView;
    DatabaseReference databaseReference;
    String CurrentUser;
    private StorageTask uploadTask;
    StorageReference UserProfileRef;
    private static final int IMAGE_REQUEST=1;
    Uri ImageUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        getInformationOfUser();
        circleImageView.setOnClickListener(v -> {
            setTheProfileImage();
        });
        return view;
    }

    private void initViews(View view) 
    {
        sharedPreferences=getContext().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        textView_email=view.findViewById(R.id.textView_email);
        textView_name=view.findViewById(R.id.textView_name);
        textView_dob=view.findViewById(R.id.textView_dob);
        circleImageView=view.findViewById(R.id.imageview2);
        textView_bloodgroup=view.findViewById(R.id.textView_bloodGroup);
        textView_gender=view.findViewById(R.id.text_gender);
        textView_number=view.findViewById(R.id.textView_phone);
        CurrentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("AllUser").child(CurrentUser);
        UserProfileRef= FirebaseStorage.getInstance().getReference().child("Profile Images");
    }

    private void setTheProfileImage()
    {
        PopupMenu popupMenu=new PopupMenu(getContext(),circleImageView);
        popupMenu.getMenuInflater().inflate(R.menu.profile_change,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.changeImage) {
                final CharSequence[] options = {"Choose from Gallery","Remove Image","Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Photo!");
                builder.setItems(options, (dialog, which) -> {
                    // TODO Auto-generated method stub
                    if(options[which].equals("Choose from Gallery")){
                        openImage();
                    }
                    else if(options[which].equals("Cancel")){
                        dialog.dismiss();
                    }else if(options[which].equals("Remove Image")){
                        removeImage();
                    }
                });
                builder.show();
                return true;}
            return false;});
        popupMenu.show();
    }
    private void getInformationOfUser(){
        if(sharedPreferences.getString("imageurl","").equals("default"))
            circleImageView.setImageResource(R.drawable.ic_baseline_person_24);
        else
            Glide.with(getContext()).load(sharedPreferences.getString("imageurl","")).into(circleImageView);
        textView_email.setText("Email :-"+sharedPreferences.getString("email",""));
        textView_name.setText(sharedPreferences.getString("fullname",""));
        textView_bloodgroup.setText("BloodGroup :-"+sharedPreferences.getString("bloodgroup",""));
        textView_gender.setText("Gender :-"+sharedPreferences.getString("gender",""));
        textView_dob.setText("Date of Birth :-"+sharedPreferences.getString("dob",""));
        textView_number.setText("Phone Number :-"+sharedPreferences.getString("mobilenumber",""));
    }
//    private void getInformationOfUser()
//    {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                if(snapshot.exists())
//                {
//                    String email=snapshot.child("email").getValue().toString();
//                    String fullname=snapshot.child("fullname").getValue().toString();
//                    String imageUrl=snapshot.child("imageurl").getValue().toString();
//                    String dob=snapshot.child("dob").getValue().toString();
//                    String phonenumber=snapshot.child("mobilenumber").getValue().toString();
//                    String bloodGroup=snapshot.child("bloodgroup").getValue().toString();
//                    String gender=snapshot.child("gender").getValue().toString();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error)
//            {
//                Alerter.create(getParentFragment().getActivity())
//                        .setTitle("Alert")
//                        .setText(error.getMessage())
//                        .setIcon(R.drawable.alerticon)
//                        .setBackgroundColorRes(R.color.black)
//                        .setDuration(2000)
//                        .setOnClickListener(v -> {
//                        }).setOnShowListener(() -> {
//                }).setOnHideListener(() -> {
//                }).show();
//            }
//        });
//    }

    private void removeImage()
    {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("AllUser").child(CurrentUser).child("imageurl");
        databaseReference.setValue("default").addOnCompleteListener(task -> {
            if(task.isComplete()){
                circleImageView.setImageResource(R.drawable.ic_baseline_person_24);
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(),
                        e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    //Todo :- Change the user profile Image
    private void openImage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading...");
        if(ImageUri!=null){
            final StorageReference file=UserProfileRef.child(System.currentTimeMillis()+"."+getFileExtension(ImageUri));
            uploadTask=file.putFile(ImageUri);
            uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return file.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if(task.isSuccessful()){
                    Uri downloaduri=task.getResult();
                    assert downloaduri != null;
                    String mUri= downloaduri.toString();

                    databaseReference=FirebaseDatabase.getInstance().getReference("AllUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("imageurl",mUri);
                    databaseReference.updateChildren(map);
                    getImgeUrl();
                    getInformationOfUser();
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(e ->{
                            Log.e("error",e.getLocalizedMessage());
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();});
        }else {
            Toast.makeText(getContext(),"no image selected",Toast.LENGTH_SHORT).show();
        }
        progressDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData() !=null){
            ImageUri=data.getData();
            if(uploadTask!=null && uploadTask.isInProgress()){
                Toast.makeText(getContext(),"Upload in Progress",Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
    }

    private void getImgeUrl(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("imageurl",snapshot.child("imageurl").getValue().toString());
                }else {
                    Toast.makeText(getActivity(), "url not found...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error:-"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}