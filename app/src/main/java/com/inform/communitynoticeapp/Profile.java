package com.inform.communitynoticeapp;

import static com.inform.communitynoticeapp.R.id.nav_bookmarks;
import static com.inform.communitynoticeapp.R.id.nav_messageBoard;
import static com.inform.communitynoticeapp.R.id.nav_noticeBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * This class handles the profile page
 */
@SuppressWarnings("JavaDoc")
public class Profile extends AppCompatActivity {

    private final FirebaseConnector firebase= FirebaseConnector.getInstance();
    private Context context;
    private ImageView profilePicture;
    private TextView communityTV, roleTV;

    /**
     * Creates the edit profile layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * Creates restarts the page
     */
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        init();
    }

    /**
     * Creates the page in restart
     */
    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    public void init() {
        setContentView(R.layout.activity_profile);
        roleTV = findViewById(R.id.roleTV);
        profilePicture = findViewById(R.id.displayPicture_IV);
        communityTV = findViewById(R.id.communityTV);
        TextView welcomeMessageTV = findViewById(R.id.welcomeMessage_TV);
        TextView displayName = findViewById(R.id.usernameTV);
        displayName.setText(firebase.getUser().getDisplayName());
        welcomeMessageTV.setText(getString(R.string.Greeting)+ firebase.getUser().getDisplayName()+"!");
        context=this;

        firebase.getUserDetailsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails details = snapshot.getValue(UserDetails.class);
                assert details != null;
                roleTV.setText(details.getRole());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Error has occurred" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        readCommunities();
        showProfilePic();

        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationProfile);

        //set posts selected
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId())
            {
                case nav_noticeBoard:
                    startActivity(new Intent(getApplicationContext(), NoticeBoard.class));
                    overridePendingTransition(0,0);
                    return true;

                case nav_messageBoard:
                    startActivity(new Intent(getApplicationContext(), MessageBoard.class));
                    overridePendingTransition(0,0);
                    return true;

                case nav_bookmarks:
                    startActivity(new Intent(getApplicationContext(), Bookmarks.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.nav_profile:
                    return true;
            }
            return false;
        });
    }


    /**
     * Creates hamburger menu
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.hamburger_menu, menu);
        MenuItem itemOne = menu.findItem(R.id.manageRequests);
        MenuItem itemTwo = menu.findItem(R.id.manageCommunities);
        if (!roleTV.getText().toString().equals("Moderator")) {
            itemOne.setVisible(false);
            itemTwo.setVisible(false);
        }
        return true;
    }

    /**
     * Click listener
     * @param item
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.logout){
            showLogoutDialog();
        }else if(id==R.id.editProfile){
            Intent editProfile = new Intent(Profile.this, EditProfile.class);
            startActivity(editProfile);
        }else if(id==R.id.manageRequests){
            Intent manage_Requests = new Intent(Profile.this, ManageRequests.class);
            startActivity(manage_Requests);
        }else if(id==R.id.manageCommunities){
            Intent manageCommunities = new Intent(Profile.this, ManageCommunities.class);
            startActivity(manageCommunities);
        }else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Shows logout dialog
     */
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            firebase.getUserAuth().signOut();
            ((Activity) context).finish();
            Intent login = new Intent(Profile.this, LogIn.class);
            startActivity(login);

        });
        builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * SHows profile picture
     */
    public void showProfilePic() {
        String photoUrl = firebase.getDisplayPicture().toString();

        StorageReference photoRef = firebase.getFBStorage().getReferenceFromUrl(photoUrl);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            profilePicture.setImageBitmap(bmp);
        }).addOnFailureListener(exception ->
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show());
    }

    /**
     * Reads users community
     */
    private void readCommunities() {
        firebase.getUserCommunities().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> communitiesList = new ArrayList<>();
                Community aCommunity;
                for(DataSnapshot content: snapshot.getChildren()){
                    aCommunity = content.getValue(Community.class);
                    assert aCommunity != null;
                    communitiesList.add(aCommunity.getName());
                }
                Collections.sort(communitiesList);
                communityTV.setText(communitiesList.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}