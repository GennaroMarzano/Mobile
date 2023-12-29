package com.example.asilapp10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChartPie extends AppCompatActivity {
    AnyChartView anyChartView;
    DatePicker datePickerStart;
    DatePicker datePickerEnd;
    String startDate = ""; // Variabile per la data di inizio
    String endDate = "";
    Button buttonCalendar, edit, apply;
    String[] categories = {"Food", "Medicines", "Other"};
    FirebaseUser user;
    FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent != null) {
            int totalFood = intent.getIntExtra("totalFood", 0);
            int totalMedicines = intent.getIntExtra("totalMedicines", 0);
            int totalOther = intent.getIntExtra("totalOther", 0);

            // Ora hai accesso ai valori delle somme nella nuova activity
        }

        Date currentDate = new Date();

        // Formattare la data corrente
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        setContentView(R.layout.activity_chart_pie);
        buttonCalendar = findViewById(R.id.three_dots);

        anyChartView = findViewById(R.id.any_chart_view);
        edit = findViewById(R.id.b_edit_chart_pie);

        apply = findViewById(R.id.btn_apply_test);

        datePickerStart = findViewById(R.id.d_pie_start);
        datePickerEnd = findViewById(R.id.d_pie_end);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        String userId = user.getUid();
        DocumentReference docRef = db.collection("Chart Pie Data")
                .document(userId + " Charges")
                .collection("Dates")
                .document(formattedDate);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Double foodValue = documentSnapshot.getDouble("Food");
                Double medicinesValue = documentSnapshot.getDouble("Medicines");
                Double otherValue = documentSnapshot.getDouble("Other");

                double[] valuesArray = new double[]{
                        foodValue.doubleValue(),
                        medicinesValue.doubleValue(),
                        otherValue.doubleValue()
                };
                boolean flag = false;
                setupPieChart(valuesArray, flag);
            }
        });

        // Serve per spostarsi nelle classi, ovvero nelle Activity
        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarPieChart.class);
                startActivity(intent);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditChartPie.class);
                startActivity(intent);
                finish();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Imposta un listener per rilevare il cambio di data
                datePickerStart.init(datePickerStart.getYear(), datePickerStart.getMonth(), datePickerStart.getDayOfMonth(),
                        new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Esegui azioni con la data selezionata
                                // year, monthOfYear e dayOfMonth rappresentano la data selezionata
                                String selectedDatePie = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                startDate = selectedDatePie;
                            }
                        });

                datePickerEnd.init(datePickerEnd.getYear(), datePickerEnd.getMonth(), datePickerEnd.getDayOfMonth(),
                        new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Esegui azioni con la data selezionata
                                // year, monthOfYear e dayOfMonth rappresentano la data selezionata
                                String selectedDatePie = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                endDate = selectedDatePie;
                            }
                        });
                //Query
                CollectionReference colRef = db.collection("Chart Pie Data")
                                               .document(userId + "Charges")
                                               .collection("Dates");
                colRef.whereGreaterThanOrEqualTo(FieldPath.documentId(), startDate)
                        .whereLessThanOrEqualTo(FieldPath.documentId(), endDate)
                        .get().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){

                                for(QueryDocumentSnapshot document : task.getResult()){
                                    String documentId = document.getId();
                                }
                            }
                        });
                boolean flag = true;
                //setupPieChart(flag);
            }
        });
    }

    public void setupPieChart(double[] valuesArray, boolean flag){

        if(flag == true){
            anyChartView.clear();
            //New values to new arrays
        }

        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for ( int i = 0; i < categories.length; i++) {
            dataEntries.add(new ValueDataEntry(categories[i], valuesArray[i]));
        }

        pie.data(dataEntries);
        pie.palette(new String[]{"#ED2B2B", "#53C943", "#2D70D3"}); // Ad esempio: rosso, verde, blu
        anyChartView.setChart(pie);
    }
}