package com.example.asilapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class EditChartPie extends AppCompatActivity {

    EditText editTextFood, editTextMedicines, editTextOther;
    FirebaseUser user;
    Button buttonBack, buttonAdd;
    FirebaseAuth mAuth;
    private static final String TAG = "EditChartPie";
    public static final String KEY_FOOD = "Food";
    public static final String KEY_MEDICINES = "Medicines";
    private static final String KEY_OTHER = "Other";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chart_pie);

        Date currentDate = new Date();

        // Formattare la data corrente
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        buttonBack = findViewById(R.id.btn_add_back);
        buttonAdd = findViewById(R.id.btn_add_add);

        editTextFood = findViewById(R.id.edit_text_food);
        editTextMedicines = findViewById(R.id.edit_text_medicines);
        editTextOther = findViewById(R.id.edit_text_other);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChartPie.class);
                startActivities(new Intent[]{intent});
                finish();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double[] food =  { editTextFood.getText().toString().isEmpty() ? 0.0
                                   : Double.parseDouble(editTextFood.getText().toString()) };
                double[] medicines = { editTextMedicines.getText().toString().isEmpty() ? 0.0
                                       : Double.parseDouble(editTextMedicines.getText().toString()) };
                double[] other = { editTextOther.getText().toString().isEmpty() ? 0.0
                                   : Double.parseDouble(editTextOther.getText().toString()) };

                String userId = user.getUid();
                DocumentReference docRef = db.collection("Chart Pie Data")
                                             .document(userId + " FMO");

                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<Double> values = (List<Double>) documentSnapshot.get(formattedDate);

                            Double foodValue = 0.0;
                            Double medicinesValue = 0.0;
                            Double otherValue = 0.0;

                            if (values != null && values.size() >= 3) {
                                foodValue = values.get(0);
                                medicinesValue = values.get(1);
                                otherValue = values.get(2);

                                // Utilizza i valori ottenuti
                            }

                            // Verifica se i valori sono diversi da null prima di convertirli in double
                            if (foodValue != null && medicinesValue != null && otherValue != null) {

                                food[0] += foodValue.doubleValue();
                                medicines[0] += medicinesValue.doubleValue();
                                other[0] += otherValue.doubleValue();

                                List<Double> updatedValues = Arrays.asList(food[0], medicines[0], other[0]);

                                // Aggiorna l'array nel documento Firestore utilizzando il riferimento docRef
                                docRef.update(formattedDate, updatedValues)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(EditChartPie.this, "Data saved!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EditChartPie.this, "Error!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                Intent intent = new Intent(getApplicationContext(), ChartPie.class);
                                startActivity(intent);
                                finish();

                                // Usa questi valori come desiderato nel tuo codice
                                // Ad esempio, assegna i valori alle variabili dell'interfaccia utente o esegui altre operazioni
                            } else {
                                // Gestisci il caso in cui uno o più valori siano null
                            }
                        } else {
                            // Il documento non esiste
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gestione dell'errore nel recupero dei dati da Firestore
                    }
                });
            }
        });
    }
}