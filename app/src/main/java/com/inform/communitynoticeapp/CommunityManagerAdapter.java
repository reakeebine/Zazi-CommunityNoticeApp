package com.inform.communitynoticeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * The adapter takes an object of view holder class, we make our own view holder class
 * instead of using RecyclerView.ViewHolder we want to define our own text view.
 */
@SuppressWarnings("JavaDoc")
public class CommunityManagerAdapter extends RecyclerView.Adapter<CommunityManagerAdapter.ViewHolder> {
    private final ArrayList<String> communitiesList;
    private final Context context;

    /**
     * Constructor used to make object of class
     * @param communitiesList
     * @param context
     */
    public CommunityManagerAdapter(ArrayList<String> communitiesList, Context context) {
        this.communitiesList = communitiesList;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    /**
     * Inner class holding the bookmark cards
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView communityTV;
        MaterialCardView cardView;
        /**
         * Constructor for community card view
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            communityTV = itemView.findViewById(R.id.community_TV);
            cardView = itemView.findViewById(R.id.community_manager_cardView);
        }
    }


    /**
     * This method is used to inflate the layout
     * @param parent
     * @param viewType
     */
    @NonNull
    @Override
    public CommunityManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View requestView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_manager_card, parent, false);
        return new CommunityManagerAdapter.ViewHolder(requestView);
    }

    /**
     * This method is used to bind the view, displays community cards and their properties
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CommunityManagerAdapter.ViewHolder holder, int position) {
        holder.communityTV.setText(communitiesList.get(position));
    }

    /**
     * This method is used to get the size of the community list size
     */
    @Override
    public int getItemCount() {
        return communitiesList.size();
    }
}
