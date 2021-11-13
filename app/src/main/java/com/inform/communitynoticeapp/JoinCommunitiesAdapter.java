package com.inform.communitynoticeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * The adapter takes an object of view holder class, we make our own view holder class
 * instead of using RecyclerView.ViewHolder we want to define our own text view.
 */
@SuppressWarnings("JavaDoc")
public class JoinCommunitiesAdapter extends RecyclerView.Adapter<JoinCommunitiesAdapter.ViewHolder> {
    private final ArrayList<String> communitiesList;
    private final Context context;
    private final FirebaseConnector firebase= FirebaseConnector.getInstance();


    /**
     * Constructor used to make object of class
     * @param communitiesList
     * @param context
     */
    public JoinCommunitiesAdapter(ArrayList<String> communitiesList, Context context) {
        this.communitiesList = communitiesList;
        this.context = context;
    }

    /**
     * Inner class holding the bookmark cards
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView communityTV;
        MaterialCardView cardView;
        MaterialButton joinBtn, leaveBtn;

        /**
         * Constructor for community card view
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            communityTV = itemView.findViewById(R.id.communityName_TV);
            cardView = itemView.findViewById(R.id.join_community_cardView);
            joinBtn = itemView.findViewById(R.id.join_Btn);
            leaveBtn = itemView.findViewById(R.id.leave_Btn);
        }
    }


    /**
     * This method is used to inflate the layout
     * @param parent
     * @param viewType
     */
    @NonNull
    @Override
    public JoinCommunitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View communityView = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_community_card, parent, false);
        return new JoinCommunitiesAdapter.ViewHolder(communityView);
    }

    /**
     * This method is used to bind the view, displays community cards and their properties
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull JoinCommunitiesAdapter.ViewHolder holder, int position) {
        holder.communityTV.setText(communitiesList.get(position));
        final String thisCommunity = communitiesList.get(position);

        holder.joinBtn.setOnClickListener(view -> firebase.getUserCommunities().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean alreadyIn = false;
                Community aCommunity;
                for(DataSnapshot content: snapshot.getChildren()){
                    aCommunity = content.getValue(Community.class);
                    assert aCommunity != null;
                    if (aCommunity.getName().equals(thisCommunity)) {
                        alreadyIn = true;
                    }
                }

                if (!alreadyIn) {
                    firebase.joinCommunity(thisCommunity);
                    Toast.makeText(context, "You have joined " + thisCommunity, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "You are already in this community.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }));

        holder.leaveBtn.setOnClickListener(view -> firebase.getUserCommunities().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String communityKey = null;
                Community aCommunity;
                for (DataSnapshot content : snapshot.getChildren()) {
                    aCommunity = content.getValue(Community.class);
                    assert aCommunity != null;
                    if (aCommunity.getName().equals(thisCommunity)) {
                        communityKey = content.getKey();
                    }
                }

                if (communityKey != null) {
                    if (snapshot.getChildrenCount()>1) {
                        firebase.leaveCommunity(communityKey);
                        Toast.makeText(context, "You have left " + thisCommunity, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "You must be in at least one community.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "You cannot leave a community you are not part of.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    /**
     * This method is used to get the size of the community list
     */
    @Override
    public int getItemCount() {
        return communitiesList.size();
    }
}
