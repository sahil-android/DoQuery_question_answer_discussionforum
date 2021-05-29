package com.example.doquery;

import com.google.firebase.firestore.Exclude;

public class Search {

    private String question;
    private String usernameEmail;
    private  String idQuestion;

    public Search(){
        //no argument constructor needed
    }
    public Search(String question, String usernameEmail){
        this.question = question;
        this.usernameEmail = usernameEmail;
    }

    public String getQuestion() {
        return question;
    }

    public String getUsernameEmail() {
        return usernameEmail;
    }

    @Exclude
    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }
}
