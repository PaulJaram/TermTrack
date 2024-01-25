package com.example.termtrack.UI;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtrack.R;
import com.example.termtrack.entities.Term;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder>{
    private final LayoutInflater mInflater;
    private List<Term> mTerms;
    private final Context context;

    public TermAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termItemView1;
        private final TextView termItemView2;
        private final TextView termItemView2Date;
        private final TextView termItemView3;
        private final TextView termItemView3Date;
        public TermViewHolder(@NonNull View itemView) {
            super(itemView);
            termItemView1 = itemView.findViewById(R.id.textView2);
            termItemView2 = itemView.findViewById(R.id.textView5);
            termItemView2Date = itemView.findViewById(R.id.textView5date);
            termItemView3 = itemView.findViewById(R.id.textView6);
            termItemView3Date = itemView.findViewById(R.id.textView6date);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Term currentTerm = mTerms.get(position);
                    String pattern = "MM/dd/yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
                    Intent intent= new Intent(context, TermDetails.class);
                    intent.putExtra("ID", currentTerm.getID());
                    intent.putExtra("title", currentTerm.getTitle());
                    intent.putExtra("start", simpleDateFormat.format(currentTerm.getStart()));
                    intent.putExtra("end", simpleDateFormat.format(currentTerm.getEnd()));
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {
        if(mTerms!=null){
            Term currentTerm = mTerms.get(position);
            String title = currentTerm.getTitle();
            Date start = currentTerm.getStart();
            Date end = currentTerm.getEnd();
            String pattern = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
            String startString = "Start: ";
            String startStringDate = simpleDateFormat.format(start);
            String endString =  "End: ";
            String endStringDate =  simpleDateFormat.format(end);
            holder.termItemView1.setText(title);
            holder.termItemView2.setText(startString);
            holder.termItemView2Date.setText(startStringDate);
            holder.termItemView3.setText(endString);
            holder.termItemView3Date.setText(endStringDate);
        }
        else{
            String noTerms = "No Terms";
            holder.termItemView1.setText(noTerms);
        }
    }

    @Override
    public int getItemCount() {
        if(mTerms!=null) {
            return mTerms.size();
        }
        else{return 0;}
    }
    public void setTerms(List<Term> terms){
        mTerms = terms;
        notifyDataSetChanged();
    }
}
