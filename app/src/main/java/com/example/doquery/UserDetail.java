package com.example.doquery;

import com.google.firebase.firestore.Exclude;

public class UserDetail {

    private String userEmail;
    private String userPassword;
    private String userId;

    public UserDetail(){
        //no argument constructor needed
    }

    public UserDetail(String userEmail, String userPassword){
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    @Exclude
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
