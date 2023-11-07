package com.abonado.academicplanner.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "courseId",
        childColumns = "asmntCourseId", onDelete = ForeignKey.RESTRICT))
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    private int assessmentId;
    private int asmntCourseId;
    private String assessmentTitle;
    private String assessmentType;
    private String assessmentStart;
    private String assessmentEnd;

    public Assessment(int assessmentId, int asmntCourseId, String assessmentTitle,
                      String assessmentType, String assessmentStart, String assessmentEnd) {
        this.assessmentId = assessmentId;
        this.asmntCourseId = asmntCourseId;
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

    public String getAssessmentStart() {
        return assessmentStart;
    }

    public void setAssessmentStart(String assessmentStart) {
        this.assessmentStart = assessmentStart;
    }

    public String getAssessmentEnd() {
        return assessmentEnd;
    }

    public void setAssessmentEnd(String assessmentEnd) {
        this.assessmentEnd = assessmentEnd;
    }

    public int getAsmntCourseId() {
        return asmntCourseId;
    }

    public void setAsmntCourseId(int asmntCourseId) {
        this.asmntCourseId = asmntCourseId;
    }
}
