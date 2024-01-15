package com.example.asilapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextFirstName, editTextLastName,
            editTextTaxIdCode,editTextBirthPlace, editTextNationality,
            editTextPlaceOfResidence, editTextAddress, editTextPhoneNumber;
    Button buttonReg;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressBar progressBar;
    TextView textView;
    DatePicker dateOfBirth;

    private static final String TAG = "Register";
    private static final String KEY_SMOKE = "Do you smoke?";
    private static final String KEY_DRUGS = "Do you do drugs?";
    private static final String KEY_DRINK = "Do you drink?";
    private static final String KEY_MEDICINES = "Are you on medicines?";
    private static final String KEY_OPERATED = "Have you ever been operated in the past?";
    private static final String KEY_FAMILY_DISEASES = "Has anyone in your family had any diseases?";
    private static final String KEY_EXERCISE = "Do you exercise?";
    private static final String KEY_GOOD_DIET = "Do you have a good diet?";
    private static final String KEY_SLEEP = "Do you sleep enough?";
    private static final String KEY_ALLERGIES = "Do you have any allergies?";
    private static final String KEY_WATER = "Do you drink enough water?";
    private static final String KEY_MEALS_EAT_DAY = "How many meals do you eat in a day?";
    private static final String KEY_VEGETABLES_FRUIT = "Do you eat enough vegetables and fruit?";
    private static final String KEY_DIET_ON = "Which diet are you on?";
    private static final String KEY_HEART = "Heartbeat rate";
    public static final String KEY_PRESSURE = "Pressure";
    public static final String KEY_DIABETES = "Diabetes";
    public static final String KEY_RESPIRATORY_RATE = "Respiratory rate";
    public static final String KEY_OXYGENATION = "Oxygenation";
    public static final String KEY_TEMPERATURE = "Body temperature";
    public static final String KEY_PRESSURE_DATE = "Pressure measurement data";
    public static final String KEY_DIABETES_DATE = "Diabetes measurement data";
    public static final String KEY_RESPIRATORY_RATE_DATE = "Respiratory rate measurement data";
    public static final String KEY_HEARTBEAT_DATE = "Heartbeat measurement data";
    public static final String KEY_OXYGENATION_DATE = "Oxygenation data";
    public static final String KEY_TEMPERATURE_DATE = "Body temperature data";
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioGroup radioGroup3;
    private RadioGroup radioGroup4;
    private RadioGroup radioGroup5;
    private RadioGroup radioGroup6;
    private RadioGroup radioGroup7;
    private RadioGroup radioGroup8;
    private RadioGroup radioGroup9;
    private RadioGroup radioGroup10;
    private RadioGroup radioGroup11;
    private RadioGroup radioGroup12;
    private RadioGroup radioGroup13;
    private RadioGroup radioGroup14;

    public static final String KEY_FIRST_NAME = "First name";
    public static final String KEY_LAST_NAME = "Last name";
    public static final String KEY_TAX_ID_CODE = "Tax ID Code";
    public static final String KEY_BIRTH_PLACE = "Birth place";
    public static final String KEY_DATE_OF_BIRTH = "Date of birth";
    public static final String KEY_NATIONALITY = "Nationality";
    public static final String KEY_PLACE_OF_RESIDENCE = "Place of residence";
    public static final String KEY_ADDRESS = "Address";
    public static final String KEY_PHONE_NUMBER = "Phone number";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Date currentDate = new Date();

        // Formattare la data corrente
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup3 = findViewById(R.id.radioGroup3);
        radioGroup4 = findViewById(R.id.radioGroup4);
        radioGroup5 = findViewById(R.id.radioGroup5);
        radioGroup6 = findViewById(R.id.radioGroup6);
        radioGroup7 = findViewById(R.id.radioGroup7);
        radioGroup8 = findViewById(R.id.radioGroup8);
        radioGroup9 = findViewById(R.id.radioGroup9);
        radioGroup10 = findViewById(R.id.radioGroup10);
        radioGroup11 = findViewById(R.id.radioGroup11);
        radioGroup12 = findViewById(R.id.radioGroup12);
        radioGroup13 = findViewById(R.id.radioGroup13);
        radioGroup14 = findViewById(R.id.radioGroup14);

        editTextFirstName = findViewById(R.id.f_name);
        editTextLastName = findViewById(R.id.l_name);
        editTextTaxIdCode = findViewById(R.id.cf);
        editTextBirthPlace = findViewById(R.id.b_place);
        dateOfBirth = findViewById(R.id.d_birth_picker);
        editTextNationality = findViewById(R.id.nationality);
        editTextPlaceOfResidence = findViewById(R.id.p_residence);
        editTextAddress = findViewById(R.id.address);
        editTextPhoneNumber = findViewById(R.id.phone_number);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;

                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    user = mAuth.getCurrentUser();
                                    String userId = user.getUid();
                                    UsersLifestyleData(userId);

                                    UserData(userId);

                                    HealthData(userId);

                                    List<Double> dbInitialValue = Arrays.asList(0.0, 0.0, 0.0);

                                    Map<String, Object> note = new HashMap<>();

                                    note.put(formattedDate, dbInitialValue);

                                    db.collection("Chart Pie Data")
                                            .document(userId + " FMO")
                                            .set(note)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });

                                    Toast.makeText(Register.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void UsersLifestyleData(String userId){

        String smoke = getSelectedRadioValue(radioGroup1);
        String drink = getSelectedRadioValue(radioGroup2);
        String drugs = getSelectedRadioValue(radioGroup3);
        String medicines = getSelectedRadioValue(radioGroup4);
        String operated = getSelectedRadioValue(radioGroup5);
        String family_diseases = getSelectedRadioValue(radioGroup6);
        String exercise = getSelectedRadioValue(radioGroup7);
        String good_diet = getSelectedRadioValue(radioGroup8);
        String sleep = getSelectedRadioValue(radioGroup9);
        String allergies = getSelectedRadioValue(radioGroup10);
        String water = getSelectedRadioValue(radioGroup11);
        String meals_eat_day = getSelectedRadioValue(radioGroup12);
        String vegetables_fruits = getSelectedRadioValue(radioGroup13);
        String diet_on = getSelectedRadioValue(radioGroup14);

        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_SMOKE, smoke);
        note.put(KEY_DRUGS, drugs);
        note.put(KEY_DRINK, drink);
        note.put(KEY_MEDICINES, medicines);
        note.put(KEY_OPERATED, operated);
        note.put(KEY_FAMILY_DISEASES, family_diseases);
        note.put(KEY_EXERCISE, exercise);
        note.put(KEY_GOOD_DIET, good_diet);
        note.put(KEY_SLEEP, sleep);
        note.put(KEY_ALLERGIES, allergies);
        note.put(KEY_WATER, water);
        note.put(KEY_MEALS_EAT_DAY, meals_eat_day);
        note.put(KEY_VEGETABLES_FRUIT, vegetables_fruits);
        note.put(KEY_DIET_ON, diet_on);

        db.collection("Lifestyle Users").document(userId + " Lifestyle").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Register.this, "Lifestyle data saves!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    private String getSelectedRadioValue(RadioGroup radioGroup) {
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

        if (selectedRadioButton != null) {
            return selectedRadioButton.getText().toString();
        } else {
            return "";
        }
    }

    public void UserData(String userId){
        int day = dateOfBirth.getDayOfMonth();
        int month = dateOfBirth.getMonth() + 1; // Mese comincia da zero, aggiungiamo 1 per la rappresentazione corretta
        int year = dateOfBirth.getYear();

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String taxIdCode = editTextTaxIdCode.getText().toString();
        String birthPlace = editTextBirthPlace.getText().toString();
        String dateOfBirth = day + "/" + month + "/" + year;
        String nationality = editTextNationality.getText().toString();
        String placeOfResidence =  editTextPlaceOfResidence.getText().toString();
        String address = editTextAddress.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();

        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_FIRST_NAME, firstName);
        note.put(KEY_LAST_NAME, lastName);
        note.put(KEY_TAX_ID_CODE, taxIdCode);
        note.put(KEY_BIRTH_PLACE, birthPlace);
        note.put(KEY_DATE_OF_BIRTH, dateOfBirth);
        note.put(KEY_NATIONALITY, nationality);
        note.put(KEY_PLACE_OF_RESIDENCE, placeOfResidence);
        note.put(KEY_ADDRESS, address);
        note.put(KEY_PHONE_NUMBER, phoneNumber);

        db.collection("Users Data").document(userId + " Personal Data").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Register.this, "User data saves!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void HealthData(String userId){
        Map<String, Object> note =  new HashMap<>();

        note.put(KEY_HEART, "No Data");
        note.put(KEY_PRESSURE, "No Data");
        note.put(KEY_DIABETES, "No Data");
        note.put(KEY_RESPIRATORY_RATE, "No Data");
        note.put(KEY_OXYGENATION, "No Data");
        note.put(KEY_TEMPERATURE, "No Data");
        note.put(KEY_HEARTBEAT_DATE, "No Data");
        note.put(KEY_RESPIRATORY_RATE_DATE, "No Data");
        note.put(KEY_DIABETES_DATE, "No Data");
        note.put(KEY_PRESSURE_DATE, "No Data");
        note.put(KEY_OXYGENATION_DATE, "No Data");
        note.put(KEY_TEMPERATURE_DATE, "No Data");

        db.collection("User Measurement Data").document(userId + " Health Data")
                .set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });
    }
}