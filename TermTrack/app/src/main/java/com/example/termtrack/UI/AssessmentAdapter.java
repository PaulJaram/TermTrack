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
import com.example.termtrack.entities.Assessment;
import com.example.termtrack.entities.enums.TypeAssessment;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
    private final LayoutInflater mInflater;
    private List<Assessment> mAssessments;
    private final Context context;

    public AssessmentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView1;
        private final TextView assessmentItemView2;
        private final TextView assessmentItemView2Date;
        private final TextView assessmentItemView3;
        private final TextView assessmentItemView3Date;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentItemView1 = itemView.findViewById(R.id.textView4);
            assessmentItemView2 = itemView.findViewById(R.id.textView9);
            assessmentItemView2Date = itemView.findViewById(R.id.textView9Date);
            assessmentItemView3 = itemView.findViewById(R.id.textView10);
            assessmentItemView3Date = itemView.findViewById(R.id.textView10Date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Assessment currentAssessment = mAssessments.get(position);
                    String pattern = "MM/dd/yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
                    Intent intent = new Intent(context, AssessmentDetails.class);
                    intent.putExtra("ID", currentAssessment.getID());
                    intent.putExtra("title", currentAssessment.getTitle());
                    intent.putExtra("type", TypeAssessment.toString(currentAssessment.getType()));
                    intent.putExtra("start", simpleDateFormat.format(currentAssessment.getStart()));
                    intent.putExtra("end", simpleDateFormat.format(currentAssessment.getEnd()));
                    intent.putExtra("courseID", currentAssessment.getCourseID());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        if (mAssessments != null) {
            Assessment currentAssessment = mAssessments.get(position);
            String title = currentAssessment.getTitle();
            Date start = currentAssessment.getStart();
            Date end = currentAssessment.getEnd();
            String pattern = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
            String startString = "Start: ";
            String startStringDate = simpleDateFormat.format(start);
            String endString = "End: ";
            String endStringDate = simpleDateFormat.format(end);
            holder.assessmentItemView1.setText(title);
            holder.assessmentItemView2.setText(startString);
            holder.assessmentItemView2Date.setText(startStringDate);
            holder.assessmentItemView3.setText(endString);
            holder.assessmentItemView3Date.setText(endStringDate);
        } else {
            String noAssessments = "No Assessments";
            holder.assessmentItemView1.setText(noAssessments);
        }
    }

    @Override
    public int getItemCount() {
        if (mAssessments != null) {
            return mAssessments.size();
        } else {
            return 0;
        }
    }

    public void setAssessments(List<Assessment> assessments) {
        mAssessments = assessments;
        notifyDataSetChanged();
    }
}
