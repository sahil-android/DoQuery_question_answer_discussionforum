package com.example.doquery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userRef = db.collection("Users");
    CollectionReference questionRef = db.collection("QuestionCollection");
    EditText emailaddress;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailaddress = findViewById(R.id.emailaddress);
        password = findViewById(R.id.password);

    }

    public void loggedin(View view){

        final String email = emailaddress.getText().toString();
        String paasword = password.getText().toString();

        if(email.trim().matches("") || paasword.matches(""))
        {
            Toast.makeText(this, "missing field", Toast.LENGTH_SHORT).show();
        }
        else{

            userRef.whereEqualTo("userEmail", email)
                    .whereEqualTo("userPassword", paasword)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            String emailaddress = "";
                            String userID = "";
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                UserDetail savedUser = documentSnapshot.toObject(UserDetail.class);
                                savedUser.setUserId(documentSnapshot.getId());
                                emailaddress = savedUser.getUserEmail();
                                userID = documentSnapshot.getId();
                            }
                            if(emailaddress.matches(email)){

                                savedata(email,userID);

                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                                intent.putExtra("username", email);
                                startActivity(intent);
                            }


                            else
                                Toast.makeText(MainActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("error",e.toString());
                        }
                    });

        }
    }

    public void signedup(View view){

        final String email = emailaddress.getText().toString();
        final String paasword = password.getText().toString();

        if(email.trim().matches("") || paasword.matches(""))
        {
            Toast.makeText(this, "missing field", Toast.LENGTH_SHORT).show();
        }
        else{
            userRef.whereEqualTo("userEmail",email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            String emailaddress = "";
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                            {
                                UserDetail userDetail = documentSnapshot.toObject(UserDetail.class);
                                userDetail.setUserId(documentSnapshot.getId());
                                emailaddress = userDetail.getUserEmail();
                            }
                            if(emailaddress.matches(email))
                                Toast.makeText(MainActivity.this, "User Already Exist!!", Toast.LENGTH_SHORT).show();
                            else{

                                final UserDetail newUser = new UserDetail(email,paasword);
                                userRef.add(newUser);

                                userRef.whereEqualTo("userEmail",email)
                                        .limit(1)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                String userID = "";
                                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                                                {
                                                    userID = documentSnapshot.getId();
                                                }
                                                savedata(email, userID);
                                                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                                                intent.putExtra("username", newUser.getUserEmail());
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("error",e.toString());
                                            }
                                        });

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("error",e.toString());
                        }
                    });
        }
    }

    public void savedata(String email, String userID){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", email);
        editor.putString("userID", userID);
        editor.apply();
    }
}