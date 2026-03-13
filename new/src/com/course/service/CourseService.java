package com.course.service;

import com.course.model.Course;
import com.course.model.Student;
import com.course.exception.CourseFullException;
import com.course.exception.CourseNotFoundException;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CourseService {

    ArrayList<Course> courses = new ArrayList<>();

    public void addCourse(Course c){
        courses.add(c);
    }

    public void enrollStudent(int courseid, Student s)
            throws CourseNotFoundException, CourseFullException{

        Course found = null;

        for(Course c : courses){
            if(c.getCourseid() == courseid){
                found = c;
            }
        }

        if(found == null){
            throw new CourseNotFoundException("Course not found");
        }

        if(found.getEnrolledstudents().size() >= found.getMaxseats()){
            throw new CourseFullException("Course full");
        }

        found.getEnrolledstudents().add(s);

        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("courses.txt", true));
            bw.write(courseid + " " + s);
            bw.newLine();
            bw.close();
        }
        catch(IOException e){
            System.out.println("File error");
        }
    }

    public void viewCourses(){

        for(Course c : courses){
            c.display();
        }

        try{
            BufferedReader br = new BufferedReader(new FileReader("courses.txt"));
            String line;

            while((line = br.readLine()) != null){
                System.out.println(line);
            }

            br.close();
        }
        catch(IOException e){
            System.out.println("File error");
        }
    }
}