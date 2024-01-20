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
    Button buttonFragmentUser, buttonFragmentQR, buttonFragmentHealth, buttonChartPie, buttonHome;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inizializza l'istanza di FirebaseAuth

        auth = FirebaseAuth.getInstance();

        // Trova i pulsanti nel layout

        buttonFragmentUser = findViewById(R.id.btn_profile);
        buttonFragmentQR = findViewById(R.id.btn_qr);
        buttonFragmentHealth = findViewById(R.id.btn_health);
        buttonChartPie = findViewById(R.id.btn_pie);
        buttonHome = findViewById(R.id.btn_home);

        // Ottieni l'utente corrente

        user = auth.getCurrentUser();

        // Verifica se l'utente è loggato, altrimenti reindirizza alla schermata di login

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivities(new Intent[]{intent});
            finish();
        }

        // Configura il click listener per il pulsante del profilo utente
        buttonFragmentUser.setOnClickListener(v -> {

            // Ottieni il gestore dei frammenti

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Crea e sostituisci il frammento dell'utente

            FragmentUserData userFragment = new FragmentUserData();
            fragmentTransaction.replace(R.id.fragment_container, userFragment);
            fragmentTransaction.commit();
        });

        // Configura il click listener per il pulsante del codice QR
        buttonFragmentQR.setOnClickListener(v -> {

            // Ottieni il gestore dei frammenti

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Crea e sostituisci il frammento dell'utente

            FragmentQRCode codeQRFragment = new FragmentQRCode();
            fragmentTransaction.replace(R.id.fragment_container, codeQRFragment);
            fragmentTransaction.commit();
        });

         // Configura il click listener per il pulsante del profilo salute
        buttonFragmentHealth.setOnClickListener(v -> {

            // Ottieni il gestore dei frammenti

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Crea e sostituisci il frammento dell'utente

            FragmentHealthProfile healthProfileFragment = new FragmentHealthProfile();
            fragmentTransaction.replace(R.id.fragment_container, healthProfileFragment);
            fragmentTransaction.commit();
        });

        //Configura il click listener per il pulsante del grafico a torta
        buttonChartPie.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChartPie.class);
            startActivity(intent);
            finish();
        });

        //Configura il click listener per il pulsante home
        buttonHome.setOnClickListener(v ->{

            // Ottieni il gestore dei frammenti

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Crea e sostituisci il frammento dell'utente

            FragmentRules rules = new FragmentRules();
            fragmentTransaction.replace(R.id.fragment_container, rules);
            fragmentTransaction.commit();
        });
    }
}