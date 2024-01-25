package com.example.termtrack.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.termtrack.entities.Course;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses ORDER BY ID ASC;")
    List<Course> getAllCourses();

    @Query("SELECT * FROM courses WHERE termID=:term ORDER BY ID ASC;")
    List<Course> getAssociatedCourses(int term);
}
