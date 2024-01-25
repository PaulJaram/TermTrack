package com.example.termtrack.UI;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termtrack.R;
import com.example.termtrack.database.Repository;
import com.example.termtrack.entities.Assessment;
import com.example.termtrack.entities.Term;
import com.example.termtrack.entities.enums.CourseStatus;
import com.example.termtrack.entities.enums.TypeAssessment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AssessmentDetails extends AppCompatActivity {
    Repository repository;
    int assessmentID;
    String title;
    String type;
    String start;
    String end;
    int courseID;
    EditText titleText;
    TextView startText;
    TextView endText;
    RadioGroup typeRadioGroup;
    RadioButton oaRadio;
    RadioButton paRadio;
    DatePickerDialog.OnDateSetListener startPicker;
    final Calendar myCalenderStart = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener endPicker;
    final Calendar myCalenderEnd = Calendar.getInstance();
    Assessment currentAssessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        //get info from adapter
        assessmentID = getIntent().getIntExtra("ID", -1);
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        start = getIntent().getStringExtra("start");
        end = getIntent().getStringExtra("end");
        courseID = getIntent().getIntExtra("courseID", -1);
        repository = new Repository(getApplication());
        //EditText fields
        titleText = findViewById(R.id.titleText);
        startText = findViewById(R.id.startText);
        endText = findViewById(R.id.endText);
        typeRadioGroup = findViewById(R.id.typeRadioGroup);
        oaRadio = findViewById(R.id.oaRadio);
        paRadio = findViewById(R.id.paRadio);
        //find current assessment
        if (assessmentID != -1) {
            for (Assessment a : repository.getmAssociatedAssessments(courseID)) {
                if (a.getID() == assessmentID) {
                    currentAssessment = a;
                    break;
                }
            }
        }
        //check correct radio
        if (assessmentID == -1) {
            type = "Objective Assessment";
        }
        if (type.equals("Performance Assessment")) {
            typeRadioGroup.check(R.id.paRadio);
            paRadio.setChecked(true);
        } else {
            typeRadioGroup.check(R.id.oaRadio);
            oaRadio.setChecked(true);
        }
        //set edit text
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
                new DatePickerDialog(AssessmentDetails.this, startPicker, myCalenderStart.get(Calendar.YEAR), myCalenderStart.get(Calendar.MONTH), myCalenderStart.get(Calendar.DAY_OF_MONTH)).show();
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
                new DatePickerDialog(AssessmentDetails.this, endPicker, myCalenderEnd.get(Calendar.YEAR), myCalenderEnd.get(Calendar.MONTH), myCalenderEnd.get(Calendar.DAY_OF_MONTH)).show();
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
                    Toast errorToast = new Toast(AssessmentDetails.this);
                    errorToast.setText("Please Fill Out Missing Fields");
                    errorToast.show();
                } else {
                    //save Assessment to database
                    repository = new Repository(getApplication());
                    try {
                        startDate = formatter.parse(startText.getText().toString());
                        endDate = formatter.parse(endText.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    TypeAssessment type = null;
                    //check typeAssessment radio group
                    if (paRadio.isChecked()) {
                        type = TypeAssessment.PERFORMANCE;
                    } else if (oaRadio.isChecked()) {
                        type = TypeAssessment.OBJECTIVE;
                    }
                    //if brand new assessment
                    if (assessmentID == -1) {
                        //if first ever assessment: id = 1
                        if (repository.getmAllAssessments().size() == 0) {
                            assessmentID = 1;
                        } else {
                            //if not first assessment: id = last term id + 1
                            assessmentID = repository.getmAllTerms().get(repository.getmAllTerms().size() - 1).getID() + 1;
                        }
                        //add new assessment to the database
                        currentAssessment = new Assessment(assessmentID, titleText.getText().toString(), type, startDate, endDate, courseID);
                        repository.insert(currentAssessment);
                        Toast savedToast = new Toast(AssessmentDetails.this);
                        savedToast.setText("Assessment was Saved");
                        savedToast.show();
                    } else {
                        //if existing assessment update database
                        currentAssessment = new Assessment(assessmentID, titleText.getText().toString(), type, startDate, endDate, courseID);
                        repository.update(currentAssessment);
                        Toast savedToast = new Toast(AssessmentDetails.this);
                        savedToast.setText("Assessment was Saved");
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
                if (assessmentID == -1) {
                    //if assessment was not saved
                    Toast deleteToast = new Toast(AssessmentDetails.this);
                    deleteToast.setText("Assessment was Not Created");
                    deleteToast.show();
                    finish();
                } else {
                    //delete assessment
                    repository.delete(currentAssessment);
                    Toast deleteToast = new Toast(AssessmentDetails.this);
                    deleteToast.setText(currentAssessment.getTitle()+ " was Deleted");
                    deleteToast.show();
                    Intent intent = new Intent(AssessmentDetails.this, CourseDetails.class);
                    intent.putExtra("ID", courseID);
                    startActivity(intent);
                }
            }
        });
    }

    //menu with notify option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item to set up notification for assessment start
        if (item.getItemId() == R.id.notifyStart) {
            if (courseID == -1) {
                //if course is not saved and does not exist
                Toast toast = new Toast(AssessmentDetails.this);
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
                Toast.makeText(AssessmentDetails.this, "Assessment Start Notification Set for " + startText.getText().toString(), Toast.LENGTH_LONG).show();
                String messageStart = "Assessment: " + titleText.getText().toString() + ", is starting today";
                Intent intent = new Intent(AssessmentDetails.this, MyAssessmentDetailsReceiver.class);
                intent.putExtra("key", messageStart);
                Random rand = new Random();
                int intentNumber = rand.nextInt(1000);
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, intentNumber, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
            }
        }
        //menu item to set up notification for assessment end
        if (item.getItemId() == R.id.notifyEnd) {
            if (courseID == -1) {
                //if course is not saved and does not exist
                Toast toast = new Toast(AssessmentDetails.this);
                toast.setText("Please Save Assessment Before Setting Up Notification");
                toast.show();
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date endDate = null;
                try {
                    endDate = formatter.parse(startText.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Long trigger = endDate.getTime();
                Toast.makeText(AssessmentDetails.this, "Assessment End Notification Set for " + startText.getText().toString(), Toast.LENGTH_LONG).show();
                String messageEnd = "Assessment: " + titleText.getText().toString() + ", is ending today";
                Intent intent = new Intent(AssessmentDetails.this, MyAssessmentDetailsReceiver.class);
                intent.putExtra("key", messageEnd);
                Random rand = new Random();
                int intentNumber = rand.nextInt(1000);
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, intentNumber, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
            }
        }//Back button
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(AssessmentDetails.this, CourseDetails.class);
            intent.putExtra("ID", courseID);
            startActivity(intent);
            return true;
        }
        return true;
    }
}