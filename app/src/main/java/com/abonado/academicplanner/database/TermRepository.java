package com.abonado.academicplanner.database;

import android.app.Application;


import com.abonado.academicplanner.dao.TermDAO;
import com.abonado.academicplanner.entities.Course;
import com.abonado.academicplanner.entities.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TermRepository {

    private TermDAO mTermDAO;


    private List<Course> mAllAsscCourses;
    private List<Term> mAllTerms;
    private Term mTerm;

    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public TermRepository(Application application){
        AcademicDatabaseBuilder db=AcademicDatabaseBuilder.getDatabase(application);
        mTermDAO = db.termDAO();
    }


    public Term getTerm(int termId){
        databaseExecutor.execute(()->{
            mTerm = mTermDAO.getTerm(termId);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mTerm;
    }


    public List<Term>getAllTerms(){
        databaseExecutor.execute(()->{
            mAllTerms = mTermDAO.getAllTerms();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllTerms;
    }


    public void insert(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.insert(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.update(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.delete(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Course>getTermCourses(int termId){
        databaseExecutor.execute(()->{
            mAllAsscCourses = mTermDAO.getTermCourses(termId);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAsscCourses;
    }

}
