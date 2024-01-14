package com.example.asilapp10;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHealthProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHealthProfile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String KEY_HEART = "Heartbeat rate";
    public static final String KEY_PRESSURE = "Pressure";
    public static final String KEY_DIABETES = "Diabetes";
    public static final String KEY_RESPIRATORY_RATE = "Respiratory rate";
    public static final String KEY_PRESSURE_DATE = "Pressure measurement data";
    public static final String KEY_DIABETES_DATE = "Diabetes measurement data";
    public static final String KEY_RESPIRATORY_RATE_DATE = "Respiratory rate measurement data";
    public static final String KEY_HEARTBEAT_DATE = "Heartbeat measurement data";
    FirebaseUser user;
    FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tHeartData, tDiabetesData, tRespiratoryData, tPressureData, tMeasureDataHeart,
             tMeasureDataDiabetes, tMeasureDataRespiratory, tMeasureDataPressure, tDateHeart,
            tDatePressure, tDateDiabetes, tDateRespiratory, tDataMeasureHeartGone, tDataMeasurePressureGone,
            tDataMeasureDiabetesGone, tDataMeasureRespiratoryGone, heartbeat, diabetes, respiratory,
            pressure, tInfoSensor;
    ImageView imageViewSensorLocation;

    Button buttonShare, buttonBack, buttonMeasure, buttonMeasureHeart, buttonMeasurePressure,
           buttonMeasureDiabetes, buttonMeasureRespiratory, buttonBackMeasureHeart;
    SensorEventListener proximitySensorListener;

    public FragmentHealthProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHealthProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHealthProfile newInstance(String param1, String param2) {
        FragmentHealthProfile fragment = new FragmentHealthProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_healt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        String userId = user.getUid();

        queryHealthData(userId);

        imageViewSensorLocation = getView().findViewById(R.id.imageViewSensorLocation);
        tInfoSensor = getView().findViewById(R.id.info_sensor);

        buttonShare = getView().findViewById(R.id.btn_share_measurement);
        buttonBack =  getView().findViewById(R.id.btn_back_measurement);
        buttonBackMeasureHeart = getView().findViewById(R.id.btn_back_measurement_heart);
        buttonMeasure = getView().findViewById(R.id.btn_measure);
        buttonMeasureHeart = getView().findViewById(R.id.btn_measure_heart);
        buttonMeasurePressure = getView().findViewById(R.id.btn_measure_pressure);
        buttonMeasureDiabetes = getView().findViewById(R.id.btn_measure_diabetes);
        buttonMeasureRespiratory = getView().findViewById(R.id.btn_measure_respiratory_rate);

        tDataMeasureHeartGone = getView().findViewById(R.id.data_measure_heart);
        tHeartData = getView().findViewById(R.id.t_heart);
        tMeasureDataHeart = getView().findViewById(R.id.heart_date);
        tDateHeart = getView().findViewById(R.id.t_heart_date);
        heartbeat = getView().findViewById(R.id.heartbeat);

        tDataMeasurePressureGone = getView().findViewById(R.id.data_measure_pressure);
        tPressureData = getView().findViewById(R.id.t_pressure);
        tMeasureDataPressure = getView().findViewById(R.id.pressure_date);
        tDatePressure = getView().findViewById(R.id.t_pressure_date);
        pressure = getView().findViewById(R.id.pressure);

        tDataMeasureDiabetesGone = getView().findViewById(R.id.data_measure_diabetes);
        tDiabetesData = getView().findViewById(R.id.t_diabetes);
        tMeasureDataDiabetes = getView().findViewById(R.id.diabetes_date);
        tDateDiabetes = getView().findViewById(R.id.t_diabetes_date);
        diabetes = getView().findViewById(R.id.diabetes);

        tDataMeasureRespiratoryGone = getView().findViewById(R.id.data_measure_respiratory_rate);
        tRespiratoryData = getView().findViewById(R.id.t_respiratory_rate);
        tMeasureDataRespiratory = getView().findViewById(R.id.respratory_rate_date);
        tDateRespiratory = getView().findViewById(R.id.t_respiratory_rate_date);
        respiratory = getView().findViewById(R.id.respiratory);

        buttonMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Impostare la visibilità a GONE
                tDataMeasureHeartGone.setVisibility(View.VISIBLE);
                tHeartData.setVisibility(View.GONE);
                tMeasureDataHeart.setVisibility(View.GONE);
                tDateHeart.setVisibility(View.GONE);

                tDataMeasurePressureGone.setVisibility(View.VISIBLE);
                tPressureData.setVisibility(View.GONE);
                tMeasureDataPressure.setVisibility(View.GONE);
                tDatePressure.setVisibility(View.GONE);

                tDataMeasureDiabetesGone.setVisibility(View.VISIBLE);
                tDiabetesData.setVisibility(View.GONE);
                tMeasureDataDiabetes.setVisibility(View.GONE);
                tDateDiabetes.setVisibility(View.GONE);

                tDataMeasureRespiratoryGone.setVisibility(View.VISIBLE);
                tRespiratoryData.setVisibility(View.GONE);
                tMeasureDataRespiratory.setVisibility(View.GONE);
                tDateRespiratory.setVisibility(View.GONE);

                // Impostare la visibilità a VISIBLE
                buttonShare.setVisibility(View.GONE);
                buttonBack.setVisibility(View.VISIBLE);
                buttonMeasure.setVisibility(View.GONE);
                buttonMeasureHeart.setVisibility(View.VISIBLE);
                buttonMeasurePressure.setVisibility(View.VISIBLE);
                buttonMeasureDiabetes.setVisibility(View.VISIBLE);
                buttonMeasureRespiratory.setVisibility(View.VISIBLE);

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Impostare la visibilità a GONE
                tDataMeasureHeartGone.setVisibility(View.GONE);
                tHeartData.setVisibility(View.VISIBLE);
                tMeasureDataHeart.setVisibility(View.VISIBLE);
                tDateHeart.setVisibility(View.VISIBLE);
                heartbeat.setVisibility(View.VISIBLE);

                tDataMeasurePressureGone.setVisibility(View.GONE);
                tPressureData.setVisibility(View.VISIBLE);
                tMeasureDataPressure.setVisibility(View.VISIBLE);
                tDatePressure.setVisibility(View.VISIBLE);
                pressure.setVisibility(View.VISIBLE);

                tDataMeasureDiabetesGone.setVisibility(View.GONE);
                tDiabetesData.setVisibility(View.VISIBLE);
                tMeasureDataDiabetes.setVisibility(View.VISIBLE);
                tDateDiabetes.setVisibility(View.VISIBLE);
                diabetes.setVisibility(View.VISIBLE);

                tDataMeasureRespiratoryGone.setVisibility(View.GONE);
                tRespiratoryData.setVisibility(View.VISIBLE);
                tMeasureDataRespiratory.setVisibility(View.VISIBLE);
                tDateRespiratory.setVisibility(View.VISIBLE);
                respiratory.setVisibility(View.VISIBLE);

                imageViewSensorLocation.setVisibility(View.GONE);
                tInfoSensor.setVisibility(View.GONE);

                // Impostare la visibilità a VISIBLE
                buttonShare.setVisibility(View.VISIBLE);
                buttonBack.setVisibility(View.GONE);
                buttonMeasure.setVisibility(View.VISIBLE);
                buttonMeasureHeart.setVisibility(View.GONE);
                buttonMeasurePressure.setVisibility(View.GONE);
                buttonMeasureDiabetes.setVisibility(View.GONE);
                buttonMeasureRespiratory.setVisibility(View.GONE);

                queryHealthData(userId);
            }
        });

        buttonBackMeasureHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherMeasurement();
            }
        });

        buttonMeasureHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
                simulateAndDisplayHeartbeatRate(userId);
            }
        });

        buttonMeasurePressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateAndDisplayBloodPressure(userId);
            }
        });

        buttonMeasureDiabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simulateAndDisplayDiabetes(userId);
            }
        });

        buttonMeasureRespiratory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simulateAndDisplayRespiratoryRate(userId);
            }
        });
    }

    public void simulateAndDisplayHeartbeatRate(String userId){
        final int[] heartRate = {0};
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    sensorManager.unregisterListener(proximitySensorListener);
                    // Il dito è vicino al sensore, genera un valore per la frequenza cardiaca
                    heartRate[0] = new Random().nextInt((130 - 60) + 1) + 60; // Valore casuale tra 60 e 100
                    // Mostra o utilizza il valore di heartRate
                    String heartbeatRateText = heartRate[0] + " bpm";

                    LocalDate currentDate = LocalDate.now();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    String formattedDate = currentDate.format(formatter);

                    showOtherMeasurement();

                    // Mostra la stringa nella TextView
                    tDataMeasureHeartGone.setText(heartbeatRateText);
                    tDateHeart.setText(formattedDate);

                    Map<String, Object> note =  new HashMap<>();

                    note.put(KEY_HEART, heartbeatRateText);
                    note.put(KEY_HEARTBEAT_DATE, formattedDate);

                    db.collection("User Measurement Data").document(userId + " Health Data")
                            .update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Wow!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(proximitySensorListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void showImage(){
        imageViewSensorLocation.setVisibility(View.VISIBLE);
        tInfoSensor.setVisibility(View.VISIBLE);
        buttonBackMeasureHeart.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.GONE);

        tDataMeasureHeartGone.setVisibility(View.GONE);
        tHeartData.setVisibility(View.GONE);
        tMeasureDataHeart.setVisibility(View.GONE);
        tDateHeart.setVisibility(View.GONE);
        heartbeat.setVisibility(View.GONE);

        tDataMeasurePressureGone.setVisibility(View.GONE);
        tPressureData.setVisibility(View.GONE);
        tMeasureDataPressure.setVisibility(View.GONE);
        tDatePressure.setVisibility(View.GONE);
        pressure.setVisibility(View.GONE);

        tDataMeasureDiabetesGone.setVisibility(View.GONE);
        tDiabetesData.setVisibility(View.GONE);
        tMeasureDataDiabetes.setVisibility(View.GONE);
        tDateDiabetes.setVisibility(View.GONE);
        diabetes.setVisibility(View.GONE);

        tDataMeasureRespiratoryGone.setVisibility(View.GONE);
        tRespiratoryData.setVisibility(View.GONE);
        tMeasureDataRespiratory.setVisibility(View.GONE);
        tDateRespiratory.setVisibility(View.GONE);
        respiratory.setVisibility(View.GONE);

        // Impostare la visibilità a VISIBLE
        buttonMeasureHeart.setVisibility(View.GONE);
        buttonMeasurePressure.setVisibility(View.GONE);
        buttonMeasureDiabetes.setVisibility(View.GONE);
        buttonMeasureRespiratory.setVisibility(View.GONE);


    }

    public void showOtherMeasurement(){

        imageViewSensorLocation.setVisibility(View.GONE);
        tInfoSensor.setVisibility(View.GONE);

        tDataMeasureHeartGone.setVisibility(View.VISIBLE);
        tHeartData.setVisibility(View.GONE);
        tMeasureDataHeart.setVisibility(View.GONE);
        tDateHeart.setVisibility(View.GONE);
        heartbeat.setVisibility(View.VISIBLE);

        tDataMeasurePressureGone.setVisibility(View.VISIBLE);
        tPressureData.setVisibility(View.GONE);
        tMeasureDataPressure.setVisibility(View.GONE);
        tDatePressure.setVisibility(View.GONE);
        pressure.setVisibility(View.VISIBLE);

        tDataMeasureDiabetesGone.setVisibility(View.VISIBLE);
        tDiabetesData.setVisibility(View.GONE);
        tMeasureDataDiabetes.setVisibility(View.GONE);
        tDateDiabetes.setVisibility(View.GONE);
        diabetes.setVisibility(View.VISIBLE);

        tDataMeasureRespiratoryGone.setVisibility(View.VISIBLE);
        tRespiratoryData.setVisibility(View.GONE);
        tMeasureDataRespiratory.setVisibility(View.GONE);
        tDateRespiratory.setVisibility(View.GONE);
        respiratory.setVisibility(View.VISIBLE);

        // Impostare la visibilità a VISIBLE
        buttonShare.setVisibility(View.GONE);
        buttonBack.setVisibility(View.VISIBLE);
        buttonBackMeasureHeart.setVisibility(View.GONE);
        buttonMeasure.setVisibility(View.GONE);
        buttonMeasureHeart.setVisibility(View.VISIBLE);
        buttonMeasurePressure.setVisibility(View.VISIBLE);
        buttonMeasureDiabetes.setVisibility(View.VISIBLE);
        buttonMeasureRespiratory.setVisibility(View.VISIBLE);
    }

    public void simulateAndDisplayBloodPressure(String userId){
        Random rand = new Random();

        // Genera valori casuali per la pressione sistolica e diastolica
        int sistolic = rand.nextInt((200 - 80) + 1) + 80; // Intervallo: 80-200 mmHg
        int diastolic = rand.nextInt((120 - 40) + 1) + 40; // Intervallo: 40-120 mmHg

        // Costruisci la stringa da mostrare
        String bloodPressureText = "Max: " + sistolic + " mmHg\n" +
                "Min: " + diastolic + " mmHg";

        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String formattedDate = currentDate.format(formatter);

        // Mostra la stringa nella TextView
        tDataMeasurePressureGone.setText(bloodPressureText);
        tDatePressure.setText(formattedDate);

        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_PRESSURE, bloodPressureText);
        note.put(KEY_PRESSURE_DATE, formattedDate);

        db.collection("User Measurement Data").document(userId + " Health Data")
                .update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "User data saves!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void simulateAndDisplayDiabetes(String userId){
        Random rand = new Random();

        // Genera valori casuali per il glucosio nel sangue
        int glucoseLevel = rand.nextInt((250 - 70) + 1) + 70; // Intervallo: 70-250 mg/dL

        String glucoseLevelText = glucoseLevel + " mmol/L";

        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String formattedDate = currentDate.format(formatter);

        // Mostra la stringa nella TextView
        tDataMeasureDiabetesGone.setText(glucoseLevelText);
        tDateDiabetes.setText(formattedDate);

        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_DIABETES, glucoseLevelText);
        note.put(KEY_DIABETES_DATE, formattedDate);

        db.collection("User Measurement Data").document(userId + " Health Data")
                .update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "User data saves!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void simulateAndDisplayRespiratoryRate(String userId){
        Random rand = new Random();

        // Genera un valore casuale per la frequenza respiratoria
        int respiratoryRate = rand.nextInt((30 - 10) + 1) + 10; // Intervallo: 10-30 respiri al minuto

        String respiratoryRateText = respiratoryRate + " bpm";

        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String formattedDate = currentDate.format(formatter);

        // Mostra la stringa nella TextView
        tDataMeasureRespiratoryGone.setText(String.valueOf(respiratoryRateText));
        tDateRespiratory.setText(formattedDate);

        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_RESPIRATORY_RATE, respiratoryRateText);
        note.put(KEY_RESPIRATORY_RATE_DATE, formattedDate);

        db.collection("User Measurement Data").document(userId + " Health Data")
                .update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "User data saves!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void queryHealthData(String userId){
        DocumentReference docRef = db.collection("User Measurement Data")
                .document(userId + " Health Data");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Qui ottieni i dati dal documento
                        String heart = document.getString(KEY_HEART);
                        String pressure = document.getString(KEY_PRESSURE);
                        String diabetes = document.getString(KEY_DIABETES);
                        String respiratoryRate = document.getString(KEY_RESPIRATORY_RATE);
                        String heartDate = document.getString(KEY_HEARTBEAT_DATE);
                        String pressureDate = document.getString(KEY_PRESSURE_DATE);
                        String diabetesDate = document.getString(KEY_DIABETES_DATE);
                        String respiratoryRateDate = document.getString(KEY_RESPIRATORY_RATE_DATE);


                        tHeartData.setText(heart);
                        tDateHeart.setText(heartDate);
                        tPressureData.setText(pressure);
                        tDatePressure.setText(pressureDate);
                        tDiabetesData.setText(diabetes);
                        tDateDiabetes.setText(diabetesDate);
                        tRespiratoryData.setText(respiratoryRate);
                        tDateRespiratory.setText(respiratoryRateDate);
                    } else {
                        Log.d("Firestore", "Nessun documento trovato");
                    }
                } else {
                    Log.d("Firestore", "Errore nel recupero del documento", task.getException());
                }
            }
        });
    }
}