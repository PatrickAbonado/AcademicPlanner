package com.abonado.academicplanner.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(foreignKeys = @ForeignKey(entity = Term.class, parentColumns = "termId",
                childColumns = "courseTermId", onDelete = ForeignKey.RESTRICT))
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private int courseTermId;
    private String courseTitle;
    private String courseStart;
    private String courseEnd;
    private String courseStatus;
    private String courseNotes;
    private String courseInstrName;
    private String courseInstrPhone;
    private String courseInstrEmail;


    public Course( int courseId, int courseTermId, String courseTitle, String courseStart,
                  String courseEnd, String courseStatus, String courseNotes,
                  String courseInstrName, String courseInstrPhone, String courseInstrEmail) {

        this.courseId = courseId;
        this.courseTermId = courseTermId;
        this.courseTitle = courseTitle;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus;
        this.courseNotes = courseNotes;
        this.courseInstrName = courseInstrName;
        this.courseInstrPhone = courseInstrPhone;
        this.courseInstrEmail = courseInstrEmail;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseTermId() {
        return courseTermId;
    }

    public void setCourseTermId(int courseTermId) {
        this.courseTermId = courseTermId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(String courseStart) {
        this.courseStart = courseStart;
    }

    public String getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(String courseEnd) {
        this.courseEnd = courseEnd;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseNotes() {
        return courseNotes;
    }

    public void setCourseNotes(String courseNotes) {
        this.courseNotes = courseNotes;
    }

    public String getCourseInstrName() {
        return courseInstrName;
    }

    public void setCourseInstrName(String courseInstrName) {
        this.courseInstrName = courseInstrName;
    }

    public String getCourseInstrPhone() {
        return courseInstrPhone;
    }

    public void setCourseInstrPhone(String courseInstrPhone) {
        this.courseInstrPhone = courseInstrPhone;
    }

    public String getCourseInstrEmail() {
        return courseInstrEmail;
    }

    public void setCourseInstrEmail(String courseInstrEmail) {
        this.courseInstrEmail = courseInstrEmail;
    }
}
