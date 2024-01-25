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
import com.example.termtrack.entities.Course;
import com.example.termtrack.entities.enums.CourseStatus;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>{
    private final LayoutInflater mInflater;
    private List<Course> mCourses;
    private final Context context;

    public CourseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView1;
        private final TextView courseItemView2;
        private final TextView courseItemView2Date;
        private final TextView courseItemView3;
        private final TextView courseItemView3Date;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseItemView1 = itemView.findViewById(R.id.textView3);
            courseItemView2 = itemView.findViewById(R.id.textView7);
            courseItemView2Date = itemView.findViewById(R.id.textView7Date);
            courseItemView3 = itemView.findViewById(R.id.textView8);
            courseItemView3Date = itemView.findViewById(R.id.textView8Date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Course currentCourse = mCourses.get(position);
                    String pattern = "MM/dd/yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
                    Intent intent= new Intent(context, CourseDetails.class);
                    intent.putExtra("ID", currentCourse.getID());
                    intent.putExtra("title", currentCourse.getTitle());
                    intent.putExtra("start", simpleDateFormat.format(currentCourse.getStart()));
                    intent.putExtra("end", simpleDateFormat.format(currentCourse.getEnd()));
                    intent.putExtra("status", CourseStatus.toString(currentCourse.getStatus()));
                    intent.putExtra("instructorName", currentCourse.getInstructorName());
                    intent.putExtra("phone", currentCourse.getPhone());
                    intent.putExtra("email", currentCourse.getEmail());
                    intent.putExtra("note", currentCourse.getNote());
                    intent.putExtra("termID", currentCourse.getTermID());
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.course_list_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if(mCourses!=null){
            Course currentCourse = mCourses.get(position);
            String title = currentCourse.getTitle();
            Date start = currentCourse.getStart();
            Date end = currentCourse.getEnd();
            String pattern = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
            String startString = "Start: ";
            String startStringDate = simpleDateFormat.format(start);
            String endString =  "End: ";
            String endStringDate = simpleDateFormat.format(end);
            holder.courseItemView1.setText(title);
            holder.courseItemView2.setText(startString);
            holder.courseItemView2Date.setText(startStringDate);
            holder.courseItemView3.setText(endString);
            holder.courseItemView3Date.setText(endStringDate);
        }
        else{
            String noCourses = "No Courses";
            holder.courseItemView1.setText(noCourses);
        }
    }

    @Override
    public int getItemCount() {
        if(mCourses!=null) {
            return mCourses.size();
        }
        else{return 0;}
    }
    public void setCourses(List<Course> courses){
        mCourses = courses;
        notifyDataSetChanged();
    }
}
