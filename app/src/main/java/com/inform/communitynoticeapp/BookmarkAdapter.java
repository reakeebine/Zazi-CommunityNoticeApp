package com.inform.communitynoticeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * The adapter takes an object of view holder class, we make our own view holder class
 * instead of using RecyclerView.ViewHolder we want to define our own text view.
 */
@SuppressWarnings("JavaDoc")
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder>{
    private final ArrayList<Post> postList;
    private final Context context;
    private final FirebaseConnector firebase= FirebaseConnector.getInstance();

    /**
     * Constructor used to make object of class
     * @param postList
     * @param context
     */
    public BookmarkAdapter(ArrayList<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    /**
     * This method is used to inflate the layout
     * @param parent
     * @param viewType
     */
    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_card, parent, false);
        return new BookmarkAdapter.ViewHolder(postView);
    }

    /**
     * This method is used to bind the view, displays bookmark cards and their properties
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder holder, int position) {
        holder.dispName.setText(postList.get(position).getUser());
        holder.dateTime.setText(postList.get(position).getDateTime());
        holder.community.setText(postList.get(position).getCommunity());

        if (!postList.get(position).getPost().equals("")) {
            holder.post.setText(postList.get(position).getPost());
        } else {
            holder.post.getLayoutParams().height = 0;
        }

        //show post picture
        if (!postList.get(position).getImageUri().equals("")) {
            StorageReference photoRef = firebase.getFBStorage().getReferenceFromUrl(postList.get(position).getImageUri());

            final long ONE_MEGABYTE = 1024 * 1024;
            photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.postPicIV.setImageBitmap(bmp);
            }).addOnFailureListener(e -> {
                //handle failure
                Toast.makeText(context, "An error occurred: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            holder.postPicIV.getLayoutParams().height = 0;
            holder.postPicIV.requestLayout();
        }

    }

    /**
     * This method is used to get the size of the bookmark list
     */
    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * Inner class holding the bookmark cards
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dispName, post, dateTime, community;
        ImageView postPicIV;
        MaterialCardView cardView;
        /**
         * Constructor for bookmark card view
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.post_contentThree);
            dispName =itemView.findViewById(R.id.display_nameThree);
            dateTime=itemView.findViewById(R.id.dateTime_TVThree);
            community=itemView.findViewById(R.id.community_TVThree);
            postPicIV=itemView.findViewById(R.id.postPic_IVThree);
            cardView=itemView.findViewById(R.id.cardviewThree);
        }
    }
}
