package com.example.doquery;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;
import java.util.Locale;

public class QuestionAdapter extends FirestoreRecyclerAdapter<Question, QuestionAdapter.QuestionHolder> {

    private OnItemClickListener listener;

    public QuestionAdapter(@NonNull FirestoreRecyclerOptions<Question> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionHolder holder, int position, @NonNull Question model) {

        holder.textViewUsername.setText("By- " + model.getUsernameEmail());
        holder.textViewQuestion.setText(model.getQuestion());
       String time = getDate(model.getTimestamp());
        if(time!=null){
            holder.textViewTime.setText(time);
        }
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_question, parent, false);
        return new QuestionHolder(v);
    }

    class QuestionHolder extends RecyclerView.ViewHolder{

        TextView textViewQuestion;
        TextView textViewUsername;
        TextView textViewTime;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);

            textViewQuestion = itemView.findViewById(R.id.questionTextView);
            textViewUsername = itemView.findViewById(R.id.usernameTextview);
            textViewTime = itemView.findViewById(R.id.timeTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){

                        listener.onItemClick(getSnapshots().getSnapshot(position), position);

                    }
                }
            });

        }
    }

    public interface OnItemClickListener {

        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public  String getDate(Timestamp infotime){
        if(infotime != null) {
            String mytime = infotime.toString();
            String seconds = mytime.substring(mytime.indexOf("=") + 1, mytime.indexOf(","));
            long time = Long.parseLong(seconds);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(time * 1000);
            return DateFormat.format("dd-MM-yyyy  h:mm a", cal).toString();

        }
        return  null;
    }

}
