package com.example.doquery;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    private CollectionReference questionRef = FirebaseFirestore.getInstance()
            .collection("QuestionCollection");
    private String emailUsername;

    private QuestionAdapter adapter;

    private ArrayList<Search> searchList = new ArrayList<Search>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        final Intent intent = getIntent();
        emailUsername = intent.getStringExtra("username");



        FloatingActionButton buttonAddQuestion = findViewById(R.id.button_add_note);
        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(QuestionActivity.this, WriteQuestionActivity.class);
                newIntent.putExtra("username", intent.getStringExtra("username"));
                startActivity(newIntent);
            }
        });

        FloatingActionButton buttonsearchQuestion = findViewById(R.id.button_search);
        buttonsearchQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serachIntent = new Intent(QuestionActivity.this, SearchActivity.class);
                startActivity(serachIntent);
            }
        });

        setUpRecyclerView();

        SharedPreferences sharedPreferences = getSharedPreferences("SHARED PREF", MODE_PRIVATE);

        emailUsername = sharedPreferences.getString("username", "");
    }

    private void setUpRecyclerView(){
        Query query = questionRef.orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Question> options = new FirestoreRecyclerOptions.Builder<Question>()
                .setQuery(query, Question.class)
                .build();
        adapter = new QuestionAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new QuestionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                SharedPreferences sharedPreferences = getSharedPreferences("SHARED PREF", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id",id);
                editor.apply();
                Intent answerIntent = new Intent(QuestionActivity.this, AnswerActivity.class);
                answerIntent.putExtra("id", id);
                startActivity(answerIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}