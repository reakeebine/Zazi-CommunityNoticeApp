package com.inform.communitynoticeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * The adapter takes an object of view holder class, we make our own view holder class
 * instead of using RecyclerView.ViewHolder we want to define our own text view.
 */
@SuppressWarnings("JavaDoc")
public class MessageBoardAdapter extends RecyclerView.Adapter<MessageBoardAdapter.ViewHolder> implements Filterable {
    private final ArrayList<Post> postList;
    private final ArrayList<Post> postListFull;
    private final Context context;
    private final FirebaseConnector firebase= FirebaseConnector.getInstance();

    /**
     * Constructor used to make object of class
     * @param postList
     * @param context
     */
    public MessageBoardAdapter(ArrayList<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
        postListFull=new ArrayList<>(postList);
    }
    /**
     * Handles liking post
     * @param postID
     * @param imageView
     */
    //method for liking/disliking
    private void likePost(String postID , ImageView imageView) {
        firebase.like(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebase.getUser().getUid()).exists()){
                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("liked");
                }
                else {
                    imageView.setImageResource(R.drawable.dislike);
                    imageView.setTag("like");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Maintains number of likes
     * @param likes
     * @param postId
     */
    //method to increment likes
    private void numberOfLikes(TextView likes , String postId) {
        firebase.like(postId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+" likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is used to inflate the layout
     * @param parent
     * @param viewType
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card, parent, false);
        return new MessageBoardAdapter.ViewHolder(postView);
    }

    /**
     * This method is used to bind the view, displays post cards and their properties
     * @param holder
     * @param position
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.dispName.setText(postList.get(position).getUser());
        holder.dateTime.setText(postList.get(position).getDateTime());
        holder.community.setText(postList.get(position).getCommunity());
        holder.postID.setText(postList.get(position).getPostID());

        SharedPreferences preferences = getSharedPreferences();
        String state = preferences.getString(position +"pressed", "no");

        //Used to make sure the button is still bookmarked when you leave and come back to the screen
        if(state.equals("yes")){
            holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.clicked_bookmark));
        }else{
            holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.ic_baseline_bookmark));
        }

        //Change bookmark colour when clicking bookmark button
        holder.bookmark.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            SharedPreferences.Editor editor = getSharedPreferences().edit();
            if (isChecked && state.equals("no")) {
                editor.putString(position + "pressed", "yes");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.clicked_bookmark));
                addPostToBookmarks(postList.get(position));
            } else  if (isChecked && state.equals("yes")) {
                editor.putString(position + "pressed", "no");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.ic_baseline_bookmark));
                removePostBookmark(holder.postID.getText().toString());
            } else  if (!isChecked && state.equals("no")) {
                editor.putString(position + "pressed", "no");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.ic_baseline_bookmark));
                removePostBookmark(holder.postID.getText().toString());
            } else  if (!isChecked && state.equals("yes")) {
                editor.putString(position + "pressed", "yes");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.clicked_bookmark));
                addPostToBookmarks(postList.get(position));
            }

        });


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
                holder.postPicIV.setVisibility(View.VISIBLE);
            }).addOnFailureListener(e -> {
                //handle failure
                Toast.makeText(context, "An error occurred: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            holder.postPicIV.setVisibility(View.GONE);
        }

        //Utilizing the like/dislike and the likes increment methods
        likePost(postList.get(position).getPostID(),holder.like_button);
        numberOfLikes(holder.like_Textview,postList.get(position).getPostID());

        holder.like_button.setOnClickListener(view -> {
            if(holder.like_button.getTag().equals("like")) {
                firebase.likePost(position,postList);

            }
            else{
                firebase.unlikePost(position,postList);
            }
        });
    }

    /**
     * This method is used to remove bookmarks
     * @param postID
     */
    private void removePostBookmark(String postID) {
        firebase.removeBookmark(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    dataSnapshot.getRef().removeValue();
                }
                Toast.makeText(context, "Bookmark removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "An error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is used to add bookmarks
     * @param post
     */
    private void addPostToBookmarks(Post post) {
        firebase.addPostToBookmarks(post).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(context, "Bookmark added", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "An error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Gets shared preferences
     */
    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("MessageBoardButton", Context.MODE_PRIVATE);
    }

    /**
     * Gets postList size
     */
    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * Gets filter object
     */
    @Override
    public Filter getFilter() {
        return postFilter;
    }

    private final Filter postFilter = new Filter() {
        /**
         * This method is used to filter posts
         * @param charSequence
         */
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Post> filteredList = new ArrayList<>();
            if(charSequence==null || charSequence.length()==0){
                filteredList.addAll(postListFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Post post: postListFull){
                    for(String tag:post.getHashtags()){
                        if(tag.toLowerCase().equals(filterPattern)){
                            filteredList.add(post);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values=filteredList;
            return results;
        }

        /**
         * Shows results of filtering
         * @param charSequence
         * @param filterResults
         */
        @SuppressWarnings("unchecked")
        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            postList.clear();
            postList.addAll((List<? extends Post>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    /**
     * Inner class holding the post cards
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dispName, post, dateTime, postID, like_Textview, community;
        ImageView postPicIV,like_button;
        MaterialCardView cardView;
        ToggleButton bookmark;
        LinearLayout linearLayout;
        /**
         * Constructor for community card view
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.post_content);
            dispName =itemView.findViewById(R.id.display_name);
            dateTime=itemView.findViewById(R.id.dateTime_TV);
            community=itemView.findViewById(R.id.community_TV);
            postPicIV=itemView.findViewById(R.id.postPic_IV);
            cardView=itemView.findViewById(R.id.cardview);
            bookmark=itemView.findViewById(R.id.bookmark_Btn);
            postID=itemView.findViewById(R.id.postID);
            linearLayout=itemView.findViewById(R.id.linear_LL);
            like_button = itemView.findViewById(R.id.like_btn);
            like_Textview = itemView.findViewById(R.id.likes_textview);
        }
    }
}
