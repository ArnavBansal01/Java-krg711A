package com.course.model;

import java.util.ArrayList;

public class Course {
    private int courseid;
    private String coursename;
    private int maxseats;

    private ArrayList<Student>enrolledstudents;
    public Course(int courseid, String coursenname, int maxseats) {
        this.courseid = courseid;
        this.coursename = coursename;
        this.maxseats = maxseats;
        enrolledstudents = new ArrayList<>();
}


public void display(){
        System.out.println(courseid);

        System.out.println(coursename);
        System.out.println(maxseats);
        System.out.println(enrolledstudents.size());

}
    public int getCourseid() {
        return courseid;
    }

    public int getMaxseats() {
        return maxseats;
    }

    public ArrayList<Student> getEnrolledstudents() {
        return enrolledstudents;
    }
    public int getcourseid(){
        return courseid;
    }
    public String getcoursename(){
        return coursename;
    }
    public int getmaxseats(){
        return maxseats;
    }
    public ArrayList<Student> getenrolledstudents(){
        return enrolledstudents;
    }}
