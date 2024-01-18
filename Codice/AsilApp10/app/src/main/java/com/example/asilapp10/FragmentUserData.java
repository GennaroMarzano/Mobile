package com.example.asilapp10;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FragmentUserData extends Fragment {

    TextView tfName, tlName, tCF, tbPlace, tdBirth, tNationality, tpResidence, tAddress, tpNumber,
             tEmail, tPassword;
    Button buttonEdit, buttonApplyEdit, buttonLogout, buttonBack, buttonShare;
    FirebaseUser user;
    FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String KEY_FIRST_NAME = "First name";
    public static final String KEY_LAST_NAME = "Last name";
    public static final String KEY_TAX_ID_CODE = "Tax ID Code";
    public static final String KEY_BIRTH_PLACE = "Birth place";
    public static final String KEY_DATE_OF_BIRTH = "Date of birth";
    public static final String KEY_NATIONALITY = "Nationality";
    public static final String KEY_PLACE_OF_RESIDENCE = "Place of residence";
    public static final String KEY_ADDRESS = "Address";
    public static final String KEY_PHONE_NUMBER = "Phone number";


    //MANCA VISUALIZZARE LA DATA, LA PASSWORD ( O FORSE NO  PER MOTIVI DI SICUREZZA )
    // E ABBASSARE IL LAYOUT DOVE SI TROVANO I BOTTONI PERCHE' LI TAGLIA
    public FragmentUserData() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tfName = requireView().findViewById(R.id.tf_name);
        tlName = requireView().findViewById(R.id.tl_name);
        tCF = requireView().findViewById(R.id.tCF);
        tbPlace = requireView().findViewById(R.id.tb_place);
        tdBirth = requireView().findViewById(R.id.td_birth);
        tNationality = requireView().findViewById(R.id.t_nationality);
        tpResidence = requireView().findViewById(R.id.tp_residence);
        tAddress = requireView().findViewById(R.id.t_address);
        tpNumber = requireView().findViewById(R.id.tp_number);
        tEmail = requireView().findViewById(R.id.t_email);
        tPassword = requireView().findViewById(R.id.t_password);



        buttonEdit = requireView().findViewById(R.id.btn_edit_data);
        buttonApplyEdit = requireView().findViewById(R.id.btn_apply_edit_data);
        buttonLogout = requireView().findViewById(R.id.btn_logout);
        buttonBack = requireView().findViewById(R.id.btn_back_edit);
        buttonShare = requireView().findViewById(R.id.btn_share);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        String userId = (user != null) ? user.getUid() : null;

        queryFireStore(userId);

        buttonEdit.setOnClickListener(v -> {

                tfName.setEnabled(true);
                tlName.setEnabled(true);
                tCF.setEnabled(true);
                tbPlace.setEnabled(true);
                tdBirth.setEnabled(true);
                tNationality.setEnabled(true);
                tpResidence.setEnabled(true);
                tAddress.setEnabled(true);
                tpNumber.setEnabled(true);

                buttonEdit.setVisibility(View.GONE);
                buttonLogout.setVisibility(View.GONE);
                buttonShare.setVisibility(View.GONE);

                buttonBack.setVisibility(View.VISIBLE);
                buttonApplyEdit.setVisibility(View.VISIBLE);
        });

        buttonLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            if (getActivity() != null) { // Controllo per evitare NullPointerException
                getActivity().finish();
            }
        });

        buttonApplyEdit.setOnClickListener(v -> {

                tfName.setEnabled(false);
                tlName.setEnabled(false);
                tCF.setEnabled(false);
                tbPlace.setEnabled(false);
                tdBirth.setEnabled(false);
                tNationality.setEnabled(false);
                tpResidence.setEnabled(false);
                tAddress.setEnabled(false);
                tpNumber.setEnabled(false);

                buttonEdit.setVisibility(View.VISIBLE);
                buttonLogout.setVisibility(View.VISIBLE);
                buttonShare.setVisibility(View.VISIBLE);

                buttonBack.setVisibility(View.GONE);
                buttonApplyEdit.setVisibility(View.GONE);

                saveNewDataUser(userId);
        });

        buttonBack.setOnClickListener(v -> {

                tfName.setEnabled(false);
                tlName.setEnabled(false);
                tCF.setEnabled(false);
                tbPlace.setEnabled(false);
                tdBirth.setEnabled(false);
                tNationality.setEnabled(false);
                tpResidence.setEnabled(false);
                tAddress.setEnabled(false);
                tpNumber.setEnabled(false);

                buttonEdit.setVisibility(View.VISIBLE);
                buttonLogout.setVisibility(View.VISIBLE);
                buttonShare.setVisibility(View.VISIBLE);
                buttonBack.setVisibility(View.GONE);
                buttonApplyEdit.setVisibility(View.GONE);

        });

        buttonShare.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Confirm sharing");
            builder.setMessage("Do you want to share your personal data?");
            builder.setPositiveButton("YES", (dialog, id) -> shareDataUser());
            builder.setNegativeButton("NO", (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }

    private void queryFireStore(String userId) {

        DocumentReference docRef = db.collection("Users Data")
                .document(userId + " Personal Data");

        // Effettua la tua interrogazione
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    Log.d("FirestoreDocument", "Document Snapshot: " + documentSnapshot.getData());

                    // Imposta i dati sulle TextView
                    tfName.setText(documentSnapshot.getString("First name"));
                    tlName.setText(documentSnapshot.getString("Last name"));
                    tCF.setText(documentSnapshot.getString("Tax ID Code"));
                    tbPlace.setText(documentSnapshot.getString("Birth place"));
                    tdBirth.setText(documentSnapshot.getString("Date of birth"));
                    tNationality.setText(documentSnapshot.getString("Nationality"));
                    tpResidence.setText(documentSnapshot.getString("Place of residence"));
                    tAddress.setText(documentSnapshot.getString("Address"));
                    tpNumber.setText(documentSnapshot.getString("Phone number"));
                    tEmail.setText(user.getEmail());
                    // tPassword non dovrebbe essere recuperata o mostrata per questioni di sicurezza
                } else {
                    // Il documento non esiste
                    Log.d("FirestoreError", "Document does not exist");
                }
            } else {
                // Gestione dell'errore
                Exception e = task.getException();
                if (e != null) {
                    e.printStackTrace();
                    Log.d("FirestoreError", "Error fetching document: " + e.getMessage());
                }
            }
        });

    }

    public void saveNewDataUser(String userId){
        String firstName = tfName.getText().toString();
        String lastName = tlName.getText().toString();
        String taxIdCode = tCF.getText().toString();
        String birthPlace = tbPlace.getText().toString();
        //String dateOfBirth = day + "/" + month + "/" + year; // Assicurati di gestire questa parte in base alla tua logica
        String nationality = tNationality.getText().toString();
        String placeOfResidence = tpResidence.getText().toString();
        String address = tAddress.getText().toString();
        String phoneNumber = tpNumber.getText().toString();

        Map<String, Object> note = new HashMap<>();

        if (!firstName.isEmpty()) {
            note.put(KEY_FIRST_NAME, firstName);
        }
        if (!lastName.isEmpty()) {
            note.put(KEY_LAST_NAME, lastName);
        }
        if (!taxIdCode.isEmpty()) {
            note.put(KEY_TAX_ID_CODE, taxIdCode);
        }
        if (!birthPlace.isEmpty()) {
            note.put(KEY_BIRTH_PLACE, birthPlace);
        }
        // Gestisci dateOfBirth qui se necessario
        if (!nationality.isEmpty()) {
            note.put(KEY_NATIONALITY, nationality);
        }
        if (!placeOfResidence.isEmpty()) {
            note.put(KEY_PLACE_OF_RESIDENCE, placeOfResidence);
        }
        if (!address.isEmpty()) {
            note.put(KEY_ADDRESS, address);
        }
        if (!phoneNumber.isEmpty()) {
            note.put(KEY_PHONE_NUMBER, phoneNumber);
        }

        // Aggiorna solo i campi non vuoti
        if (!note.isEmpty()) {
            db.collection("Users Data")
                    .document(userId + " Personal Data").set(note, SetOptions.merge())
                    .addOnCompleteListener(task -> {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Changes saved!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    public void shareDataUser(){

        String data = "Name: " + tfName.getText().toString() + "\n" +
                "Last name: " + tlName.getText().toString() + "\n" +
                "Tax ID Code: " + tCF.getText().toString() + "\n" +
                "Birth place: " + tbPlace.getText().toString() + "\n" +
                "Date of Birth: " + tdBirth.getText().toString() + "\n" +
                "Nationality: " + tNationality.getText().toString() + "\n" +
                "Place of residence: " + tpResidence.getText().toString() + "\n" +
                "Address: " + tAddress.getText().toString() + "\n" +
                "Phone number: " + tpNumber.getText().toString() + "\n" +
                "Email: " + tEmail.getText().toString() + "\n";


        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, data);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");

        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
        }
    }
}