package com.example.asilapp10;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
    public static final String KEY_PRESSURE = "Pressure";
    public static final String KEY_DIABETES = "Diabetes";
    public static final String KEY_RESPIRATORY_RATE = "Respiratory rate";
    FirebaseUser user;
    FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tHeartData, tDiabetesData, tRespiratoryData, tPressureData, tMeasureDataHeart,
             tMeasureDataDiabetes, tMeasureDataRespiratory, tMeasureDataPressure, tDataHeart,
            tDataPressure, tDataDiabetes, tDataRespiratory, tDataMeasureHeartGone, tDataMeasurePressureGone,
            tDataMeasureDiabetesGone, tDataMeasureRespiratoryGone;

    Button buttonShare, buttonBack, buttonMeasure, buttonMeasureHeart, buttonMeasurePressure,
           buttonMeasureDiabetes, buttonMeasureRespiratory;

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

        buttonShare = getView().findViewById(R.id.btn_share_measurement);
        buttonBack =  getView().findViewById(R.id.btn_back_measurement);
        buttonMeasure = getView().findViewById(R.id.btn_measure);
        buttonMeasureHeart = getView().findViewById(R.id.btn_measure_heart);
        buttonMeasurePressure = getView().findViewById(R.id.btn_measure_pressure);
        buttonMeasureDiabetes = getView().findViewById(R.id.btn_measure_diabetes);
        buttonMeasureRespiratory = getView().findViewById(R.id.btn_measure_respiratory_rate);

        tDataMeasureHeartGone = getView().findViewById(R.id.data_measure_heart);
        tHeartData = getView().findViewById(R.id.t_heart);
        tMeasureDataHeart = getView().findViewById(R.id.heart_date);
        tDataHeart = getView().findViewById(R.id.t_heart_date);

        tDataMeasurePressureGone = getView().findViewById(R.id.data_measure_pressure);
        tPressureData = getView().findViewById(R.id.t_pressure);
        tMeasureDataPressure = getView().findViewById(R.id.pressure_date);
        tDataPressure = getView().findViewById(R.id.t_pressure_date);

        tDataMeasureDiabetesGone = getView().findViewById(R.id.data_measure_diabetes);
        tDiabetesData = getView().findViewById(R.id.t_diabetes);
        tMeasureDataDiabetes = getView().findViewById(R.id.diabetes_date);
        tDataDiabetes = getView().findViewById(R.id.t_diabetes_date);

        tDataMeasureRespiratoryGone = getView().findViewById(R.id.data_measure_respiratory_rate);
        tRespiratoryData = getView().findViewById(R.id.t_respiratory_rate);
        tMeasureDataRespiratory = getView().findViewById(R.id.respratory_rate_date);
        tDataRespiratory = getView().findViewById(R.id.t_respiratory_rate_date);

        buttonMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Impostare la visibilità a GONE
                tDataMeasureHeartGone.setVisibility(View.VISIBLE);
                tHeartData.setVisibility(View.GONE);
                tMeasureDataHeart.setVisibility(View.GONE);
                tDataHeart.setVisibility(View.GONE);

                tDataMeasurePressureGone.setVisibility(View.VISIBLE);
                tPressureData.setVisibility(View.GONE);
                tMeasureDataPressure.setVisibility(View.GONE);
                tDataPressure.setVisibility(View.GONE);

                tDataMeasureDiabetesGone.setVisibility(View.VISIBLE);
                tDiabetesData.setVisibility(View.GONE);
                tMeasureDataDiabetes.setVisibility(View.GONE);
                tDataDiabetes.setVisibility(View.GONE);

                tDataMeasureRespiratoryGone.setVisibility(View.VISIBLE);
                tRespiratoryData.setVisibility(View.GONE);
                tMeasureDataRespiratory.setVisibility(View.GONE);
                tDataRespiratory.setVisibility(View.GONE);

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
                tDataHeart.setVisibility(View.VISIBLE);

                tDataMeasurePressureGone.setVisibility(View.GONE);
                tPressureData.setVisibility(View.VISIBLE);
                tMeasureDataPressure.setVisibility(View.VISIBLE);
                tDataPressure.setVisibility(View.VISIBLE);

                tDataMeasureDiabetesGone.setVisibility(View.GONE);
                tDiabetesData.setVisibility(View.VISIBLE);
                tMeasureDataDiabetes.setVisibility(View.VISIBLE);
                tDataDiabetes.setVisibility(View.VISIBLE);

                tDataMeasureRespiratoryGone.setVisibility(View.GONE);
                tRespiratoryData.setVisibility(View.VISIBLE);
                tMeasureDataRespiratory.setVisibility(View.VISIBLE);
                tDataRespiratory.setVisibility(View.VISIBLE);

                // Impostare la visibilità a VISIBLE
                buttonShare.setVisibility(View.VISIBLE);
                buttonBack.setVisibility(View.GONE);
                buttonMeasure.setVisibility(View.VISIBLE);
                buttonMeasureHeart.setVisibility(View.GONE);
                buttonMeasurePressure.setVisibility(View.GONE);
                buttonMeasureDiabetes.setVisibility(View.GONE);
                buttonMeasureRespiratory.setVisibility(View.GONE);
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

    public void simulateAndDisplayBloodPressure(String userId){
        Random rand = new Random();

        // Genera valori casuali per la pressione sistolica e diastolica
        int sistolic = rand.nextInt((200 - 80) + 1) + 80; // Intervallo: 80-200 mmHg
        int diastolic = rand.nextInt((120 - 40) + 1) + 40; // Intervallo: 40-120 mmHg

        // Costruisci la stringa da mostrare
        String bloodPressureText = "Max: " + sistolic + " mmHg\n" +
                "Min: " + diastolic + " mmHg";

        // Mostra la stringa nella TextView
        tDataMeasurePressureGone.setText(bloodPressureText);

        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_PRESSURE, bloodPressureText);

        db.collection("User Measurement Data").document(userId + "Health Data")
                .set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        // Mostra la stringa nella TextView
        tDataMeasureDiabetesGone.setText(String.valueOf(glucoseLevel));

        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_DIABETES, glucoseLevel);

        db.collection("User Measurement Data").document(userId + "Health Data")
                .set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
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


        // Mostra la stringa nella TextView
        tDataMeasureRespiratoryGone.setText(String.valueOf(respiratoryRate));

        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_RESPIRATORY_RATE, respiratoryRate);

        db.collection("User Measurement Data").document(userId + "Health Data")
                .set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}