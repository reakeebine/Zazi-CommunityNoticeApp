package com.inform.communitynoticeapp;

import java.util.ArrayList;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Model class for the users
 */
@SuppressWarnings("JavaDoc")
public class UserDetails {
    private String email, dispName, community, role, requestStatus;
    private ArrayList<String> communities;

    /**
     * Empty Constructor
     */
    public UserDetails(){

    }

    /**
     * constructor
     * @param dispName
     * @param email
     * @param community
     * @param role
     */
    public UserDetails(String dispName, String email, String community, String role){
        this.dispName=dispName;
        this.email=email;
        this.community=community;
        this.role=role;
        this.requestStatus="None";
    }

    /**
     * constructor
     * @param dispName
     * @param email
     * @param role
     */
    public UserDetails(String dispName, String email, String role){
        this.dispName=dispName;
        this.email=email;
        this.role=role;
        this.requestStatus="None";
    }

    /**
     * getter method for community
     */
    public String getCommunity() {
        return community;
    }

    /**
     * Setter method for community
     * @param community
     */
    public void setCommunity(String community) {
        this.community = community;
    }

    /**
     * getter method for email
     */
    public String getEmail(){
        return email;
    }

    /**
     * Setter method for email
     * @param email
     */
    public void setEmail(String email){
        this.email=email;
    }

    /**
     * getter method for display name
     */
    public String getDispName(){return dispName;}

    /**
     * Setter method for display name
     * @param dispName
     */
    public void setDispName(String dispName){
        this.dispName=dispName;
    }

    /**
     * getter method for user role
     */
    public String getRole() {
        return role;
    }

    /**
     * Setter method for role
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * getter method for request status
     */
    public String getRequestStatus() {
        return requestStatus;
    }

    /**
     * Setter method for request status
     * @param requestStatus
     */
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
