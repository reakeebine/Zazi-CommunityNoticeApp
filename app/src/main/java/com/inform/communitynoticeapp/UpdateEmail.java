package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Class for updating email
 */
@SuppressWarnings("JavaDoc")
public class UpdateEmail extends AppCompatActivity {
    private TextInputLayout newEmail, newEmailAgain;
    private ValidateInput validate;

    /**
     * Creates the update email layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        newEmail = findViewById(R.id.updateEmail_TI);
        newEmailAgain = findViewById(R.id.updateEmailAgain_TI);
        validate = new ValidateInput(this,newEmail, newEmailAgain);
        Button saveChanges = findViewById(R.id.saveChanges_Btn2);
        saveChanges.setOnClickListener(view -> handleSaveChangesClick());
    }

    /**
     * Save user changes
     */
    private void handleSaveChangesClick() {
        FirebaseConnector firebase = FirebaseConnector.getInstance();
        String email = Objects.requireNonNull(newEmail.getEditText()).getText().toString();
        String emailAgain = Objects.requireNonNull(newEmailAgain.getEditText()).getText().toString();

        if(validate.checkEmailValid(email,emailAgain).equals("valid")){
            firebase.updateEmail(email).addOnCompleteListener(task -> {
                firebase.getUser().sendEmailVerification();
                Toast.makeText(UpdateEmail.this, "Email successfully updated!", Toast.LENGTH_SHORT).show();
                firebase.getUserAuth().signOut();
            });
        }

        Intent login = new Intent(UpdateEmail.this, LogIn.class);
        startActivity(login);
    }
}