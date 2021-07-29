package com.example.demo;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

@Configuration
public class ClassroomConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository studentRepository,
            TeacherRepository teacherRepository,
            CourseRepository courseRepository){
        return  args -> {
//            Student bob = new Student(
//                    1L,"bob"
//
//            );
//            Student march = new Student(
//                    2L,"march"
//
//            );
//            studentRepository.saveAllAndFlush(
//                    List.of(bob, march)
//            );
//            Teacher makhanov = new Teacher(
//                    1L,"makhanov"
//
//            );
//            Teacher cholwich = new Teacher(
//                    2L,"cholwich"
//
//            );
//            teacherRepository.saveAllAndFlush(
//                    List.of(makhanov, cholwich)
//            );
        };

    }
}

