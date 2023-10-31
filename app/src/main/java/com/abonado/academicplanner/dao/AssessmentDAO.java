package com.abonado.academicplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.abonado.academicplanner.entities.Assessment;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM assessment ORDER BY assessmentId ASC")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessment WHERE assessmentId=:asmntId ORDER BY assessmentId ASC ")
    Assessment getAssessment(int asmntId);


}
