package com.example.termtrack.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.termtrack.entities.enums.TypeAssessment;

import java.util.Date;

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String title;
    private TypeAssessment type;
    private Date start;
    private Date end;
    private int courseID;

    public Assessment(int ID, String title, TypeAssessment type, Date start, Date end, int courseID) {
        this.ID = ID;
        this.title = title;
        this.type = type;
        this.start = start;
        this.end = end;
        this.courseID = courseID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TypeAssessment getType() {
        return type;
    }

    public void setType(TypeAssessment type) {
        this.type = type;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}
