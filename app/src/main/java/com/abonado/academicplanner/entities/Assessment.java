package com.abonado.academicplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity
public class Assessment {

    public Assessment(int assessmentId, String assessmentTitle, String assessmentType, Timestamp assessmentStart, Timestamp assessmentEnd) {
        this.assessmentId = assessmentId;
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentStart = assessmentStart;
        this.assessmentEnd = assessmentEnd;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Timestamp getAssessmentStart() {
        return assessmentStart;
    }

    public void setAssessmentStart(Timestamp assessmentStart) {
        this.assessmentStart = assessmentStart;
    }

    public Timestamp getAssessmentEnd() {
        return assessmentEnd;
    }

    public void setAssessmentEnd(Timestamp assessmentEnd) {
        this.assessmentEnd = assessmentEnd;
    }

    @PrimaryKey(autoGenerate = true)
    private int assessmentId;
    private String assessmentTitle;
    private String assessmentType;
    private Timestamp assessmentStart;
    private Timestamp assessmentEnd;
}
