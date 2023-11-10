package com.abonado.academicplanner.database;

import android.app.Application;

import com.abonado.academicplanner.dao.AssessmentDAO;
import com.abonado.academicplanner.entities.Assessment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AssessmentRepository {
    private AssessmentDAO mAssessmentDAO;
    private List<Assessment> mAllAssessments;
    private Assessment mAssessment;
    private List<Assessment> mAllAsscAsmnts;

    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public AssessmentRepository(Application application){
        AcademicDatabaseBuilder db = AcademicDatabaseBuilder.getDatabase(application);
        mAssessmentDAO = db.assessmentDAO();

    }

    public Assessment getAsmntByAsmntId(int asmntId){
        databaseExecutor.execute(()->{
            mAssessment = mAssessmentDAO.getAsmntByAsmntId(asmntId);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAssessment;
    }


    public List<Assessment>getAllAssessments(){
        databaseExecutor.execute(()->{
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllAssessments;
    }

    public void insert(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.update(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.delete(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Assessment> getCourseAsscAsmnts(int courseId){
        databaseExecutor.execute(()->{
            mAllAsscAsmnts = mAssessmentDAO.getAsscAsmntsByCrsId(courseId);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAsscAsmnts;
    }


}
