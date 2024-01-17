package com.example.asilapp10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChartPie extends AppCompatActivity {
    AnyChartView anyChartView;
    DatePicker datePickerStart;
    DatePicker datePickerEnd;
    String startDate = "";
    String endDate = "";
    Button edit, apply;
    String[] categories = {"Food", "Medicines", "Other"};
    private Pie pie;
    FirebaseUser user;
    FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        setContentView(R.layout.activity_chart_pie);

        anyChartView = findViewById(R.id.any_chart_view);
        edit = findViewById(R.id.b_edit_chart_pie);

        apply = findViewById(R.id.btn_apply_test);

        datePickerStart = findViewById(R.id.d_pie_start);
        datePickerEnd = findViewById(R.id.d_pie_end);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        String userId = (user != null) ? user.getUid() : "";

        DocumentReference docRef = db.collection("Chart Pie Data")
                .document(userId + " FMO");

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<Double> values = new ArrayList<>();

                Object rawData = documentSnapshot.get(formattedDate);
                if (rawData instanceof List<?>) {
                    List<?> rawList = (List<?>) rawData;

                    for (Object item : rawList) {
                        if (item instanceof Double) {
                            values.add((Double) item);
                        } else if (item instanceof Integer) {
                            values.add(((Integer) item).doubleValue());
                        }
                    }
                }
                if (values.size() >= 3) {
                    double[] valuesArray = new double[]{
                            values.get(0),
                            values.get(1),
                            values.get(2)
                    };
                    setupPieChart(valuesArray);
                }
            }
        });


        edit.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EditChartPie.class);
            startActivity(intent);
            finish();
        });


        apply.setOnClickListener(v -> {
            startDate = getCurrentFormattedDate(datePickerStart.getYear(), datePickerStart.getMonth(), datePickerStart.getDayOfMonth());
            endDate = getCurrentFormattedDate(datePickerEnd.getYear(), datePickerEnd.getMonth(), datePickerEnd.getDayOfMonth());

            datePickerStart.init(datePickerStart.getYear(), datePickerStart.getMonth(), datePickerStart.getDayOfMonth(),
                    (view, year, monthOfYear, dayOfMonth) ->
                            startDate = getCurrentFormattedDate(year, monthOfYear, dayOfMonth));

            datePickerEnd.init(datePickerEnd.getYear(), datePickerEnd.getMonth(), datePickerEnd.getDayOfMonth(),
                    (view, year, monthOfYear, dayOfMonth) ->
                            endDate = getCurrentFormattedDate(year, monthOfYear, dayOfMonth));

            performFirestoreQuery(userId);
        });

    }

    public void setupPieChart(double[] valuesArray) {
        if (pie == null) {

            pie = AnyChart.pie();
            anyChartView.setChart(pie);
        }

        List<DataEntry> dataEntries = new ArrayList<>();
        for (int i = 0; i < categories.length; i++) {
            dataEntries.add(new ValueDataEntry(categories[i], valuesArray[i]));
        }

        pie.autoRedraw(false);
        pie.data(dataEntries);
        pie.palette(new String[]{"#ED2B2B", "#53C943", "#2D70D3"});
        pie.autoRedraw(true);
    }

    private void performFirestoreQuery(String userId) {
        double[] sumFood = {0.0};
        double[] sumMedicines = {0.0};
        double[] sumOther = {0.0};

        Log.d("QueryDates", "Start date before try-catch: " + startDate + ", End date: " + endDate);


        if (startDate != null && endDate != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            List<String> datesInRange = new ArrayList<>();

            try {
                Date start = format.parse(startDate);
                Date end = format.parse(endDate);

                Log.d("QueryDates", "Start date: " + startDate + ", End date: " + endDate);

                Calendar calendarStart = Calendar.getInstance();
                if (start != null) {
                    calendarStart.setTime(start);
                }

                Calendar calendarEnd = Calendar.getInstance();
                if (end != null) {
                    calendarEnd.setTime(end);
                }


                while (!calendarStart.after(calendarEnd)) {
                    Date currentDate = calendarStart.getTime();
                    String formattedDate = format.format(currentDate);
                    datesInRange.add(formattedDate);
                    calendarStart.add(Calendar.DATE, 1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("DateParseError", "Error parsing dates", e);
            }

            Log.d("DatesInRange", "Dates in Range: " + datesInRange);


            DocumentReference docRef = db.collection("Chart Pie Data")
                    .document(userId + " FMO");

            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Log.d("Fire store Document", "Document Snapshot: " + documentSnapshot.getData());
                        for (String date : datesInRange) {
                            Object rawData = documentSnapshot.get(date);

                            if (rawData instanceof List<?>) {
                                List<?> rawList = (List<?>) rawData;
                                if (rawList.size() >= 3 && allElementsAreNumbers(rawList)) {
                                    sumFood[0] += ((Number) rawList.get(0)).doubleValue();
                                    sumMedicines[0] += ((Number) rawList.get(1)).doubleValue();
                                    sumOther[0] += ((Number) rawList.get(2)).doubleValue();
                                }
                            }
                        }
                        double[] valuesArray = new double[]{
                                sumFood[0],
                                sumMedicines[0],
                                sumOther[0]
                        };

                        Log.d("SummedValues", "Food: " + sumFood[0] + ", Medicines: "
                                + sumMedicines[0] + ", Other: " + sumOther[0]);

                        setupPieChart(valuesArray);
                    } else {
                        Log.d("Fire store Error", "Document does not exist");
                    }
                } else {
                    Exception e = task.getException();
                    if (e != null) {
                        e.printStackTrace();
                        Log.d("Fire store Error", "Error fetching document: " + e.getMessage());
                    }
                }
            });

        }
    }

    private String getCurrentFormattedDate(int year, int month, int day) {
        return String.format(Locale.ENGLISH, "%04d-%02d-%02d", year, month + 1, day);
    }

    private boolean allElementsAreNumbers(List<?> list) {
        for (Object element : list) {
            if (!(element instanceof Number)) {
                return false;
            }
        }
        return true;
    }
}