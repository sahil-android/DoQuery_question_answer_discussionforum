package com.example.doquery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnswerActivity extends AppCompatActivity {

    private String emailUsername;
    private String id;

    private CollectionReference answerRef;

    private DocumentReference answeRRef;
    private Answer note;

    private CollectionReference answerID;
    private AnswerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        String id1;
        String id2;

        Intent myonlyIntent = getIntent();
        id1 = myonlyIntent.getStringExtra("myid");

        final Intent intent = getIntent();
        id2 = intent.getStringExtra("id");

        if(id1 == null){
            id = id2;
        }else{
            id = id1;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED PREF", MODE_PRIVATE);
        String userID = sharedPreferences.getString("userID", "");

        answerRef = FirebaseFirestore.getInstance().collection("QuestionCollection").document(id).collection("Answers");

        answerID = FirebaseFirestore.getInstance().collection("Users").document(userID).collection("answerID");

        FloatingActionButton buttonAddAnswer = findViewById(R.id.button_add_note1);
        buttonAddAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(AnswerActivity.this,WriteAnswerActivity.class);
                newIntent.putExtra("id", id);
                startActivity(newIntent);
            }
        });

        setUpRecyclerView();


        emailUsername = sharedPreferences.getString("username", "");
    }
    private void setUpRecyclerView() {
        Query query = answerRef.orderBy("like", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Answer> options = new FirestoreRecyclerOptions.Builder<Answer>()
                .setQuery(query, Answer.class)
                .build();
        adapter = new AnswerAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AnswerAdapter.OnitemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Answer note = documentSnapshot.toObject(Answer.class);
                answeRRef = FirebaseFirestore.getInstance().collection("QuestionCollection").
                        document(id).collection("Answers").document(documentSnapshot.getId());

                if(note!=null){
                    String myAnswer = note.getAnswer();
                    int mylike = note.getLike() + 1;
                    checklike(myAnswer, mylike);
                }

            }
        });
    }

    public void checklike(final String myAnswer, final int myLike){
        final int[] m = {0};

        answerID.whereEqualTo("AnswerID", myAnswer)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    String matchID="";
                    DocumentSnapshot myDOc;
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            myDOc = documentSnapshot;
                            matchID = documentSnapshot.getString("AnswerID");
                            Log.i("like", "matching ids" + matchID);

                        }
                        if(matchID == null){
                            matchID = "ldkdl";
                        }
                        if(myAnswer != null){
                            if(!matchID.matches(myAnswer) ){
                                Map<String,Object> dd = new HashMap<>();
                                dd.put("like", myLike);
                                answeRRef.update(dd);
                                Map<String,Object> ques = new HashMap<>();
                                ques.put("AnswerID",myAnswer);
                                answerID.add(ques);
                            }else{
                                Log.i("like", "match found");
                            }
                        }
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