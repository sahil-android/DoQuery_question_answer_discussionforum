package com.example.doquery;

import com.google.firebase.Timestamp;

public class Answer {

    private String answer;
    private String questionID;
    private String usernameEmail;
   private Timestamp timestamp;
   private int like;

    public Answer(){
        //no argument constructor needed
    }
    public Answer(String answer, String questionID, String usernameEmail, Timestamp timestamp, int like){
        this.answer = answer;
        this.questionID = questionID;
        this.usernameEmail = usernameEmail;
       this.timestamp = timestamp;
       this.like = like;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestionID() {
        return questionID;
    }

    public String getUsernameEmail() {
        return usernameEmail;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
