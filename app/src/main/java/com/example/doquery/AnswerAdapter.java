package com.example.doquery;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;
import java.util.Locale;

public class AnswerAdapter extends FirestoreRecyclerAdapter<Answer, AnswerAdapter.AnswerHolder> {

    private OnitemClickListener listener;

    public AnswerAdapter(@NonNull FirestoreRecyclerOptions<Answer> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnswerHolder holder, int position, @NonNull Answer model) {

        holder.textViewAnswer.setText(model.getAnswer());
        holder.textViewUsername.setText( "By- " + model.getUsernameEmail());
        holder.likeText.setText(Integer.toString(model.getLike()));
        String time = getDate(model.getTimestamp());
        if(time!=null){
            holder.textViewTime.setText(time);
        }

    }

    @NonNull
    @Override
    public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_answer, parent, false);
        return new AnswerHolder(v);
    }

    class AnswerHolder extends RecyclerView.ViewHolder {

        TextView textViewAnswer;
        TextView textViewUsername;
        TextView textViewTime;
        ImageButton likeImage;
        TextView likeText;

        public AnswerHolder(@NonNull View itemView) {
            super(itemView);
            textViewAnswer = itemView.findViewById(R.id.answerTextView);
            textViewUsername = itemView.findViewById(R.id.userNameTextview);
            textViewTime = itemView.findViewById(R.id.timeAnswerTextView);
            likeImage = itemView.findViewById(R.id.like_image);
            likeText = itemView.findViewById(R.id.like_text);

            likeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener!= null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }

    }

    public interface OnitemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener (OnitemClickListener listener) {

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
