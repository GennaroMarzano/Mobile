package com.example.asilapp10;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
    public static final String KEY_OXYGENATION = "Oxygenation";
    public static final String KEY_TEMPERATURE = "Temperature";
    public static final String KEY_PRESSURE_DATE = "Pressure measurement data";
    public static final String KEY_DIABETES_DATE = "Diabetes measurement data";
    public static final String KEY_RESPIRATORY_RATE_DATE = "Respiratory rate measurement data";
    public static final String KEY_HEARTBEAT_DATE = "Heartbeat measurement data";
    public static final String KEY_OXYGENATION_DATE = "Oxygenation data";
    public static final String KEY_TEMPERATURE_DATE = "Temperature data";
    public static final String KEY_NUMBER_EDIT_TEXT = "Number Edit Text";
    public static final String KEY_ID_DOCUMENT = "LsYRSc0yPM8h7PoN9Pmy";
    public static final String KEY_IDS_DELETED = "IDs deleted";

    FirebaseUser user;
    FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tHeartData, tDiabetesData, tRespiratoryData, tPressureData, tMeasureDataHeart,
             tMeasureDataDiabetes, tMeasureDataRespiratory, tMeasureDataPressure, tDateHeart,
            tDatePressure, tDateDiabetes, tDateRespiratory, tDataMeasureHeartGone, tDataMeasurePressureGone,
            tDataMeasureDiabetesGone, tDataMeasureRespiratoryGone, heartbeat, diabetes, respiratory,
            pressure, tInfoSensor, tOxygenationData, tMeasureDataOxygenation, tDateOxygenation,
            tDataMeasureOxygenationGone, oxygenation, tTemperatureData, tMeasureDataTemperature,
            tDateTemperature, tDataMeasureTemperature, temperature, tDataMeasureTemperatureGone;
    ImageView imageViewSensorLocation;

    Button buttonShare, buttonBack, buttonMeasure, buttonMeasureHeart, buttonMeasurePressure,
           buttonMeasureDiabetes, buttonMeasureRespiratory, buttonBackMeasureHeart,
           buttonMeasureOxygenation, buttonMeasureTemperature, buttonAddEditText, buttonEditEditText,
           buttonBackEditText, buttonSaveEditText;

    LinearLayout container;
    SensorEventListener proximitySensorListener;
    int countEditText;

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

        DocumentReference docRef = db.collection("Pathologies")
                .document(userId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        countEditText = document.getLong(KEY_NUMBER_EDIT_TEXT).intValue();
                    } else {
                        Log.d("Firestore", "Nessun documento trovato");
                    }
                } else {
                    Log.d("Firestore", "Errore nel recupero del documento", task.getException());
                }
            }
        });

        container = getView().findViewById(R.id.container);

        queryHealthData(userId);

        imageViewSensorLocation = getView().findViewById(R.id.imageViewSensorLocation);
        tInfoSensor = getView().findViewById(R.id.info_sensor);

        buttonShare = getView().findViewById(R.id.btn_share_measurement);
        buttonBack =  getView().findViewById(R.id.btn_back_measurement);
        buttonAddEditText = getView().findViewById(R.id.btn_add_edit_text);
        buttonEditEditText = getView().findViewById(R.id.btn_edit_edit_text);
        buttonBackEditText = getView().findViewById(R.id.btn_back_edit_text);
        buttonSaveEditText = getView().findViewById(R.id.btn_save_edit_text);

        buttonBackMeasureHeart = getView().findViewById(R.id.btn_back_measurement_heart);
        buttonMeasure = getView().findViewById(R.id.btn_measure);
        buttonMeasureHeart = getView().findViewById(R.id.btn_measure_heart);
        buttonMeasurePressure = getView().findViewById(R.id.btn_measure_pressure);
        buttonMeasureDiabetes = getView().findViewById(R.id.btn_measure_diabetes);
        buttonMeasureRespiratory = getView().findViewById(R.id.btn_measure_respiratory_rate);
        buttonMeasureOxygenation = getView().findViewById(R.id.btn_measure_oxygenation);
        buttonMeasureTemperature = getView().findViewById(R.id.btn_measure_temperature);

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

        tDataMeasureOxygenationGone = getView().findViewById(R.id.data_measure_oxygenation);
        tOxygenationData = getView().findViewById(R.id.t_oxygenation);
        tMeasureDataOxygenation = getView().findViewById(R.id.oxygenation_date);
        tDateOxygenation = getView().findViewById(R.id.t_oxygenation_date);
        oxygenation = getView().findViewById(R.id.oxygenation);

        tDataMeasureTemperatureGone = getView().findViewById(R.id.data_measure_temperature);
        tTemperatureData = getView().findViewById(R.id.t_temperature);
        tMeasureDataTemperature = getView().findViewById(R.id.temperature_date);
        tDateTemperature = getView().findViewById(R.id.t_temperature_date);
        temperature = getView().findViewById(R.id.temperature);

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

                tDataMeasureOxygenationGone.setVisibility(View.VISIBLE);
                tOxygenationData.setVisibility(View.GONE);
                tMeasureDataOxygenation.setVisibility(View.GONE);
                tDateOxygenation.setVisibility(View.GONE);

                tDataMeasureTemperatureGone.setVisibility(View.VISIBLE);
                tTemperatureData.setVisibility(View.GONE);
                tMeasureDataTemperature.setVisibility(View.GONE);
                tDateTemperature.setVisibility(View.GONE);

                // Impostare la visibilità a VISIBLE
                buttonShare.setVisibility(View.GONE);
                buttonBack.setVisibility(View.VISIBLE);
                buttonMeasure.setVisibility(View.GONE);
                buttonMeasureHeart.setVisibility(View.VISIBLE);
                buttonMeasurePressure.setVisibility(View.VISIBLE);
                buttonMeasureDiabetes.setVisibility(View.VISIBLE);
                buttonMeasureRespiratory.setVisibility(View.VISIBLE);
                buttonMeasureOxygenation.setVisibility(View.VISIBLE);
                buttonMeasureTemperature.setVisibility(View.VISIBLE);

            }
        });

//PROBLEMI DA RISOLVERE, QUANDO CLICCO BACK SI DOPPIANO GLI EDITTEXT SOTTO, SE RICARICO IL LAYOUT
        //SCOMPAIONO, SE NON VIENE INSERITO NESSUN CODICE E L'UTENTE CLICCA OK L'APP CRASHA
        //INSERIRE UN CONTROLLO

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

                tDataMeasureOxygenationGone.setVisibility(View.GONE);
                tOxygenationData.setVisibility(View.VISIBLE);
                tMeasureDataOxygenation.setVisibility(View.VISIBLE);
                tDateOxygenation.setVisibility(View.VISIBLE);

                tDataMeasureTemperatureGone.setVisibility(View.GONE);
                tTemperatureData.setVisibility(View.VISIBLE);
                tMeasureDataTemperature.setVisibility(View.VISIBLE);
                tDateTemperature.setVisibility(View.VISIBLE);

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
                buttonMeasureOxygenation.setVisibility(View.GONE);
                buttonMeasureTemperature.setVisibility(View.GONE);

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

        buttonMeasureOxygenation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateAndDisplayOxygenation(userId);
            }
        });

        buttonMeasureTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateAndDisplayTemperature(userId);
            }
        });

        buttonEditEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea l'EditText per la password
                EditText passwordEditText = new EditText(getContext());
                passwordEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                passwordEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});

                // Crea l'AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Insert Medical Code");
                builder.setView(passwordEditText);

                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String doctorCode = passwordEditText.getText().toString();
                        queryMedicalCode(doctorCode, new QueryCallback() {
                            @Override
                            public void onQueryCompleted(boolean isMatch) {
                                if (isMatch) {
                                    // Codice corrispondente
                                    buttonEditEditText.setVisibility(View.GONE);
                                    buttonAddEditText.setVisibility(View.VISIBLE);
                                    buttonBackEditText.setVisibility(View.VISIBLE);
                                    buttonSaveEditText.setVisibility(View.VISIBLE);

                                    enableAllViews(container);
                                } else {
                                    // Nessun codice trovato
                                    Toast.makeText(getActivity(), "No code found!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Mostra l'AlertDialog
                builder.show();
            }
        });

        buttonAddEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditTextAndDeleteButton(userId);
            }
        });

        buttonBackEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllViews(container);

                buttonEditEditText.setVisibility(View.VISIBLE);
                buttonAddEditText.setVisibility(View.GONE);
                buttonBackEditText.setVisibility(View.GONE);
                buttonSaveEditText.setVisibility(View.GONE);
            }
        });

        buttonSaveEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(userId);
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

        tDataMeasureOxygenationGone.setVisibility(View.GONE);
        tOxygenationData.setVisibility(View.GONE);
        tMeasureDataOxygenation.setVisibility(View.GONE);
        tDateOxygenation.setVisibility(View.GONE);
        oxygenation.setVisibility(View.GONE);

        tDataMeasureTemperatureGone.setVisibility(View.GONE);
        tTemperatureData.setVisibility(View.GONE);
        tMeasureDataTemperature.setVisibility(View.GONE);
        tDateTemperature.setVisibility(View.GONE);
        temperature.setVisibility(View.GONE);

        // Impostare la visibilità a VISIBLE
        buttonMeasureHeart.setVisibility(View.GONE);
        buttonMeasurePressure.setVisibility(View.GONE);
        buttonMeasureDiabetes.setVisibility(View.GONE);
        buttonMeasureRespiratory.setVisibility(View.GONE);
        buttonMeasureOxygenation.setVisibility(View.GONE);
        buttonMeasureTemperature.setVisibility(View.GONE);
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

        tDataMeasureOxygenationGone.setVisibility(View.VISIBLE);
        tOxygenationData.setVisibility(View.GONE);
        tMeasureDataOxygenation.setVisibility(View.GONE);
        tDateOxygenation.setVisibility(View.GONE);
        oxygenation.setVisibility(View.VISIBLE);

        tDataMeasureTemperatureGone.setVisibility(View.VISIBLE);
        tTemperatureData.setVisibility(View.GONE);
        tMeasureDataTemperature.setVisibility(View.GONE);
        tDateTemperature.setVisibility(View.GONE);
        temperature.setVisibility(View.VISIBLE);

        // Impostare la visibilità a VISIBLE
        buttonShare.setVisibility(View.GONE);
        buttonBack.setVisibility(View.VISIBLE);
        buttonBackMeasureHeart.setVisibility(View.GONE);
        buttonMeasure.setVisibility(View.GONE);
        buttonMeasureHeart.setVisibility(View.VISIBLE);
        buttonMeasurePressure.setVisibility(View.VISIBLE);
        buttonMeasureDiabetes.setVisibility(View.VISIBLE);
        buttonMeasureRespiratory.setVisibility(View.VISIBLE);
        buttonMeasureOxygenation.setVisibility(View.VISIBLE);
        buttonMeasureTemperature.setVisibility(View.VISIBLE);
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

    public void simulateAndDisplayOxygenation(String userId){
        Random rand = new Random();

        // Genera un valore casuale per la frequenza respiratoria
        int oxygenation = rand.nextInt(11) + 90; // Intervallo: 90% - 100%

        String oxygenationText = oxygenation + " %";

        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String formattedDate = currentDate.format(formatter);

        // Mostra la stringa nella TextView
        tDataMeasureOxygenationGone.setText(oxygenationText);
        tDateOxygenation.setText(formattedDate);

        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_OXYGENATION, oxygenationText);
        note.put(KEY_OXYGENATION_DATE, formattedDate);

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

    public void simulateAndDisplayTemperature(String userId){
        Random rand = new Random();

        // Genera un valore casuale per la frequenza respiratoria
        float temperature = (4.5f * rand.nextFloat()) + 35.5f;   //Intervallo: 90% - 100%

        String temperatureText = temperature + " °C";

        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String formattedDate = currentDate.format(formatter);

        // Mostra la stringa nella TextView
        tDataMeasureTemperatureGone.setText(temperatureText);
        tDateTemperature.setText(formattedDate);

        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_TEMPERATURE, temperatureText);
        note.put(KEY_TEMPERATURE_DATE, formattedDate);

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
                        String oxygenation = document.getString(KEY_OXYGENATION);
                        String temperature = document.getString(KEY_TEMPERATURE);
                        String respiratoryRate = document.getString(KEY_RESPIRATORY_RATE);
                        String heartDate = document.getString(KEY_HEARTBEAT_DATE);
                        String pressureDate = document.getString(KEY_PRESSURE_DATE);
                        String diabetesDate = document.getString(KEY_DIABETES_DATE);
                        String respiratoryRateDate = document.getString(KEY_RESPIRATORY_RATE_DATE);
                        String oxygenationDate = document.getString(KEY_OXYGENATION_DATE);
                        String temperatureDate = document.getString(KEY_TEMPERATURE_DATE);

                        tHeartData.setText(heart);
                        tDateHeart.setText(heartDate);
                        tPressureData.setText(pressure);
                        tDatePressure.setText(pressureDate);
                        tDiabetesData.setText(diabetes);
                        tDateDiabetes.setText(diabetesDate);
                        tRespiratoryData.setText(respiratoryRate);
                        tDateRespiratory.setText(respiratoryRateDate);
                        tOxygenationData.setText(oxygenation);
                        tDateOxygenation.setText(oxygenationDate);
                        tTemperatureData.setText(temperature);
                        tDateTemperature.setText(temperatureDate);

                    } else {
                        Log.d("Firestore", "Nessun documento trovato");
                    }
                } else {
                    Log.d("Firestore", "Errore nel recupero del documento", task.getException());
                }
            }
        });

        docRef = db.collection("Pathologies").document(userId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.exists()){

                        // Recupera l'elenco degli ID eliminati
                        List<Long> firestoreDeletedIds = (List<Long>) documentSnapshot.get(KEY_IDS_DELETED);
                        if (firestoreDeletedIds != null) {
                            deletedEditTextIds.clear();
                            deletedEditTextIds.addAll(firestoreDeletedIds.stream()
                                    .map(Long::intValue)
                                    .collect(Collectors.toList()));
                        }

                        Integer numberOfEditTexts = documentSnapshot.getLong(KEY_NUMBER_EDIT_TEXT).intValue();

                        for (int i = 1; i <= numberOfEditTexts; i++) {

                            if (deletedEditTextIds.contains(i)) {
                                continue;
                            }
                            
                            EditText editText = new EditText(getContext());
                            editText.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            editText.setText(documentSnapshot.getString(String.valueOf(i)));
                            editText.setId(i);

                            // Crea il bottone di eliminazione
                            Button deleteButton = new Button(getContext());
                            deleteButton.setText("Delete");
                            deleteButton.setId(i + 1000); // Assicurati che l'ID del bottone sia univoco

                            // Aggiungi il listener al bottone di eliminazione
                            deleteButton.setOnClickListener(v -> {
                                container.removeView(editText);
                                container.removeView(deleteButton);
                                deletedEditTextIds.add(editText.getId());
                                updateFireStoreWithIdsDeleted(userId);
                            });

                            // Aggiungi un TextWatcher per tracciare i cambiamenti
                            editText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    // Implementazione non necessaria
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    editTextData.put(editText.getId(), s.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    // Implementazione non necessaria
                                }
                            });

                            // Imposta il colore del testo a nero
                            editText.setTextColor(Color.BLACK);

                            container.addView(editText);
                            container.addView(deleteButton);
                        }
                        disableAllViews(container);
                    }
                }
            }
        });
    }

    public void queryMedicalCode(String doctorCode, QueryCallback callback) {
        Log.d("queryMedicalCode", "Inizio della query. Codice medico: " + doctorCode);

        db.collection("Medical Code").document(KEY_ID_DOCUMENT).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        boolean isMatch = false;
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Long> storedPassword = (List<Long>) document.get("Doctor Code");
                                Log.d("queryMedicalCode", "Password trovata: " + storedPassword);

                                // Converte l'input dell'utente in un intero
                                int userInput = Integer.parseInt(doctorCode);

                                // Controlla se l'input corrisponde a uno degli elementi dell'array 'password'
                                for (Long password : storedPassword) {
                                    if (userInput == password.intValue()) {
                                        isMatch = true;
                                        break;
                                    }
                                }

                                if (!isMatch) {
                                    Log.d("queryMedicalCode", "Non corrispondenza trovata");
                                }
                            } else {
                                Log.d("queryMedicalCode", "Il documento non esiste");
                            }
                        } else {
                            Log.e("queryMedicalCode", "Errore durante la query", task.getException());
                        }
                        // Chiamata al callback
                        callback.onQueryCompleted(isMatch);
                    }
                });
    }

    public interface QueryCallback {
        void onQueryCompleted(boolean isMatch);
    }

    // Mappa per memorizzare il testo di ciascun EditText
    Map<Integer, String> editTextData = new HashMap<>();
    // Supponiamo di avere un ArrayList per tenere traccia degli ID eliminati
    ArrayList<Integer> deletedEditTextIds = new ArrayList<>();

    public void addEditTextAndDeleteButton(String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Text");

        final EditText inputField = new EditText(getContext());
        inputField.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(inputField);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = new EditText(getContext());
                editText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                editText.setTextColor(Color.BLACK); // Imposta il colore del testo a nero

                editText.setText(inputField.getText().toString());

                int newEditTextId;

                if (!deletedEditTextIds.isEmpty()) {
                    // Se ci sono ID liberi, usa il primo ID disponibile
                    newEditTextId = deletedEditTextIds.remove(0); // Rimuove e restituisce il primo elemento

                    // Aggiorna Firestore con la lista aggiornata di ID eliminati
                    updateFireStoreWithIdsDeleted(userId);
                } else {
                    // Se non ci sono ID liberi, incrementa il contatore e usa il nuovo ID
                    countEditText++;
                    newEditTextId = countEditText;
                }

                editText.setId(newEditTextId);
                editTextData.put(newEditTextId, inputField.getText().toString());

                // Aggiungi un TextWatcher per tracciare i cambiamenti
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Implementazione non necessaria
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        editTextData.put(editText.getId(), s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // Implementazione non necessaria
                    }
                });

                Button deleteButton = new Button(getContext());
                deleteButton.setText("Delete");
                deleteButton.setId(countEditText + 1000); // Assicurati che l'ID del bottone sia univoco

                // Aggiungi listener al bottone di eliminazione
                deleteButton.setOnClickListener(v -> {

                    int deletedId = editText.getId(); // Ottieni l'ID dell'EditText eliminato
                    container.removeView(editText);
                    container.removeView(deleteButton);
                    deletedEditTextIds.add(deletedId);

                    updateFireStoreWithIdsDeleted(userId);
                });

                container.addView(editText);
                container.addView(deleteButton);

                Map<String, Object> note = new HashMap<>();
                note.put(String.valueOf(newEditTextId), inputField.getText().toString());
                note.put(KEY_NUMBER_EDIT_TEXT, countEditText);

                db.collection("Pathologies").document(userId)
                        .update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "User data saved!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void updateFireStoreWithIdsDeleted(String userId){
        // Aggiorna Firestore con l'ID eliminato
        Map<String, Object> updates = new HashMap<>();
        updates.put(KEY_IDS_DELETED, deletedEditTextIds); // Aggiunge l'ID all'array 'deleted_ids'

        db.collection("Pathologies").document(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "ID eliminato aggiornato con successo");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Errore nell'aggiornamento dell'ID eliminato", e);
                });
    }
    public void saveData(String userId) {
        Map<String, Object> note = new HashMap<>();
        for (Map.Entry<Integer, String> entry : editTextData.entrySet()) {
            note.put(String.valueOf(entry.getKey()), entry.getValue());
        }

        db.collection("Pathologies").document(userId)
                .update(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Modifications saved!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error saving modifications!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void disableAllViews(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(false); // Disabilita la View
            if (child instanceof ViewGroup) {
                disableAllViews((ViewGroup) child); // Ricorsione per sottolayout
            }
        }
    }
    private void enableAllViews(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(true); // Abilita la View
            if (child instanceof ViewGroup) {
                enableAllViews((ViewGroup) child); // Ricorsione per sottolayout
            }
        }
    }
}