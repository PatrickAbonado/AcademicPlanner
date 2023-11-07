package com.abonado.academicplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.abonado.academicplanner.dao.AssessmentDAO;
import com.abonado.academicplanner.dao.CourseDAO;
import com.abonado.academicplanner.dao.TermDAO;
import com.abonado.academicplanner.entities.Assessment;
import com.abonado.academicplanner.entities.Course;
import com.abonado.academicplanner.entities.Term;

@Database(entities = {Assessment.class, Course.class, Term.class}, version = 4, exportSchema = false)
public abstract class AcademicDatabaseBuilder extends RoomDatabase {

    public abstract AssessmentDAO assessmentDAO();
    public abstract CourseDAO courseDAO();
    public abstract TermDAO termDAO();

    private static volatile AcademicDatabaseBuilder INSTANCE;

    static AcademicDatabaseBuilder getDatabase(final Context context){
        if (INSTANCE==null){
            synchronized (AcademicDatabaseBuilder.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),AcademicDatabaseBuilder.class, "AcademicPlanner.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
