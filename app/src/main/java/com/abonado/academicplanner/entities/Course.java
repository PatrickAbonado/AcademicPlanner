package com.abonado.academicplanner.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.List;

@Entity(foreignKeys =  {
        @ForeignKey(entity = Term.class, parentColumns = "termId",
                childColumns = "courseTermId", onDelete = ForeignKey.RESTRICT),
        @ForeignKey(entity = Instructor.class, parentColumns = "instructorId",
                childColumns = "courseInstrId", onDelete = ForeignKey.RESTRICT)})
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private String courseTitle;
    private Timestamp courseStart;
    private Timestamp courseEnd;
    private boolean isPerformance;
    private Instructor courseInstructor;
    private String courseNotes;
    private List<Assessment> courseAssessments;
    private int courseTermId;
    private int courseInstrId;

    public Course(int courseId, String courseTitle, Timestamp courseStart, Timestamp courseEnd,
                  boolean isPerformance, Instructor courseInstructor, String courseNotes,
                  List<Assessment> courseAssessments, int courseTermId, int courseInstrId) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.isPerformance = isPerformance;
        this.courseInstructor = courseInstructor;
        this.courseNotes = courseNotes;
        this.courseAssessments = courseAssessments;
        this.courseTermId = courseTermId;
        this.courseInstrId = courseInstrId;
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

    public boolean getIsPerformance() {
        return isPerformance;
    }

    public void setCourseStatus(boolean isPerformance) {
        this.isPerformance = isPerformance;
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

    public int getCourseTermId() {
        return courseTermId;
    }

    public void setCourseTermId(int courseTermId) {
        this.courseTermId = courseTermId;
    }

    public int getCourseInstrId() {
        return courseInstrId;
    }

    public void setCourseInstrId(int courseInstrId) {
        this.courseInstrId = courseInstrId;
    }
}
