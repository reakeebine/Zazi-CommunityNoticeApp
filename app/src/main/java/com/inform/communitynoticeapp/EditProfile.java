package com.inform.communitynoticeapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * This class handles the the editing of the user profile
 */
@SuppressWarnings("JavaDoc")
public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout dispNameTI;
    private ImageView profilePicIV;
    private final FirebaseConnector firebase = FirebaseConnector.getInstance();
    private ValidateInput validate;
    private static final int STORAGE_PERMISSION_CODE = 113;

    /**
     * Creates the edit profile layout
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);
        validate = new ValidateInput(this,null, null, null, dispNameTI, null);

        profilePicIV = findViewById(R.id.profile_pic_IV);
        dispNameTI = findViewById(R.id.display_Name_TI);
        Button uploadPicBtn = findViewById(R.id.upload_pic_Btn);
        TextView updateEmail = findViewById(R.id.updateEmail_TV);
        TextView updatePassword = findViewById(R.id.updatePassword_TV);
        TextView makeRequest = findViewById(R.id.makeRequest_TV);
        TextView joinCommunity = findViewById(R.id.joinCommunity_TV);
        Button saveBtn = findViewById(R.id.save_btn);

        showProfilePic();

        Objects.requireNonNull(dispNameTI.getEditText()).setText(firebase.getDisplayName());

        updateEmail.setOnClickListener(this);
        updatePassword.setOnClickListener(this);
        makeRequest.setOnClickListener(this);
        joinCommunity.setOnClickListener(this);
        saveBtn.setOnClickListener(view -> handleSaveBtnClick());
        uploadPicBtn.setOnClickListener(view -> handlePicBtnClick());

    }

    /**
     * Click listener
     * @param view
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.updateEmail_TV:
                Intent updateEmail = new Intent(EditProfile.this, UpdateEmail.class);
                startActivity(updateEmail);
                break;

            case R.id.updatePassword_TV:
                Intent updatePassword = new Intent(EditProfile.this, UpdatePassword.class);
                startActivity(updatePassword);
                break;

            case R.id.makeRequest_TV:
                Intent makeRequest = new Intent(EditProfile.this, MakeRequest.class);
                startActivity(makeRequest);
                break;

            case R.id.joinCommunity_TV:
                Intent joinCommunity = new Intent(EditProfile.this, JoinCommunities.class);
                startActivity(joinCommunity);
                break;
        }

    }

    /**
     * Saves users changes
     */
    private void handleSaveBtnClick() {
        String dispName = Objects.requireNonNull(dispNameTI.getEditText()).getText().toString();
        if (validate.checkDisplayName(dispName).equals("valid")) {
            firebase.updateDispName(dispName);
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        }
        Intent profile = new Intent(EditProfile.this, Profile.class);
        startActivity(profile);
    }

    /**
     * Handle user uploading profile picture
     */
    private void handlePicBtnClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, STORAGE_PERMISSION_CODE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //startActivityForResult(intent, 1);
            uploadPicActivityResultLauncher.launch(intent);
        }
    }

    /**
     * Get user permissions for user phone storage
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    //source: https://www.youtube.com/watch?v=q1OLKyilp8M
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handlePicBtnClick();
                Toast.makeText(EditProfile.this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditProfile.this, "Storage permission denied. You cannot add a photo.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ActivityResultLauncher<Intent> uploadPicActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    Uri imageUri = data.getData();
                    //firebase.updateDisplayPicture(imageUri);
                    setProfilePic(imageUri);
                }
            });



    /**
     * Sets profile picture
     * @param photoUri
     */
    public void setProfilePic(Uri photoUri) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image...");
        progressDialog.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference pictureRef = firebase.getStorageRef().child("profilePics/" + randomKey);
        pictureRef.putFile(photoUri).addOnProgressListener(snapshot -> {
            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
            progressDialog.setMessage("Upload is " + (int) progressPercent + "% complete.");
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Upload failed. Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            pictureRef.getDownloadUrl().addOnSuccessListener(pictureUri -> {
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(pictureUri)
                        .build();
                firebase.getUser().updateProfile(profileChangeRequest);
                profilePicIV.setImageURI(photoUri);
                Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            progressDialog.dismiss();
        });
    }

    /**
     * Displays profile picture
     */
    public void showProfilePic() {
        String photoUrl = firebase.getDisplayPicture().toString();

        StorageReference photoRef = firebase.getFBStorage().getReferenceFromUrl(photoUrl);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            profilePicIV.setImageBitmap(bmp);
        }).addOnFailureListener(exception ->
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show());
    }

}