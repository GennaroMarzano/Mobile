package com.example.asilapp10;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUserData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUserData extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tfName, tlName, tCF, tbPlace, tdBirth, tNationality, tpResidence, tAddress, tpNumber,
             tEmail, tPassword;
    FirebaseUser user;
    FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FragmentUserData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUserData.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUserData newInstance(String param1, String param2) {
        FragmentUserData fragment = new FragmentUserData();
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
        return inflater.inflate(R.layout.fragment_user_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tfName = getView().findViewById(R.id.tf_name);
        tlName = getView().findViewById(R.id.tl_name);
        tCF = getView().findViewById(R.id.tCF);
        tbPlace = getView().findViewById(R.id.tb_place);
        tdBirth = getView().findViewById(R.id.td_birth);
        tNationality = getView().findViewById(R.id.t_nationality);
        tpResidence = getView().findViewById(R.id.tp_residence);
        tAddress = getView().findViewById(R.id.t_address);
        tpNumber = getView().findViewById(R.id.tp_number);
        tEmail = getView().findViewById(R.id.t_email);
        tPassword = getView().findViewById(R.id.t_password);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        String userId = user.getUid();
        queryFireStore(userId);
    }

    private void queryFireStore(String userId) {

        DocumentReference docRef = db.collection("Users Data")
                .document(userId + " Personal Data");

        // Effettua la tua interrogazione
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

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
                        tpResidence.setText(documentSnapshot.getString("Place pf residence"));
                        tAddress.setText(documentSnapshot.getString("Address"));
                        tpNumber.setText(documentSnapshot.getString("Phone Number"));
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

            }
        });
    }
}