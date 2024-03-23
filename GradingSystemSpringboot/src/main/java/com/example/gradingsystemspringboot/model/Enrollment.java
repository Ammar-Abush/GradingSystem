package com.example.gradingsystemspringboot.model;

import java.io.Serializable;
import java.util.Date;

public class Enrollment implements Serializable {
    private String ssn;
    private String courseId;
    private Date dateRegistered;
    private String grade;

    private double average;

    public int getHighest() {
        return highest;
    }

    public void setHighest(int highest) {
        this.highest = highest;
    }

    private int highest;

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    private int lower;

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    private int mark;

    // Constructors, getters, and setters

    public Enrollment() {
    }

    public Enrollment(String ssn, String courseId, Date dateRegistered, String grade, int mark) {
        this.ssn = ssn;
        this.courseId = courseId;
        this.dateRegistered = dateRegistered;
        this.grade = grade;
        this.mark = mark;
    }

    // Add getters and setters for each attribute

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
