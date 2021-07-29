package com.example.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CourseDTO {
    private Long cid;
    private String subject;
    private String schedule_Time;
    private String date;
    private Long maximum_Student;
//    private Teacher tid;
    private Set<String> students = new HashSet<>();
}
