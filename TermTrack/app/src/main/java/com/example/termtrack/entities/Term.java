package com.example.termtrack.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName="terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String title;
    private Date start;
    private Date end;

    public Term(int ID, String title, Date start, Date end) {
        this.ID = ID;
        this.title = title;
        this.start = start;
        this.end = end;
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
}
