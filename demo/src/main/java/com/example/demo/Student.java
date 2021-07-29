package com.example.demo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence",allocationSize = 1)
    private  Long sid;
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private  String name;


    @ManyToMany (cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "student_course", joinColumns =
            {@JoinColumn(name = "student_id")},inverseJoinColumns ={
            @JoinColumn(name = "course_id")
    } )
    private  Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }

    public void  removeCourse(Course course) {
        this.getCourses().remove(course);
        course.getStudents().remove(this);

    }

    public void removeCourses() {
        for (Course course : new HashSet<>(courses)) {
            removeCourse(course);
        }
    }



    public Student() {
    }

    public Student( String name) {

        this.name = name;
    }

    public Student(String name, Set<Course> courses) {
        this.name = name;
        this.courses = courses;
    }



    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                '}';
    }
}
