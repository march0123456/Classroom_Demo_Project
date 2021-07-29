package com.example.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {
    @Id
//    @Column(name = "course_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "course_sequence")
    @SequenceGenerator(name = "course_sequence",sequenceName = "course_sequence", allocationSize = 1)
    private Long cid;
    private String subject;
    @JsonFormat(pattern = "DDD HH:MM")
    private String date;
    private String schedule_Time;
    private Long maximum_Student;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable= false)
    @OnDelete(action = OnDeleteAction.CASCADE)

    private Teacher tid;

    @ManyToMany(mappedBy = "courses", cascade = CascadeType.MERGE)

    private Set<Student> students = new HashSet<>();
    public Course() {
    }

    public Course(String subject,
                  String schedule_Time,
                  Long maximum_Student,
                  Teacher tid,
                  Set<Student> students) {
        this.subject = subject;
        this.schedule_Time = schedule_Time;
        this.maximum_Student = maximum_Student;
        this.tid = tid;
        this.students = students;
    }

    public Course(Long cid, String subject,
                  String schedule_Time,
                  Long maximum_Student,
                  Teacher tid,
                  Set<Student> students) {
        this.cid = cid;
        this.subject = subject;
        this.schedule_Time = schedule_Time;
        this.maximum_Student = maximum_Student;
        this.tid = tid;
        this.students = students;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSchedule_Time() {
        return schedule_Time;
    }

    public void setSchedule_Time(String schedule_Time) {
        this.schedule_Time = schedule_Time;
    }

    public Long getMaximum_Student() {
        return maximum_Student;
    }
    @PreRemove
    public void removeCourseFromStudent() {
        for (Student s : students) {
            s.getCourses().remove(this);
        }
    }

    public void setMaximum_Student(Long maximum_Student) {
        this.maximum_Student = maximum_Student;
    }
// ----------------------------------
    public Teacher getTid() {
        return tid;
    }

    public Course setTid(Teacher tid) {
        this.tid = tid;
        return null;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Course{" +
                "cid=" + cid +
                ", subject='" + subject + '\'' +
                ", schedule_Time='" + schedule_Time + '\'' +
                ", maximum_Student=" + maximum_Student +
                ", tid=" + tid +
                ", students=" + students +
                '}';
    }
}
