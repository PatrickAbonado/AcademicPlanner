package com.abonado.academicplanner.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys =  @ForeignKey(entity = Course.class, parentColumns = "courseId",
        childColumns = "instructorCrsId", onDelete = ForeignKey.RESTRICT))
public class Instructor {

    @PrimaryKey(autoGenerate = true)
    private int instructorId;
    private String instructorName;
    private String instructorPhone;
    private String instructorEmail;
    private int instructorCrsId;

    public Instructor(int instructorId, String instructorName, String instructorPhone,
                      String instructorEmail, int instructorCrsId) {
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.instructorCrsId = instructorCrsId;
    }


    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public int getInstructorCrsId() {
        return instructorCrsId;
    }

    public void setInstructorCrsId(int instructorCrsId) {
        this.instructorCrsId = instructorCrsId;
    }
}
