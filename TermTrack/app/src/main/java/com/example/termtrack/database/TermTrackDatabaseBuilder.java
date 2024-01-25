package com.example.termtrack.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.termtrack.dao.AssessmentDAO;
import com.example.termtrack.dao.CourseDAO;
import com.example.termtrack.dao.TermDAO;
import com.example.termtrack.entities.Assessment;
import com.example.termtrack.entities.Course;
import com.example.termtrack.entities.Term;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class TermTrackDatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
    private static volatile TermTrackDatabaseBuilder INSTANCE;
    static TermTrackDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized(TermTrackDatabaseBuilder.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TermTrackDatabaseBuilder.class, "Database.db")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
