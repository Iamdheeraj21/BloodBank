package com.unknowncoder.bloodbank;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
{
    BottomNavigationView bottomNavigationView;
    DatabaseReference databaseReference;
    FrameLayout frameLayout;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        frameLayout=findViewById(R.id.fragment_container);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("BloodGroupUnit");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

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
        }else if(item_Id==R.id.logout){
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

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item ->
    {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.bottom_home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.bottom_blood_donors:
                selectedFragment = new SearchFragment();
                break;
            case R.id.bottom_profile:
                selectedFragment = new ProfileFragment();
                break;

        }
        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
        return true;
    };
}