package com.inform.communitynoticeapp;

import java.util.ArrayList;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Model class for the posts
 */
@SuppressWarnings("JavaDoc")
public class Post implements Comparable<Post> {
    private String post;
    private String user;
    private String dateTime;
    private String imageUri;
    private String postID;
    private ArrayList<String> hashtags;
    private String community;

    /**
     * Empty Constructor
     */
    public Post(){

    }

    /**
     * constructor
     * @param user
     * @param community
     * @param dateTime
     * @param hashtags
     * @param post
     * @param imageUri
     * @param postID
     */
    public Post(String user, String post, String dateTime, String imageUri, String postID, ArrayList<String> hashtags, String community){
        this.user=user;
        this.post=post;
        this.dateTime=dateTime;
        this.imageUri = imageUri;
        this.postID=postID;
        this.hashtags=hashtags;
        this.community=community;
    }

    /**
     * getter method for user
     */
    public String getUser() {
        return user;
    }

    /**
     * getter method for post
     */
    public String getPost() {
        return post;
    }

    /**
     * getter method for date and time
     */
    public String getDateTime(){return  dateTime;}

    /**
     * getter method for image
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * getter method for post id
     */
    public String getPostID() {
        return postID;
    }

    /**
     * getter method for hashtags
     */
    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    /**
     * getter method for community
     */
    public String getCommunity() {
        return community;
    }

    /**
     * Setter method for date and time
     * @param dateTime
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Setter method for hashtags
     * @param hashtags
     */
    public void setHashtags(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    /**
     * Setter method for community
     * @param community
     */
    public void setCommunity(String community) {
        this.community = community;
    }

    /**
     * Setter method for post
     * @param post
     */
    public void setPost(String post) {
        this.post = post;
    }

    /**
     * Setter method for user
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Setter method for image
     * @param imageUri
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * Setter method for postID
     * @param postID
     */
    public void setPostID(String postID) {
        this.postID = postID;
    }

    /**
     * Compare to method
     * @param post
     */
    @Override
    public int compareTo(Post post) {
        if (this.postID.equals(post.getPostID())) {
            return 0;
        } else if (this.postID.compareTo(post.getPostID())>0) {
            return -1;
        } else {
            return 1;
        }
    }
}
