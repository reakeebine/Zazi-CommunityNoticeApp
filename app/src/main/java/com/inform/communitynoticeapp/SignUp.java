package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Class for the signup layout
 */
@SuppressWarnings("JavaDoc")
public class SignUp extends AppCompatActivity {

    private TextInputLayout emailTI, passwordTI, passwordAgainTI, displayNameTI, communityTI;
    private AutoCompleteTextView textView;
    private ValidateInput validate;
    private String dispName;
    private UserDetails userCurrent;
    private final FirebaseConnector firebase = FirebaseConnector.getInstance();

    /**
     * Creates the sign up layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //[START] Signup Part
        emailTI = findViewById(R.id.emailTI);
        displayNameTI = findViewById(R.id.displayNameTI);
        passwordTI = findViewById(R.id.passwordTI);
        passwordAgainTI = findViewById(R.id.passwordAgainTI);
        communityTI = findViewById(R.id.communityTI);
        Button signUpBtn = findViewById(R.id.signUp_btn2);
        textView = findViewById(R.id.autoCompleteCommunity);
        validate=new ValidateInput(this, emailTI, passwordTI, passwordAgainTI, displayNameTI, communityTI);
        signUpBtn.setOnClickListener(view -> handleSignUpBtnClick());
        //[END] Signup Part

        readCommunities();

    }

    /**
     * Handles the signup of user
     */
    private void handleSignUpBtnClick() {
        dispName = Objects.requireNonNull(displayNameTI.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(emailTI.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(passwordTI.getEditText()).getText().toString().trim();
        String passwordAgain = Objects.requireNonNull(passwordAgainTI.getEditText()).getText().toString().trim();
        String community = Objects.requireNonNull(communityTI.getEditText()).getText().toString().trim();
        String role = "Community Member";//default role
        userCurrent = new UserDetails(dispName, email, role);

        if(validate.checkEmailValid(email).equals("valid") && validate.checkPasswordValid(password, passwordAgain).equals("valid") ){
            if(validate.checkDisplayName(dispName).equals("valid") && validate.checkCommunity(community).equals("valid")) {
                //signup user
               firebase.signUpUser(email, password).addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       //Used to get user info e.g. email, password, etc.
                       firebase.sendVerificationEmail().addOnCompleteListener(task1 -> {
                           if(task1.isSuccessful()){
                               firebase.updateDispName(dispName);
                               firebase.saveNameInFirebase(userCurrent, community);
                               setDefaultPic();
                               Toast.makeText(SignUp.this, "You have signed up successfully! Don't forget to verify your email.", Toast.LENGTH_SHORT).show();
                               Intent login = new Intent(SignUp.this, LogIn.class);
                               startActivity(login);
                           }else {
                               Toast.makeText(SignUp.this, "Error has occurred" + task1.getException(), Toast.LENGTH_SHORT).show();
                           }
                       });
                   } else {
                       Toast.makeText(SignUp.this, "Error has occurred" + task.getException(), Toast.LENGTH_SHORT).show();
                   }

               });
            }
        }

    }

    /**
     * Sets default profile pic
     */
    public void setDefaultPic() {
        firebase.getStorageRef().child("profilePics/defaultPic.jpg").getDownloadUrl().addOnSuccessListener(defaultPicUri -> {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(defaultPicUri)
                    .build();
            firebase.getUser().updateProfile(profileChangeRequest);
        });
    }

    /**
     * Reads communities
     */
    private void readCommunities() {
        firebase.readCommunities().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> communitiesList = new ArrayList<>();
                String[] communitiesArray;
                Community aCommunity;
                for(DataSnapshot content: snapshot.getChildren()){
                    aCommunity = content.getValue(Community.class);
                    assert aCommunity != null;
                    communitiesList.add(aCommunity.getName());
                }

                communitiesArray = new String[communitiesList.size()];
                communitiesArray = communitiesList.toArray(communitiesArray);
                ArrayAdapter<String> communities = new ArrayAdapter<>(SignUp.this, android.R.layout.simple_dropdown_item_1line, communitiesArray);
                textView.setAdapter(communities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUp.this, "Error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}