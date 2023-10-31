package com.abonado.academicplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Instructor {

    @PrimaryKey(autoGenerate = true)
    private int instructorId;
    private String instructorName;
    private String instructorPhone;
    private String instructorEmail;

    public Instructor(int instructorId, String instructorName, String instructorPhone, String instructorEmail) {
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
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
}
