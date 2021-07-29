package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teacher")
public class Teacher {

    @Id
    @Column(name = "teacher_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;
    private String name;
    @OneToMany(mappedBy = "tid", cascade = CascadeType.PERSIST, orphanRemoval = true)

    private List<Course> courses;
    public Teacher(List<Course> courses) {
        this.courses = courses;
    }



    public Teacher(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public Teacher() {

    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "tid='" + tid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
