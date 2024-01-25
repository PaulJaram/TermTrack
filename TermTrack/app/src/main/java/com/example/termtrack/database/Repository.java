package com.example.termtrack.database;

import android.app.Application;
import android.content.Context;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.example.termtrack.dao.AssessmentDAO;
import com.example.termtrack.dao.CourseDAO;
import com.example.termtrack.dao.TermDAO;
import com.example.termtrack.entities.Assessment;
import com.example.termtrack.entities.Course;
import com.example.termtrack.entities.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private TermDAO mTermDAO;
    private CourseDAO mCourseDAO;
    private AssessmentDAO mAssessmentDAO;
    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    private List<Course> mAssociatedCourses;
    private List<Assessment> mAllAssessments;
    private List<Assessment> mAssociatedAssessments;

    private static int NUMBER_OF_THREADS = 6;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public Repository(Application application){
        TermTrackDatabaseBuilder db = TermTrackDatabaseBuilder.getDatabase(application);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mAssessmentDAO = db.assessmentDAO();
    }
    public List<Term> getmAllTerms(){
        databaseExecutor.execute(()->{
            mAllTerms = mTermDAO.getAllTerms();
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return mAllTerms;
    }
    public List<Course> getmAllCourses(){
        databaseExecutor.execute(()->{
            mAllCourses = mCourseDAO.getAllCourses();
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return mAllCourses;
    }
    public List<Course> getmAssociatedCourses(int term){
        databaseExecutor.execute(()->{
            mAssociatedCourses = mCourseDAO.getAssociatedCourses(term);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return mAssociatedCourses;
    }
    public List<Assessment> getmAllAssessments(){
        databaseExecutor.execute(()->{
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return mAllAssessments;
    }
    public List<Assessment> getmAssociatedAssessments(int course){
        databaseExecutor.execute(()->{
            mAssociatedAssessments = mAssessmentDAO.getAssociatedAssessments(course);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return mAssociatedAssessments;
    }

    public void insert(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.insert(term);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void update(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.update(term);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.delete(term);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }
    public void insert(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.insert(course);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void update(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.update(course);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.delete(course);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }
    public void insert(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.insert(assessment);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void update(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.update(assessment);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.delete(assessment);
        });
        try{
            Thread.sleep(250);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }
}
