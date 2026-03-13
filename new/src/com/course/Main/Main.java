package com.course.Main;

import com.course.model.Course;
import com.course.model.Student;
import com.course.service.CourseService;
import com.course.exception.CourseFullException;
import com.course.exception.CourseNotFoundException;

public class Main {

    public static void main(String[] args) {

        CourseService service = new CourseService();

        Course c1 = new Course(101,"java",2);
        Course c2 = new Course(102,"python",2);

        service.addCourse(c1);
        service.addCourse(c2);

        Student s1 = new Student(1,"arnav");
        Student s2 = new Student(2,"rahul");

        try{
            service.enrollStudent(101,s1);
            service.enrollStudent(101,s2);
        }
        catch(CourseNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(CourseFullException e){
            System.out.println(e.getMessage());
        }


        service.viewCourses();
    }
}