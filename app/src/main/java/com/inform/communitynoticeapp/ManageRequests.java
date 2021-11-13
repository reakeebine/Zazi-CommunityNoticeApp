package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * This class handles the mangement of requests
 */
@SuppressWarnings("JavaDoc")
public class ManageRequests extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private final FirebaseConnector firebase = FirebaseConnector.getInstance();

    /**
     * Creates the manage requests layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        context = this;

        recyclerView= findViewById(R.id.recyclerViewRequests);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        displayPendingRequests();
    }


    /**
     * Displays pending requests
     */
    private void displayPendingRequests() {
        firebase.readRequests().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Request aRequest;
                ArrayList<Request> requestsList = new ArrayList<>();
                for(DataSnapshot content: snapshot.getChildren()){
                    aRequest = content.getValue(Request.class);
                    Objects.requireNonNull(aRequest).setReason(aRequest.getReason());
                    if (aRequest.getStatus().equals("Pending")) {
                        requestsList.add(0, aRequest);
                    }
                }

                RequestsAdapter reqAdapter = new RequestsAdapter(requestsList, context);
                recyclerView.setAdapter(reqAdapter);
                requestsList.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageRequests.this, "Some error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}