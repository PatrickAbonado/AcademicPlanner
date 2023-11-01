package com.abonado.academicplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.abonado.academicplanner.entities.Assessment;
import com.abonado.academicplanner.entities.Course;
import com.abonado.academicplanner.entities.Term;

import java.util.List;

@Dao
public interface TermDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM term WHERE termId=:trmId ORDER BY termId ASC ")
    Term getTerm(int trmId);

    @Query("SELECT * FROM term ORDER BY termId ASC")
    List<Term> getAllTerms();

    @Query("SELECT * FROM course WHERE courseTermId = :crsTrmId ORDER BY courseId ASC ")
    List<Course> getTermCourses(int crsTrmId);

}
