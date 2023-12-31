package com.abonado.academicplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.abonado.academicplanner.entities.Course;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM course WHERE courseId=:crsId ORDER BY courseId ASC ")
    Course getCourse(int crsId);

    @Query("SELECT * FROM course ORDER BY courseId ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM course WHERE courseTermId=:termId ORDER BY courseId ASC ")
    List<Course> getTermAsscCourses(int termId);





}
