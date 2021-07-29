package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/classroom")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private CourseRepository courseRepository;

    @GetMapping("/stu/students")
    public ResponseEntity<List<StudentDTO>>getAllStudents(){
        List<StudentDTO> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping("/stu/student")
    public ResponseEntity<Optional<StudentDTO>>addStudent(
            @RequestBody StudentDTO studentDTO){

        StudentDTO std = studentService.addStudent(studentDTO);

            return new ResponseEntity(std, HttpStatus.CREATED);


    }
    @PutMapping("/stu/student/{sid}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable(name = "sid") Long sid,
                                                    @RequestBody StudentDTO student) {
        StudentDTO std = studentService.updateStudent(sid, student);
        return new ResponseEntity<>(std, HttpStatus.CREATED);
    }

    @DeleteMapping("/stu/student/{sid}")
    public ResponseEntity<String> deleteStudent(@PathVariable(name = "sid") Long sid) {
        String message = studentService.deleteStudent(sid);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
