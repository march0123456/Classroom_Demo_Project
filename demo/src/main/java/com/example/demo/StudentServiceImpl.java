package com.example.demo;

import com.example.demo.exceptionHandler.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private CourseRepository courseRepository;

    @Transactional
    @Override
    public StudentDTO addStudent(StudentDTO studentDto) {
        Student student = new Student();
            mapDtoToEntity(studentDto, student);
            Student savedStudent = studentRepository.save(student);
            return mapDtoToEntity(savedStudent);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        List<StudentDTO> studentDTOS = new ArrayList<>();
        List<Student> students = studentRepository.findAll();
        students.stream().forEach(student -> {
            StudentDTO studentDTO = mapDtoToEntity(student);
            studentDTOS.add(studentDTO);
        });
        return studentDTOS;
    }
    @Transactional
    @Override
    public StudentDTO updateStudent(Long sid, StudentDTO studentDTO) {
        Student std = studentRepository.findById(sid).get();
        std.getCourses().clear();
        mapDtoToEntity(studentDTO, std);
        Student student = studentRepository.save(std);
        return mapDtoToEntity(student);
    }

    @Override
    public String deleteStudent(Long sid) {
        Optional<Student> student = studentRepository.findById(sid);
        if(!student.isPresent()){

            throw new RecordNotFoundException("Invalid employee id : " + sid);
        }
        student.get().removeCourses();
        studentRepository.deleteById(student.get().getSid());
        return "Student with id: " + sid + " deleted successfully!";
    }



    private void mapDtoToEntity(StudentDTO studentDto, Student student) {
        student.setName(studentDto.getName());
        if(null == student.getCourses()){
            student.setCourses(new HashSet<>());
        }
        studentDto.getCourses().stream().forEach(courseName->{
            Course course = courseRepository.findBySubject(courseName);
            if (null == course){
                course = new Course();
                course.setStudents(new HashSet<>());
            }
            course.setSubject(courseName);
            student.addCourse(course);
        });
    }
    private StudentDTO mapDtoToEntity(Student student) {
        StudentDTO responseDTO = new StudentDTO();
        responseDTO.setName(student.getName());
        responseDTO.setId(student.getSid());
        responseDTO.setCourses(student.getCourses().stream()
                .map(Course::getSubject).collect(Collectors.toSet()));
        return  responseDTO;
    }

}
