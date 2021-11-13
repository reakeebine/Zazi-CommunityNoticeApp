package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Class for making requests
 */
@SuppressWarnings("JavaDoc")
public class MakeRequest extends AppCompatActivity {

    private TextInputLayout requestTI;
    private final FirebaseConnector firebase = FirebaseConnector.getInstance();


    /**
     * Creates the make request layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);
        requestTI = findViewById(R.id.requestReason_TI);
        Button submitRequest = findViewById(R.id.submitRequest_btn);
        submitRequest.setOnClickListener(view -> handleSubmitRequestClick());
    }

    /**
     * Handles the submission of requests
     */
    private void handleSubmitRequestClick() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM 'at' HH:mm");
        String dateNow = dateFormat.format(date);
        String reason = Objects.requireNonNull(requestTI.getEditText()).getText().toString();

        firebase.getUserDetailsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails details = snapshot.getValue(UserDetails.class);
                assert details != null;
                String requestStatus = details.getRequestStatus();

                if (requestStatus.equals("None")) {
                    firebase.addRequest(reason, dateNow).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(MakeRequest.this, "Request submitted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MakeRequest.this, "Some error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (requestStatus.equals("Declined")) {
                    Toast.makeText(MakeRequest.this, "Unfortunately, your request has been declined.", Toast.LENGTH_SHORT).show();
                } else if (requestStatus.equals("Pending")) {
                    Toast.makeText(MakeRequest.this, "Your request is still pending. Moderators will process your request soon!", Toast.LENGTH_SHORT).show();
                } else if (requestStatus.equals("Accepted")) {
                    Toast.makeText(MakeRequest.this, "You already have service provider permissions!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MakeRequest.this, "Some error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Intent profile = new Intent(MakeRequest.this, Profile.class);
        startActivity(profile);
    }
}