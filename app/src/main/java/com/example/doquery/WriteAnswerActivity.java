package com.example.doquery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WriteAnswerActivity extends AppCompatActivity {

    private EditText writtenAnswer;
    private String usernameEmail;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);



        writtenAnswer = findViewById(R.id.writtenAnswer);
        Intent myintent = getIntent();
        id = myintent.getStringExtra("id");


        SharedPreferences sharedPreferences = getSharedPreferences("SHARED PREF", MODE_PRIVATE);
        usernameEmail = sharedPreferences.getString("username", "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_answer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note1:
                saveNote();
                return true;
            case R.id.backactivity:
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote(){

        String answer = writtenAnswer.getText().toString();

        if(answer.trim().isEmpty()){
            Toast.makeText(this, "Please insert question and username", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference answerRef = FirebaseFirestore.getInstance().collection("QuestionCollection")
                .document(id).collection("Answers");
        Map<String,Object> hello = new HashMap<>();
        hello.put("answer", answer);
        hello.put("questionID", id);
        hello.put("usernameEmail", usernameEmail);
        hello.put("timestamp", FieldValue.serverTimestamp());
        hello.put("like",0);
        answerRef.add(hello);
        Toast.makeText(this, "Answer Added", Toast.LENGTH_SHORT).show();
        finish();
    }

}