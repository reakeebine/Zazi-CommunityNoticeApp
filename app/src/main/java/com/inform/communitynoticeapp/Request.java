package com.inform.communitynoticeapp;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Model class for the requests
 */
@SuppressWarnings("JavaDoc")
public class Request {

    private String requestID;
    private String userID;
    private String displayName;
    private String emailAddress;
    private String reason;
    private String dateTime;
    private String status;

    /**
     * Empty Constructor
     */
    public Request() {}

    /**
     * One argument constructor
     * @param userID
     * @param displayName
     * @param emailAddress
     * @param reason
     * @param dateTime
     * @param requestID
     */
    public Request(String userID, String displayName, String emailAddress, String reason, String dateTime, String requestID) {
        this.userID = userID;
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.reason = reason;
        this.dateTime = dateTime;
        this.status = "Pending";
        this.requestID = requestID;
    }

    /**
     * getter method for userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Setter method for user ID
     * @param userID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * getter method for Display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter method for Display name
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * getter method for email
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Setter method for email
     * @param emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * getter method for reason for request
     */
    public String getReason() {
        return reason;
    }

    /**
     * Setter method for reason for request
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * getter method for date and time
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Setter method for date and time
     * @param dateTime
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * getter method for status of request
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter request status
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * getter method for request ID
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Setter method for Request ID
     * @param requestID
     */
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
}
