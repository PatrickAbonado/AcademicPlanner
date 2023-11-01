package com.abonado.academicplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.abonado.academicplanner.entities.Instructor;
import com.abonado.academicplanner.entities.Term;

@Dao
public interface InstructorDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Instructor instructor);

    @Update
    void update(Instructor instructor);

    @Delete
    void delete(Instructor instructor);

    @Query("SELECT * FROM instructor WHERE instructorCrsId=:instCrsId ORDER BY instructorId ASC ")
    Term getTerms(int instCrsId);
}
