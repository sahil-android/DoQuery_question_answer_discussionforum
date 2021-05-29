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

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class WriteQuestionActivity extends AppCompatActivity {

    private EditText writtenQuestion;
    private String usernameEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_question);

        Intent newintent = getIntent();
        usernameEmail = newintent.getStringExtra("username");
        writtenQuestion = findViewById(R.id.writtenQuestion);

        SharedPreferences sharedPreferences = getSharedPreferences("SHARED PREF", MODE_PRIVATE);
        usernameEmail = sharedPreferences.getString("username", "");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.save_note:
                saveQuestion();
                return true;
            case R.id.backactivity:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveQuestion(){
        String question = writtenQuestion.getText().toString();

        if(question.trim().isEmpty()){
            Toast.makeText(this, "Please insert question", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference questionRef = FirebaseFirestore.getInstance()
                .collection("QuestionCollection");

        Map<String,Object> hello = new HashMap<>();
        hello.put("question", question);
        hello.put("usernameEmail", usernameEmail);
        hello.put("timestamp", FieldValue.serverTimestamp());
        questionRef.add(hello);
        Toast.makeText(this, "Question Uploaded", Toast.LENGTH_SHORT).show();
        finish();
    }

}