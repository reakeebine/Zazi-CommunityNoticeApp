package com.inform.communitynoticeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * The adapter takes an object of view holder class, we make our own view holder class
 * instead of using RecyclerView.ViewHolder we want to define our own text view.
 */
@SuppressWarnings("JavaDoc")
public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {
    private final ArrayList<Request> requestsList;
    private final Context context;
    private final FirebaseConnector firebase= FirebaseConnector.getInstance();

    /**
     * Constructor used to make object of class
     * @param requestsList
     * @param context
     */
    public RequestsAdapter(ArrayList<Request> requestsList, Context context) {
        this.requestsList = requestsList;
        this.context = context;
    }

    /**
     * getter for context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Inner class holding the request cards
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView displayNameTV, emailTV, dateTimeTV, reasonTV;
        MaterialCardView cardView;
        MaterialButton acceptBtn, declineBtn;
        /**
         * Constructor for request card view
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayNameTV = itemView.findViewById(R.id.displayName_TV);
            emailTV = itemView.findViewById(R.id.email_TV);
            dateTimeTV = itemView.findViewById(R.id.dateTime_TV);
            reasonTV = itemView.findViewById(R.id.reason_TV);
            cardView = itemView.findViewById(R.id.request_cardView);
            acceptBtn = itemView.findViewById(R.id.accept_Btn);
            declineBtn = itemView.findViewById(R.id.decline_Btn);
        }
    }

    /**
     * This method is used to inflate the layout
     * @param parent
     * @param viewType
     */
    @NonNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View requestView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false);
        return new RequestsAdapter.ViewHolder(requestView);
    }

    /**
     * This method is used to bind the view, displays request cards and their properties
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.ViewHolder holder, int position) {
        holder.displayNameTV.setText(requestsList.get(position).getDisplayName());
        holder.emailTV.setText(requestsList.get(position).getEmailAddress());
        holder.dateTimeTV.setText(requestsList.get(position).getDateTime());
        holder.reasonTV.setText(requestsList.get(position).getReason());

        holder.acceptBtn.setOnClickListener(view -> firebase.acceptRequest(requestsList.get(position).getRequestID(), requestsList.get(position).getUserID()));
        holder.declineBtn.setOnClickListener(view -> firebase.declineRequest(requestsList.get(position).getRequestID(), requestsList.get(position).getUserID()));
    }

    /**
     * This method is used to get the size of the request list
     */
    @Override
    public int getItemCount() {
        return requestsList.size();
    }
}
