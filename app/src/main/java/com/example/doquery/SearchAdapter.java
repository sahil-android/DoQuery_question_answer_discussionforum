package com.example.doquery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {

    private ArrayList<Search> mSearchList;
    private ArrayList<Search> mSearchListFull;
    private CollectionReference questionRef = FirebaseFirestore.getInstance()
            .collection("QuestionCollection");

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView textViewQuestion;
        TextView textViewUsername;

        public SearchViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            textViewQuestion = itemView.findViewById(R.id.searchTextView);
            textViewUsername = itemView.findViewById(R.id.usernameSearchTextview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){

                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public SearchAdapter(ArrayList<Search> searchList){

        mSearchList = searchList;
        mSearchListFull = new ArrayList<>(searchList);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_search, parent, false);
        return new SearchViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        Search currentItem = mSearchList.get(position);

        holder.textViewUsername.setText("By- " + currentItem.getUsernameEmail());
        holder.textViewQuestion.setText(currentItem.getQuestion());
    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Search> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mSearchListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Search item : mSearchListFull){
                    if(item.getQuestion().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mSearchList.clear();
            mSearchList.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
