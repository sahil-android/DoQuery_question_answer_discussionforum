package com.example.doquery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private CollectionReference questionRef = FirebaseFirestore.getInstance()
            .collection("QuestionCollection");

    private  SearchAdapter mAdapter;


    ArrayList<Search> mysearchList = new ArrayList<Search>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        final ArrayList<Search> searchList = new ArrayList<Search>();
        searchList.clear();

       questionRef.orderBy("timestamp", Query.Direction.DESCENDING)
               .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

               if(e!=null){
                   return;
               }
               for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                   Search newitem = documentSnapshot.toObject(Search.class);
                   newitem.setIdQuestion(documentSnapshot.getId());
                   searchList.add(newitem);
               }
               for(int i=0; i<searchList.size();i++){
                   System.out.println(searchList.get(i).getIdQuestion());
               }

               mAdapter = new SearchAdapter(searchList);
               mAdapter.notifyDataSetChanged();
               RecyclerView mRecyclerView = findViewById(R.id.recycler_viewSearch);
               mRecyclerView.setHasFixedSize(true);

               mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
               mRecyclerView.setAdapter(mAdapter);

               mAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                   @Override
                   public void onItemClick(int position) {

                       Intent myIntent = new Intent(SearchActivity.this, AnswerActivity.class);
                       myIntent.putExtra("myid", searchList.get(position).getIdQuestion());
                       startActivity(myIntent);
                   }
               });
           }
       });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem serachItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) serachItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                mAdapter.getFilter().filter(s);

                return false;
            }
        });
        return true;
    }
}