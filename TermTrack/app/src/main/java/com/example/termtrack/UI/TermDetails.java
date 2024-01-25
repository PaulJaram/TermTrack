package com.example.termtrack.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termtrack.R;
import com.example.termtrack.database.Repository;
import com.example.termtrack.entities.Course;
import com.example.termtrack.entities.Term;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TermDetails extends AppCompatActivity {
    String title;
    String start;
    String end;
    int termID;
    Term currentTerm;
    EditText titleText;
    TextView startText;
    TextView endText;
    Repository repository;
    DatePickerDialog.OnDateSetListener startPicker;
    final Calendar myCalenderStart = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener endPicker;
    final Calendar myCalenderEnd = Calendar.getInstance();
    List<Course> associatedCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        //EditText fields
        titleText = findViewById(R.id.titleText);
        startText = findViewById(R.id.startText);
        endText = findViewById(R.id.endText);
        //get term info from adapter
        termID = getIntent().getIntExtra("ID", -1);
        title = getIntent().getStringExtra("title");
        start = getIntent().getStringExtra("start");
        end = getIntent().getStringExtra("end");
        onResume();
        //set text from term
        titleText.setText(title);
        startText.setText(start);
        //start date picker
        //format date strings
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        startText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //default date
                Date date;
                start = startText.getText().toString();
                if (start.isEmpty()) {
                    start = "01/01/2024";
                }
                try {
                    myCalenderStart.setTime(formatter.parse(start));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(TermDetails.this, startPicker, myCalenderStart.get(Calendar.YEAR), myCalenderStart.get(Calendar.MONTH), myCalenderStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startPicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalenderStart.set(Calendar.YEAR, year);
                myCalenderStart.set(Calendar.MONTH, month);
                myCalenderStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startText.setText(formatter.format(myCalenderStart.getTime()));
            }
        };
        //end Date Picker
        endText.setText(end);
        endText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //default date
                Date date;
                end = endText.getText().toString();
                if (end.isEmpty()) {
                    end = "01/01/2024";
                }
                try {
                    myCalenderEnd.setTime(formatter.parse(end));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(TermDetails.this, endPicker, myCalenderEnd.get(Calendar.YEAR), myCalenderEnd.get(Calendar.MONTH), myCalenderEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endPicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalenderEnd.set(Calendar.YEAR, year);
                myCalenderEnd.set(Calendar.MONTH, month);
                myCalenderEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endText.setText(formatter.format(myCalenderEnd.getTime()));
            }
        };
        //save button
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //format text from fields for date variables
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date startDate = null;
                Date endDate = null;
                //check to see if all fields are filled
                if (titleText.getText().toString().isEmpty() || startText.getText().toString().isEmpty() || endText.getText().toString().isEmpty()) {
                    Toast errorToast = new Toast(TermDetails.this);
                    errorToast.setText("Please Fill Out Missing Fields");
                    errorToast.show();
                } else {
                    //save term to database
                    repository = new Repository(getApplication());
                    try {
                        startDate = formatter.parse(startText.getText().toString());
                        endDate = formatter.parse(endText.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    //if brand new term
                    if (termID == -1) {
                        //if first ever term: id = 1
                        if (repository.getmAllTerms().size() == 0) {
                            termID = 1;
                        } else {
                            //if not first term: id = last term id + 1
                            termID = repository.getmAllTerms().get(repository.getmAllTerms().size() - 1).getID() + 1;
                        }
                        //add new term to the database
                        currentTerm = new Term(termID, titleText.getText().toString(), startDate, endDate);
                        repository.insert(currentTerm);
                        Toast savedToast = new Toast(TermDetails.this);
                        savedToast.setText("Term was Saved");
                        savedToast.show();
                    } else {
                        //if existing term update database
                        currentTerm = new Term(termID, titleText.getText().toString(), startDate, endDate);
                        repository.update(currentTerm);
                        Toast savedToast = new Toast(TermDetails.this);
                        savedToast.setText("Term was Saved");
                        savedToast.show();
                    }
                    onResume();
                }
            }
        });
        //delete button
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termID == -1) {
                    //if term was not saved
                    Toast deleteToast = new Toast(TermDetails.this);
                    deleteToast.setText("Term was Not Created");
                    deleteToast.show();
                    finish();
                } else if (associatedCourses.size() != 0) {
                    //if term has associated courses
                    Toast coursesToast = new Toast(TermDetails.this);
                    coursesToast.setText("Cannot Delete Term With Associated Courses");
                    coursesToast.show();
                } else {
                    //delete term
                    repository.delete(currentTerm);
                    Toast deleteToast = new Toast(TermDetails.this);
                    deleteToast.setText(currentTerm.getTitle() + " was Deleted");
                    deleteToast.show();
                    Intent intent = new Intent(TermDetails.this, Terms.class);
                    startActivity(intent);
                }
            }
        });
        //add associated course button
        ImageButton imageButton = findViewById(R.id.addCourses);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if term is saved in database
                if (termID != -1) {
                    Intent intent = new Intent(TermDetails.this, CourseDetails.class);
                    intent.putExtra("note", "null");
                    intent.putExtra("termID", termID);
                    startActivity(intent);
                } else {
                    //if term is not saved in the database
                    Toast toast = new Toast(TermDetails.this);
                    toast.setText("Please Save Term Before Adding Course");
                    toast.show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if term exists
        if (termID != -1) {
            //get term information
            repository = new Repository(getApplication());
            String pattern = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
            for (Term t : repository.getmAllTerms()) {
                if (t.getID() == termID) {
                    title = t.getTitle();
                    start = simpleDateFormat.format(t.getStart());
                    end = simpleDateFormat.format(t.getEnd());
                    currentTerm = t;
                    break;
                }
            }
            //get associated courses
            RecyclerView courseRecyclerView = findViewById(R.id.courseRecyclerView);
            CourseAdapter courseAdapter = new CourseAdapter(this);
            courseRecyclerView.setAdapter(courseAdapter);
            courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            associatedCourses = repository.getmAssociatedCourses(termID);
            courseAdapter.setCourses(associatedCourses);
        }
    }
}