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
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Notice board class
 */
@SuppressWarnings("JavaDoc")
public class NoticeBoard extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private Context context;
    private final FirebaseConnector firebase = FirebaseConnector.getInstance();
    private NoticeBoardAdapter postAdapter;
    private Chip events, recommendations, crimeInformation, lostPets, localServices, generalNews;

    /**
     * Constructor creating layout
     */
    public NoticeBoard(){
        super(R.layout.activity_notice_board);
    }

    /**
     * Creates the notice board layout
     * @param savedInstanceState
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        events = findViewById(R.id.Events_chip);
        recommendations = findViewById(R.id.Recommendations_chip);
        crimeInformation = findViewById(R.id.CrimeInfo_chip);
        lostPets = findViewById(R.id.lostPets_chip);
        localServices = findViewById(R.id.localServices_chip);
        generalNews = findViewById(R.id.GeneralPost_chip);

        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationNotice);

        //set posts selected
        bottomNavigationView.setSelectedItemId(nav_noticeBoard);

        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId())
            {
                case nav_noticeBoard:
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
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        context=this;
        FloatingActionButton addPost = findViewById(R.id.floating_action_button);
        addPost.setOnClickListener(this);
        recyclerView= findViewById(R.id.recyclerViewNotice);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        getCommunities(this::readPost);
    }

    /**
     * Reads posts to be displayed
     * @param communities
     */
    private void readPost(ArrayList<String> communities){
        final ArrayList<Post> postArrayList = new ArrayList<>();
        Iterator<String> iterator = communities.iterator();
        while (iterator.hasNext()) {
            String community = iterator.next();
            firebase.readPostForNoticeBoard(community).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Post post;
                    for (DataSnapshot content : snapshot.getChildren()) {
                        post = content.getValue(Post.class);
                        postArrayList.add(post);
                    }

                    Collections.sort(postArrayList);

                    if (!iterator.hasNext()) {
                        postAdapter = new NoticeBoardAdapter(postArrayList, context);
                        recyclerView.setAdapter(postAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(NoticeBoard.this, "Some error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     * Reads communities user is a part of
     * @param userCommunities
     */
    private void getCommunities(NoticeBoard.userCommunitiesI userCommunities){
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

                userCommunities.onCallback(communitiesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NoticeBoard.this, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Click listener
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.floating_action_button){
            Intent addPost = new Intent(NoticeBoard.this, CreatePost.class);
            startActivity(addPost);
        }
    }

    /**
     * Click listener for filtering
     * @param view
     */
    @SuppressLint("NonConstantResourceId")
    public void OnCheckedChangeListener(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        if(postAdapter!=null){
            switch (view.getId()) {
                case R.id.Events_chip:
                    if (isChecked) {
                        postAdapter.getFilter().filter(events.getText().toString().toLowerCase());
                    } else {
                        postAdapter.getFilter().filter("");
                    }
                    break;
                case R.id.Recommendations_chip:
                    if (isChecked) {
                        postAdapter.getFilter().filter(recommendations.getText().toString().toLowerCase());
                    } else {
                        postAdapter.getFilter().filter("");
                    }
                    break;
                case R.id.CrimeInfo_chip:
                    if (isChecked) {
                        postAdapter.getFilter().filter(crimeInformation.getText().toString().toLowerCase());
                    } else {
                        postAdapter.getFilter().filter("");
                    }
                    break;
                case R.id.lostPets_chip:
                    if (isChecked) {
                        postAdapter.getFilter().filter(lostPets.getText().toString().toLowerCase());
                    } else {
                        postAdapter.getFilter().filter("");
                    }
                    break;
                case R.id.localServices_chip:
                    if (isChecked) {
                        postAdapter.getFilter().filter(localServices.getText().toString().toLowerCase());
                    } else {
                        postAdapter.getFilter().filter("");
                    }
                    break;
                case R.id.GeneralPost_chip:
                    if (isChecked) {
                        postAdapter.getFilter().filter(generalNews.getText().toString().toLowerCase());
                    } else {
                        postAdapter.getFilter().filter("");
                    }
                    break;
            }
        }else{
            Toast.makeText(NoticeBoard.this, "Posts still loading!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Interface for the getting of user community
     */
    private interface userCommunitiesI {
        /**
         * Creates a callback for community
         * @param communities
         */
        void onCallback(ArrayList<String> communities);
    }

}