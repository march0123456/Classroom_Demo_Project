package com.example.demo;

import java.util.List;

public interface StudentService {

    public StudentDTO addStudent(StudentDTO studentDto);
    public List<StudentDTO> getAllStudents();
    public StudentDTO updateStudent(Long sid, StudentDTO studentDTO);
    public String deleteStudent(Long sid);
}
