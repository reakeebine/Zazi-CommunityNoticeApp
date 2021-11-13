package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Class for the management of communities
 */
@SuppressWarnings("JavaDoc")
public class ManageCommunities extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private final FirebaseConnector firebase = FirebaseConnector.getInstance();
    private TextInputLayout communityTI;
    private ValueEventListener listener;

    /**
     * Creates the manage requests layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_communities);

        context = this;
        communityTI = findViewById(R.id.community_TI);
        Button addBtn = findViewById(R.id.addCommunity_btn);

        recyclerView= findViewById(R.id.recyclerViewCommunities);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        displayCommunities();
        addBtn.setOnClickListener(view -> handleAddBtnClick());
    }

    /**
     * Displays communities in database
     */
    private void displayCommunities() {
        listener = new ValueEventListener() {
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

                CommunityManagerAdapter comAdapter = new CommunityManagerAdapter(communitiesList, context);
                recyclerView.setAdapter(comAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageCommunities.this, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        };

        firebase.readCommunities().addValueEventListener(listener);

    }

    /**
     * Handles the click of add community button
     */
    private void handleAddBtnClick () {
        String newCommunity = Objects.requireNonNull(communityTI.getEditText()).getText().toString().trim();

        if (listener!=null)
            firebase.readCommunities().removeEventListener(listener);

        firebase.addCommunityToFirebase(newCommunity)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManageCommunities.this, "Community added", Toast.LENGTH_SHORT).show();
                    displayCommunities();
                }).addOnFailureListener(e -> Toast.makeText(ManageCommunities.this, "Some error occurred: " + e.toString(), Toast.LENGTH_SHORT).show());
    }
}