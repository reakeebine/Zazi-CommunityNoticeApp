package com.inform.communitynoticeapp;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Database class
 */
@SuppressWarnings("JavaDoc")
public class FirebaseConnector implements Cloneable, Serializable {

    private final FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private static final FirebaseConnector instance = new FirebaseConnector();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * Empty Constructor
     */
    private FirebaseConnector(){
        if(instance!=null){
            throw new IllegalStateException("Firebase database instance is already created");
        }
    }

    /**
     * Gets instance of class
     */
    public static FirebaseConnector getInstance(){
        return instance;
    }

    /**
     * Storage reference
     */
    public FirebaseStorage getFBStorage() {
        return FirebaseStorage.getInstance();
    }

    /**
     * ReadResolve
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        return instance;
    }

    /**
     * Write replace
     * @throws ObjectStreamException
     */
    private  Object writeReplace() throws  ObjectStreamException{
        return instance;
    }

    /**
     * Clone
     * @throws CloneNotSupportedException
     */
    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException{
        throw new CloneNotSupportedException();
    }

    /**
     * Firebase auth object
     */
    public FirebaseAuth getUserAuth(){
        return userAuth;
    }

    /**
     * Firebase user object
     */
    public FirebaseUser getUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Storage reference
     */
    public StorageReference getStorageRef() {
        return FirebaseStorage.getInstance().getReference();
    }

    /**
     * Database reference
     */
    private DatabaseReference getRootRef(){
        return FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Adds communities
     * @param community
     */
    public Task<Void> addCommunityToFirebase(String community){
        DatabaseReference communityRef = this.getRootRef().child("Communities").getRef();
        DatabaseReference newRef = communityRef.push();
        Community com = new Community(community);
        return newRef.setValue(com);
    }

    /**
     * Read communities
     */
    public DatabaseReference readCommunities(){
        return this.getRootRef().child("Communities").getRef();
    }

    /**
     * Save user in firebase
     */
    public void saveNameInFirebase(UserDetails userCurrent, String community){
        DatabaseReference nameRef = this.getRootRef().child("Users").child(Objects.requireNonNull(userAuth.getUid()));
        nameRef.setValue(userCurrent);
        this.joinCommunity(community);
    }

    /**
     * Sign in user
     */
    public Task<AuthResult> signInUser(String email, String password){
        return userAuth.signInWithEmailAndPassword(email, password);
    }

    /**
     * Create user
     */
    public Task<AuthResult> signUpUser(String email, String password){
        return  userAuth.createUserWithEmailAndPassword(email, password);
    }

    /**
     * Send verification email
     */
    public Task<Void> sendVerificationEmail() {
        return Objects.requireNonNull(userAuth.getCurrentUser()).sendEmailVerification();
    }

    /**
     * Check email verified
     */
    public boolean checkIfEmailIsVerified(){
        return Objects.requireNonNull(userAuth.getCurrentUser()).isEmailVerified();
    }

    /**
     * Update display name
     */
    public void updateDispName(String dispName){
        FirebaseUser currentUser = userAuth.getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(dispName).build();
        assert currentUser != null;
        currentUser.updateProfile(profileChangeRequest);
    }

    /**
     * Get display name
     */
    public String getDisplayName() {
        return getUser().getDisplayName();
    }

    /**
     * User details ref
     */
    public DatabaseReference getUserDetailsRef(){
        return getRootRef().child("Users").child(getUser().getUid()).getRef();
    }

    /**
     * Update email
     * @param email
     */
    public Task<Void> updateEmail(String email){
        return Objects.requireNonNull(userAuth.getCurrentUser()).updateEmail(email);
    }

    /**
     * Update password
     * @param password
     */
    public Task<Void> updatePassword(String password){
        return Objects.requireNonNull(userAuth.getCurrentUser()).updatePassword(password);
    }

    /**
     * get display picture
     */
    public Uri getDisplayPicture() {
        return getUser().getPhotoUrl();
    }


    /**
     * Read notice board posts
     * @param community
     */
    public Query readPostForNoticeBoard(String community){
        return this.getRootRef().child("Posts").child("NoticeBoard").orderByChild("community").equalTo(community).limitToFirst(100);
    }

    /**
     * Read message board posts
     * @param community
     */
    public Query readPostForMessageBoard(String community){
        return this.getRootRef().child("Posts").child("MessageBoard").orderByChild("community").equalTo(community).limitToFirst(100);
    }

    /**
     * Read bookmarks
     */
    public Query readBookmarks(){
        return this.getRootRef().child("Bookmarks").child(this.getUser().getUid()).orderByChild("dateTime").getRef();
    }

    /**
     * Write notice board posts
     * @param community
     * @param imageUri
     * @param hashtags
     * @param dateNow
     * @param text
     */
    public Task<Void> addPostToNoticeBoardNode(String text, String dateNow, String imageUri, ArrayList<String> hashtags, String community){
        DatabaseReference postRef = this.getRootRef().child("Posts").child("NoticeBoard").getRef();
        DatabaseReference newRef = postRef.push();
        Post post = new Post(this.getUser().getDisplayName(), text, dateNow, imageUri, newRef.getKey(), hashtags, community);
        return newRef.setValue(post);
    }

    /**
     * Write message board posts
     * @param community
     * @param imageUri
     * @param hashtags
     * @param dateNow
     * @param text
     */
    public Task<Void> addPostToMessageBoardNode(String text, String dateNow, String imageUri, ArrayList<String> hashtags, String community){
        DatabaseReference postRef = this.getRootRef().child("Posts").child("MessageBoard").getRef();
        DatabaseReference newRef = postRef.push();
        Post post = new Post(this.getUser().getDisplayName(), text, dateNow, imageUri, newRef.getKey(), hashtags, community);
        return newRef.setValue(post);
    }

    /**
     * Write bookmarks
     * @param post
     */
    public Task<Void> addPostToBookmarks(@NonNull Post post){
        DatabaseReference bookmarkRef = this.getRootRef().child("Bookmarks").child(this.getUser().getUid()).getRef();
        DatabaseReference newRef = bookmarkRef.push();
        return newRef.setValue(post);
    }

    /**
     * Remove bookmarks
     * @param postID
     */
    public Query removeBookmark(String postID){
        return this.getRootRef().child("Bookmarks").child(this.getUser().getUid()).orderByChild("postID").equalTo(postID);
    }

    /**
     * Read requests
     */
    public DatabaseReference readRequests(){
        return this.getRootRef().child("Requests").getRef();
    }

    /**
     * Write requests
     * @param reason
     * @param dateNow
     */
    public Task<Void> addRequest(String reason, String dateNow){
        getRootRef().child("Users").child(this.getUser().getUid()).child("requestStatus").setValue("Pending");
        DatabaseReference requestRef = this.getRootRef().child("Requests").getRef();
        DatabaseReference newRef = requestRef.push();
        Request newRequest = new Request(this.getUser().getUid(), this.getUser().getDisplayName(), this.getUser().getEmail(), reason, dateNow, newRef.getKey());
        return newRef.setValue(newRequest);
    }

    /**
     * accept requests
     * @param requestID
     * @param userID
     */
    public void acceptRequest(String requestID, String userID) {
        getRootRef().child("Users").child(userID).child("role").setValue("Service provider");
        getRootRef().child("Requests").child(requestID).child("status").setValue("Accepted");
        getRootRef().child("Users").child(userID).child("requestStatus").setValue("Accepted");
    }

    /**
     * decline requests
     * @param requestID
     * @param userID
     */
    public void declineRequest(String requestID, String userID) {
        getRootRef().child("Requests").child(requestID).child("status").setValue("Declined");
        getRootRef().child("Users").child(userID).child("requestStatus").setValue("Declined");
    }

    /**
     * Join community
     * @param community
     */
    public void joinCommunity(String community) {
        DatabaseReference communityRef = this.getRootRef().child("Users").child(Objects.requireNonNull(userAuth.getUid())).child("Communities").getRef();
        DatabaseReference newRef = communityRef.push();
        Community com = new Community(community);
        newRef.setValue(com);
    }

    /**
     * leave community
     * @param communityID
     */
    public void leaveCommunity(String communityID) {
        this.getRootRef().child("Users").child(Objects.requireNonNull(userAuth.getUid())).child("Communities").child(communityID).removeValue();
    }

    /**
     * Get user communities
     */
    public DatabaseReference getUserCommunities(){
        return this.getRootRef().child("Users").child(Objects.requireNonNull(userAuth.getUid())).child("Communities").getRef();
    }

    /**
     * like post
     * @param position
     * @param postList
     */
    public void likePost( int position, ArrayList<Post> postList) {
        this.getRootRef().child("likes").child(postList.get(position).getPostID()).child(this.getUser().getUid()).setValue(true);
    }

    /**
     * like post reference
     * @param postID
     */
    public DatabaseReference like(String postID){
        return  this.getRootRef().child("likes").child(postID);
    }

    /**
     * unlike post
     * @param position
     * @param postList
     */
    public void unlikePost( int position, ArrayList<Post> postList) {
        this.getRootRef().child("likes").child(postList.get(position).getPostID()).child(this.getUser().getUid()).removeValue();
    }

}
