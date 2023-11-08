package com.abonado.academicplanner.database;

import android.app.Application;

import com.abonado.academicplanner.dao.CourseDAO;
import com.abonado.academicplanner.entities.Course;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseRepository {

    private CourseDAO mCourseDAO;
    private List<Course> mAllCourses;
    private Course mCourse;

    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public CourseRepository(Application application){
        AcademicDatabaseBuilder db = AcademicDatabaseBuilder.getDatabase(application);
        mCourseDAO = db.courseDAO();

    }

    public Course getCourse(int courseId){
        databaseExecutor.execute(()->{
            mCourse = mCourseDAO.getCourse(courseId);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mCourse;
    }

    public List<Course>getAllCourses(){
        databaseExecutor.execute(()->{
            mAllCourses = mCourseDAO.getAllCourses();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllCourses;
    }

    public void insert(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.insert(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.update(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.delete(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Course>getAllAsscCourses(int termId){
        databaseExecutor.execute(()->{
            mAllCourses = mCourseDAO.getAsscCourses(termId);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllCourses;
    }


}
