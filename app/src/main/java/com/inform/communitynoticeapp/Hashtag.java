package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Gets hashtags user selects for post
 */
@SuppressWarnings("JavaDoc")
public class Hashtag extends AppCompatActivity implements View.OnClickListener {

    private CheckBox eventsCB, recommendationsCB, crimeInfoCB, lostPetsCB, localServicesCB, generalNewsCB;
    private Map<String, Object> checked;
    private List<String> hashtags;

    /**
     * Creates the hashtag layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag);
        eventsCB = findViewById(R.id.eventCB);
        recommendationsCB = findViewById(R.id.recommendationCB);
        crimeInfoCB = findViewById(R.id.crimeInfoCB);
        lostPetsCB = findViewById(R.id.lostPetsCB);
        localServicesCB = findViewById(R.id.localServicesCB);
        generalNewsCB = findViewById(R.id.generalPostCB);
        Button doneB = findViewById(R.id.doneB);
        Button clear = findViewById(R.id.clearB);
        checked = new HashMap<>();
        hashtags = new ArrayList<>();

        doneB.setOnClickListener(this);
        clear.setOnClickListener(view -> {
            if(eventsCB.isChecked()){
                eventsCB.setChecked(false);
            }if(recommendationsCB.isChecked()){
                recommendationsCB.setChecked(false);
            }if(crimeInfoCB.isChecked()){
                crimeInfoCB.setChecked(false);
            }if(lostPetsCB.isChecked()){
                lostPetsCB.setChecked(false);
            }if(localServicesCB.isChecked()){
                localServicesCB.setChecked(false);
            }if(generalNewsCB.isChecked()){
                generalNewsCB.setChecked(false);
            }
            checked.clear();
        });
    }

    /**
     * Click listener
     * @param view
     */
    @Override
    public void onClick(View view) {
        checked.put("Hashtags",hashtags);
        int id = view.getId();
        if(id==R.id.doneB){
            Intent post = new Intent();
            post.putExtra("extra", (Serializable) checked);
            setResult(RESULT_OK, post);
            finish();
        }
    }

    /**
     * Click listener
     * @param view
     */
    @SuppressLint("NonConstantResourceId")
    public void onCheckBoxClicked(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.eventCB:
                if(isChecked){
                    hashtags.add(eventsCB.getText().toString());
                }else{
                    hashtags.remove(eventsCB.getText().toString());
                }
                break;
            case R.id.recommendationCB:
                if(isChecked){
                    hashtags.add(recommendationsCB.getText().toString());
                }else{
                    hashtags.remove(recommendationsCB.getText().toString());
                }
                break;
            case R.id.crimeInfoCB:
                if(isChecked){
                    hashtags.add(crimeInfoCB.getText().toString());
                }else{
                    hashtags.remove(crimeInfoCB.getText().toString());
                }
                break;
            case R.id.lostPetsCB:
                if(isChecked){
                    hashtags.add(lostPetsCB.getText().toString());
                }else{
                    hashtags.remove(lostPetsCB.getText().toString());
                }
                break;
            case R.id.localServicesCB:
                if(isChecked){
                    hashtags.add(localServicesCB.getText().toString());
                }else{
                    hashtags.remove(localServicesCB.getText().toString());
                }
                break;
            case R.id.generalPostCB:
                if(isChecked){
                    hashtags.add(generalNewsCB.getText().toString());
                }else{
                    hashtags.remove(generalNewsCB.getText().toString());
                }
                break;
        }
    }
}