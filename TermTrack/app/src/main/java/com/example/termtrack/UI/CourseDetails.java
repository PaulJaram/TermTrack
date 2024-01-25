package com.example.termtrack.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termtrack.R;
import com.example.termtrack.database.Repository;
import com.example.termtrack.entities.Assessment;
import com.example.termtrack.entities.Course;
import com.example.termtrack.entities.enums.CourseStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CourseDetails extends AppCompatActivity {
    int courseID;
    String title;
    String start;
    String end;
    String status;
    String name;
    String phone;
    String email;
    String note;
    int termID;
    EditText titleText;
    TextView startText;
    TextView endText;
    RadioButton progressRadio;
    RadioButton completedRadio;
    RadioButton droppedRadio;
    RadioButton planRadio;
    RadioGroup statusRadioGroup;
    EditText nameText;
    EditText phoneText;
    EditText emailText;
    EditText noteText;
    Repository repository;
    DatePickerDialog.OnDateSetListener startPicker;
    final Calendar myCalenderStart = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener endPicker;
    final Calendar myCalenderEnd = Calendar.getInstance();

    Course currentCourse;
    List<Assessment> associatedAssessments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        //EditText fields
        titleText = findViewById(R.id.titleText);
        startText = findViewById(R.id.startText);
        endText = findViewById(R.id.endText);
        statusRadioGroup = findViewById(R.id.statusRadioGroup);
        progressRadio = findViewById(R.id.progressRadio);
        completedRadio = findViewById(R.id.completedRadio);
        droppedRadio = findViewById(R.id.droppedRadio);
        planRadio = findViewById(R.id.planRadio);
        nameText = findViewById(R.id.nameText);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        noteText = findViewById(R.id.noteText);
        //get course information from Adapter
        courseID = getIntent().getIntExtra("ID", -1);
        title = getIntent().getStringExtra("title");
        start = getIntent().getStringExtra("start");
        end = getIntent().getStringExtra("end");
        status = getIntent().getStringExtra("status");
        name = getIntent().getStringExtra("instructorName");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        note = getIntent().getStringExtra("note");
        termID = getIntent().getIntExtra("termID", -1);
        onResume();
        //set Text fields from Course info
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
                new DatePickerDialog(CourseDetails.this, startPicker, myCalenderStart.get(Calendar.YEAR), myCalenderStart.get(Calendar.MONTH), myCalenderStart.get(Calendar.DAY_OF_MONTH)).show();
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
                new DatePickerDialog(CourseDetails.this, endPicker, myCalenderEnd.get(Calendar.YEAR), myCalenderEnd.get(Calendar.MONTH), myCalenderEnd.get(Calendar.DAY_OF_MONTH)).show();
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
        nameText.setText(name);
        phoneText.setText(phone);
        emailText.setText(email);
        //note field is optional
        if (!note.equals("null")) {
            noteText.setText(note);
        }
        if (courseID == -1) {
            //default
            status = "Plan To Take";
        }
        switch (status) {
            case "In Progress":
                statusRadioGroup.check(R.id.progressRadio);
                progressRadio.setChecked(true);
                break;
            case "Completed":
                statusRadioGroup.check(R.id.completedRadio);
                completedRadio.setChecked(true);
                break;
            case "Dropped":
                statusRadioGroup.check(R.id.droppedRadio);
                droppedRadio.setChecked(true);
                break;
            default:
                statusRadioGroup.check(R.id.planRadio);
                planRadio.setChecked(true);
                break;
        }
        //save button
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if there is missing fields
                if (titleText.getText().toString().isEmpty() || startText.getText().toString().isEmpty() || endText.getText().toString().isEmpty()
                        || nameText.getText().toString().isEmpty() || phoneText.getText().toString().isEmpty() || emailText.getText().toString().isEmpty()) {
                    Toast errorToast = new Toast(CourseDetails.this);
                    errorToast.setText("Please Fill Out Missing Fields");
                    errorToast.show();
                } else {
                    //save Course
                    repository = new Repository(getApplication());
                    title = titleText.getText().toString();
                    name = nameText.getText().toString();
                    phone = phoneText.getText().toString();
                    email = emailText.getText().toString();
                    //note is optional
                    note = noteText.getText().toString();
                    if (noteText.getText().toString().isEmpty()) {
                        note = "null";
                    }
                    //format date strings
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    Date startDate = null;
                    Date endDate = null;
                    start = startText.getText().toString();
                    end = endText.getText().toString();
                    try {
                        startDate = formatter.parse(start);
                        endDate = formatter.parse(end);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    CourseStatus courseStatus = null;
                    //check course status radio group
                    if (progressRadio.isChecked()) {
                        courseStatus = CourseStatus.IN_PROGRESS;
                        status = CourseStatus.toString(CourseStatus.IN_PROGRESS);
                    } else if (completedRadio.isChecked()) {
                        courseStatus = CourseStatus.COMPLETED;
                        status = CourseStatus.toString(CourseStatus.COMPLETED);

                    } else if (droppedRadio.isChecked()) {
                        courseStatus = CourseStatus.DROPPED;
                        status = CourseStatus.toString(CourseStatus.DROPPED);

                    } else if (planRadio.isChecked()) {
                        courseStatus = CourseStatus.PLAN_TO_TAKE;
                        status = CourseStatus.toString(CourseStatus.PLAN_TO_TAKE);

                    }
                    //if brand new course
                    if (courseID == -1) {
                        //if first ever course: id = 1
                        if (repository.getmAllCourses().size() == 0) {
                            courseID = 1;
                        } else {
                            //if not first course: id = last id + 1
                            courseID = repository.getmAllCourses().get(repository.getmAllCourses().size() - 1).getID() + 1;
                        }
                        Course newCourse = new Course(courseID, title, startDate, endDate, courseStatus, name, phone, email, note, termID);
                        repository.insert(newCourse);
                        Toast savedToast = new Toast(CourseDetails.this);
                        savedToast.setText("Course was Saved");
                        savedToast.show();
                    } else {
                        //if existing course update database
                        Course course = new Course(courseID, title, startDate, endDate, courseStatus, name, phone, email, note, termID);
                        repository.update(course);
                        Toast savedToast = new Toast(CourseDetails.this);
                        savedToast.setText("Course was Saved");
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
                if (courseID == -1) {
                    //if course was not saved
                    Toast deleteToast = new Toast(CourseDetails.this);
                    deleteToast.setText("Course was Not Created");
                    deleteToast.show();
                    finish();
                } else {
                    //delete Course
                    for (Assessment a : repository.getmAssociatedAssessments(courseID)) {
                        repository.delete(a);
                    }
                    repository.delete(currentCourse);
                    Toast deleteToast = new Toast(CourseDetails.this);
                    deleteToast.setText(currentCourse.getTitle() + " and Assessments were Deleted");
                    deleteToast.show();
                    Intent intent = new Intent(CourseDetails.this, TermDetails.class);
                    intent.putExtra("ID", termID);
                    startActivity(intent);
                }
            }
        });
        //add associated assessment button
        ImageButton imageButton = findViewById(R.id.addAssessment);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if course exists
                if (courseID != -1) {
                    Intent intent = new Intent(CourseDetails.this, AssessmentDetails.class);
                    intent.putExtra("courseID", courseID);
                    startActivity(intent);
                } else {
                    //if course is not saved and does not exist
                    Toast toast = new Toast(CourseDetails.this);
                    toast.setText("Please Save Course Before Adding Assessment");
                    toast.show();
                }
            }
        });
    }

    //options menu with notify and share note option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu item to share note
        if (item.getItemId() == R.id.shareNote) {
            if (courseID == -1) {
                //if course is not saved and does not exist
                Toast toast = new Toast(CourseDetails.this);
                toast.setText("Please Save Course Before Sharing Note");
                toast.show();
            } else {
                Intent sentIntent = new Intent();
                sentIntent.setAction(Intent.ACTION_SEND);
                sentIntent.putExtra(Intent.EXTRA_TEXT, noteText.getText().toString());
                sentIntent.putExtra(Intent.EXTRA_TITLE, titleText.getText().toString());
                sentIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sentIntent, null);
                startActivity(shareIntent);
                return true;
            }
        }//menu item to set up notification for course start
        if (item.getItemId() == R.id.notifyStart) {
            if (courseID == -1) {
                //if course is not saved and does not exist
                Toast toast = new Toast(CourseDetails.this);
                toast.setText("Please Save Course Before Setting Up Notification");
                toast.show();
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date startDate = null;
                try {
                    startDate = formatter.parse(startText.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Long trigger = startDate.getTime();
                Toast.makeText(CourseDetails.this, "Course Start Notification Set for " + startText.getText().toString(), Toast.LENGTH_LONG).show();
                String messageStart = "Course: " + titleText.getText().toString() + ", is starting today";
                Intent intent = new Intent(CourseDetails.this, MyCourseDetailsReceiver.class);
                intent.putExtra("key", messageStart);
                Random rand = new Random();
                int intentNumber = rand.nextInt(1000);
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, intentNumber, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
            }
        }//menu item to set up notification for course end
        if (item.getItemId() == R.id.notifyEnd) {
            if (courseID == -1) {
                //if course is not saved and does not exist
                Toast toast = new Toast(CourseDetails.this);
                toast.setText("Please Save Course Before Setting Up Notification");
                toast.show();
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date endDate = null;
                try {
                    endDate = formatter.parse(endText.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Long trigger = endDate.getTime();
                Toast.makeText(CourseDetails.this, "Course End Notification Set for " + endText.getText().toString(), Toast.LENGTH_LONG).show();
                String messageEnd = "Course: " + titleText.getText().toString() + ", is ending today";
                Intent intent = new Intent(CourseDetails.this, MyCourseDetailsReceiver.class);
                intent.putExtra("key", messageEnd);
                Random rand = new Random();
                int intentNumber = rand.nextInt(1000);
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, intentNumber, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
            }
        }//Back button
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(CourseDetails.this, TermDetails.class);
            intent.putExtra("ID", termID);
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if course exists
        if (courseID != -1) {
            //get course information
            repository = new Repository(getApplication());
            String pattern = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
            for (Course c : repository.getmAllCourses()) {
                if (c.getID() == courseID) {
                    termID = c.getTermID();
                    title = c.getTitle();
                    start = simpleDateFormat.format(c.getStart());
                    end = simpleDateFormat.format(c.getEnd());
                    name = c.getInstructorName();
                    phone = c.getPhone();
                    email = c.getEmail();
                    note = c.getNote();
                    status = CourseStatus.toString(c.getStatus());
                    currentCourse = c;
                    break;
                }
            }
            //get associated assessments
            RecyclerView assessmentRecyclerView = findViewById(R.id.assessmentRecyclerView);
            AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
            assessmentRecyclerView.setAdapter(assessmentAdapter);
            assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            associatedAssessments = repository.getmAssociatedAssessments(courseID);
            assessmentAdapter.setAssessments(associatedAssessments);
        }
    }
}