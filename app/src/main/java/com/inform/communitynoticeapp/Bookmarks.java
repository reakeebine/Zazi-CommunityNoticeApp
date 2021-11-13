package com.inform.communitynoticeapp;

import static com.inform.communitynoticeapp.R.id.nav_bookmarks;
import static com.inform.communitynoticeapp.R.id.nav_messageBoard;
import static com.inform.communitynoticeapp.R.id.nav_noticeBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * This class is used to display bookmarks
 */
@SuppressWarnings("JavaDoc")
public class Bookmarks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private final FirebaseConnector firebase = FirebaseConnector.getInstance();
    private Context context;

    /**
     * Creates the bookmark page layout
     * @param savedInstanceState
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        context=this;


        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationBookmark);

        //set posts selected
        bottomNavigationView.setSelectedItemId(nav_bookmarks);

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
                    return true;

                case R.id.nav_profile:
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        recyclerView= findViewById(R.id.recyclerViewBookmark);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        displayBookmarks();
    }

    /**
     * Reads bookmarks from database to be displayed
     */
    private void displayBookmarks() {
        firebase.readBookmarks().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Post> postArrayList = new ArrayList<>();
                Post post;
                for(DataSnapshot content: snapshot.getChildren()){
                    post = content.getValue(Post.class);
                    postArrayList.add(0,post);
                }

                BookmarkAdapter postAdapter = new BookmarkAdapter(postArrayList, context);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Bookmarks.this, "An error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}