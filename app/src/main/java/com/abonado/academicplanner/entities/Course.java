package com.abonado.academicplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.List;

@Entity
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private String courseTitle;
    private Timestamp courseStart;
    private Timestamp courseEnd;
    private String courseStatus;
    private Instructor courseInstructor;
    private String courseNotes;
    private List<Assessment> courseAssessments;

    public Course(int courseId, String courseTitle, Timestamp courseStart, Timestamp courseEnd, String courseStatus, Instructor courseInstructor, String courseNotes, List<Assessment> courseAssessments) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus;
        this.courseInstructor = courseInstructor;
        this.courseNotes = courseNotes;
        this.courseAssessments = courseAssessments;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Timestamp getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(Timestamp courseStart) {
        this.courseStart = courseStart;
    }

    public Timestamp getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(Timestamp courseEnd) {
        this.courseEnd = courseEnd;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public Instructor getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(Instructor courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public String getCourseNotes() {
        return courseNotes;
    }

    public void setCourseNotes(String courseNotes) {
        this.courseNotes = courseNotes;
    }

    public List<Assessment> getCourseAssessments() {
        return courseAssessments;
    }

    public void setCourseAssessments(List<Assessment> courseAssessments) {
        this.courseAssessments = courseAssessments;
    }
}
