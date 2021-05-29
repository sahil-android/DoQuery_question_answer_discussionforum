package com.example.doquery;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

public class Question {

    private String question;
    private String usernameEmail;
    private Timestamp timestamp;

    public Question(){
        //no argument constructor needed
    }
    public Question(String question, String usernameEmail, Timestamp timestamp){
        this.question = question;
        this.usernameEmail = usernameEmail;
        this.timestamp = timestamp;
    }

    public String getQuestion() {
        return question;
    }

    public String getUsernameEmail() {
        return usernameEmail;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
