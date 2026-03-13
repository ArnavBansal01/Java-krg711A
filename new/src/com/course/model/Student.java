package com.course.model;

public class Student {

    private int studentid;
    private String studentname;

    public Student(int studentid, String studentname){
        this.studentid = studentid;
        this.studentname = studentname;
    }

    public int getstudentid(){
        return studentid;
    }

    public void setstudentid(int studentid){
        this.studentid = studentid;
    }

    public String getstudentname(){
        return studentname;
    }

    public void setstudentname(String studentname){
        this.studentname = studentname;
    }

    public void display(){
        System.out.println(studentid + " " + studentname);
    }
}