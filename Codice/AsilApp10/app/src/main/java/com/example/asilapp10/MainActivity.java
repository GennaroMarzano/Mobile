package com.example.asilapp10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button buttonFragmentUser, buttonFragmentQR, buttonFragmentHealth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        buttonFragmentUser = findViewById(R.id.btn_profile);
        buttonFragmentQR = findViewById(R.id.btn_qr);
        buttonFragmentHealth = findViewById(R.id.btn_health);
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivities(new Intent[]{intent});
            finish();
        }

        buttonFragmentUser.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            FragmentUserData userFragment = new FragmentUserData();
            fragmentTransaction.replace(R.id.fragment_container, userFragment);
            fragmentTransaction.commit();
        });


        buttonFragmentQR.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            FragmentQRCode codeQRFragment = new FragmentQRCode();
            fragmentTransaction.replace(R.id.fragment_container, codeQRFragment);
            fragmentTransaction.commit();
        });


        buttonFragmentHealth.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            FragmentHealthProfile healthProfileFragment = new FragmentHealthProfile();
            fragmentTransaction.replace(R.id.fragment_container, healthProfileFragment);
            fragmentTransaction.commit();
        });

    }
}