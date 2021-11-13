package com.inform.communitynoticeapp;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Validator class
 */
@SuppressWarnings("JavaDoc")
public class ValidateInput {
    private TextInputLayout displayNameTI;
    private TextInputLayout passwordAgainTI;
    private TextInputLayout communityTI;
    private final Context context;
    private final TextInputLayout emailTI;
    private TextInputLayout passwordTI;
    private TextInputLayout emailAgainTI;
    private final ArrayList<String> communities = new ArrayList<>();
    private final FirebaseConnector firebase = FirebaseConnector.getInstance();

    /**
     * Constructor
     * @param context
     * @param emailAgainTI
     * @param emailTI
     */
    public ValidateInput(Context context, TextInputLayout emailTI, TextInputLayout emailAgainTI){
        this.context = context;
        this.emailTI =emailTI;
        this.emailAgainTI =emailAgainTI;
    }

    /**
     * Constructor
     * @param context
     * @param displayNameTI
     * @param emailTI
     * @param communityTI
     * @param passwordAgainTI
     * @param passwordTI
     */
    public ValidateInput(Context context, TextInputLayout emailTI, TextInputLayout passwordTI, TextInputLayout passwordAgainTI, TextInputLayout displayNameTI, TextInputLayout communityTI){
        this.context=context;
        this.displayNameTI=displayNameTI;
        this.emailTI = emailTI;
        this.passwordTI =passwordTI;
        this.passwordAgainTI=passwordAgainTI;
        this.communityTI=communityTI;
    }

    /**
     * Check display name
     * @param displayName
     */
    public CharSequence checkDisplayName(String displayName) {
        CharSequence val="valid";
        if(displayName.equals("")){
            displayNameTI.setEndIconActivated(true);
            displayNameTI.setError("Enter display name");
            val=displayNameTI.getError();

        }
        return val;
    }

    /**
     * Check email
     * @param email
     */
    public CharSequence checkEmailValid(String email){
        if(email.length()==0){
            emailTI.setEndIconActivated(true);
            emailTI.setError("Please enter your email");
            return emailTI.getError();
        }else if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            emailTI.setEndIconActivated(true);
            emailTI.setError("Email is invalid");
            return emailTI.getError();
        }
        else {
            return "valid";
        }
    }

    /**
     * Check email
     * @param email
     * @param emailAgain
     */
    public CharSequence checkEmailValid(String email, String emailAgain){
        if(email.length()==0){
            emailTI.setEndIconActivated(true);
            emailTI.setError("Please enter your email");
            return emailTI.getError();
        }else if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            emailTI.setEndIconActivated(true);
            emailTI.setError("Email is invalid");
            return emailTI.getError();
        }else if(!(email.equals(emailAgain))){
            emailTI.setEndIconActivated(true);
            emailAgainTI.setEndIconActivated(true);
            emailAgainTI.setError("Passwords don't match");
            return emailAgainTI.getError();
        }
        else {
            return "valid";
        }
    }

    /**
     * Check password
     * @param password
     * @param passwordAgain
     */
    public CharSequence checkPasswordValid(@NonNull String password, @NonNull String passwordAgain){
        if(password.length()==0){
            passwordTI.setEndIconActivated(true);
            passwordTI.setError("Please enter a password");
            return passwordTI.getError();
        }else if(password.length()<6){
            passwordTI.setEndIconActivated(true);
            passwordTI.setError("Password must be 6 characters");
            return passwordTI.getError();
        } else if(!(passwordAgain.equals(password))){
            passwordTI.setEndIconActivated(true);
            passwordAgainTI.setEndIconActivated(true);
            passwordAgainTI.setError("Passwords don't match");
            return passwordAgainTI.getError();
        } else {
            return "valid";
        }
    }

    /**
     * Check password
     * @param password
     */
    public CharSequence checkEnteredPasswordValid(@NonNull String password){
        if(password.length()==0){
            passwordTI.setEndIconActivated(true);
            passwordTI.setError("Please enter a password");
            return passwordTI.getError();
        }else if(password.length()<6){
            passwordTI.setEndIconActivated(true);
            passwordTI.setError("Password must be 6 characters");
            return passwordTI.getError();
        } else {
            return "valid";
        }
    }

    /**
     * Check community
     * @param community
     */
    public CharSequence checkCommunity(@NonNull String community){
        readCommunities();
        if(community.equals("")){
            communityTI.setEndIconActivated(true);
            communityTI.setError("Enter your community name");
            return communityTI.getError();
        } else if(!(communities.contains(community))){
            communityTI.setEndIconActivated(true);
            communityTI.setError("This community group does not exist");
            return communityTI.getError();
        }
        return "valid";
    }

    /**
     * Get context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Read communities in database
     */
    private void readCommunities() {
        firebase.readCommunities().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Community aCommunity;
                for(DataSnapshot content: snapshot.getChildren()){
                    aCommunity = content.getValue(Community.class);
                    assert aCommunity != null;
                    communities.add(aCommunity.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

