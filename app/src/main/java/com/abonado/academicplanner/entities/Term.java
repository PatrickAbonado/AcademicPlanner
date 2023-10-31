package com.abonado.academicplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.List;

@Entity
public class Term {

    @PrimaryKey(autoGenerate = true)
    private int termId;
    private String termName;
    private Timestamp termStart;
    private Timestamp termEnd;
    private List<Course> termCourses;

    public Term(int termId, String termName, Timestamp termStart,
                Timestamp termEnd, List<Course> termCourses) {
        this.termId = termId;
        this.termName = termName;
        this.termStart = termStart;
        this.termEnd = termEnd;
        this.termCourses = termCourses;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public Timestamp getTermStart() {
        return termStart;
    }

    public void setTermStart(Timestamp termStart) {
        this.termStart = termStart;
    }

    public Timestamp getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(Timestamp termEnd) {
        this.termEnd = termEnd;
    }

    public List<Course> getTermCourses() {
        return termCourses;
    }

    public void setTermCourses(List<Course> termCourses) {
        this.termCourses = termCourses;
    }


}
