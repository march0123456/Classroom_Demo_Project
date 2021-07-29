package com.example.demo;

import com.example.demo.exceptionHandler.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ClassroomService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;



    @Autowired
    public ClassroomService(TeacherRepository teacherRepository,
                            StudentRepository studentRepository,
                            CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }


    public List<Student> getAllStudent() {

        return studentRepository.findAll();
    }

    public List<StudentDTO> getSingleStudent( long sid) {

        List<StudentDTO> list = new ArrayList<>();
        Optional<Student> students = studentRepository.findById(sid);
        if (students.isPresent()) {
            students.stream().forEach(student -> {
                StudentDTO studentDTO = mapDtoToEntity(student);
                list.add(studentDTO);
            });
            return list;
        }
        throw new NotFoundException("Id does not exists");
    }



    public ResponseEntity<Teacher> getSingleTeacher(long tid) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(tid);
        if(teacherOptional.isPresent()){
            return new ResponseEntity<>(teacherOptional.get(), HttpStatus.OK);
        }else{
            throw new RecordNotFoundException("Invalid id:"+ tid);
        }
    }

    public List<Teacher> getAllTeacher() {
        return teacherRepository.findAll();
    }

    public Teacher addNewTeacher(Teacher teacher) {
       Optional<Teacher> teacherOptional = teacherRepository
               .findTeacherByNameContaining(teacher.getName());
       if (teacherOptional.isPresent()){
           throw new NotFoundException("registered already");
       }
        return teacherRepository.save(teacher);

    }

    public Student addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository
                .findStudentByNameContaining(student.getName());
        if (studentOptional.isPresent()){
            throw new IllegalStateException("registered already");
        }

        return studentRepository.save(student);
    }

    public List<CourseDTO> getCourses() {

        List<CourseDTO> courseList = new ArrayList<>();
        List<Course> courses = courseRepository.findAll();
        courses.stream().forEach(course ->{

            CourseDTO courseDTO = courseDTOEntity(course);
            courseList.add(courseDTO);

        } );
        return courseList  ;

    }
    private CourseDTO courseDTOEntity(Course course) {

        CourseDTO responseDTO = new CourseDTO();
        responseDTO.setSubject(course.getSubject());
        responseDTO.setCid(course.getCid());
        responseDTO.setMaximum_Student(course.getMaximum_Student());
        responseDTO.setSchedule_Time(course.getSchedule_Time());
        responseDTO.setDate(course.getDate());
//        responseDTO.setTid(course.getTid());
        responseDTO.setStudents(course.getStudents().stream()
                .map(Student::getName).collect(Collectors.toSet()));
        return responseDTO;
    }

    public StudentDTO addStudentCourse(Long sid, Long cid, StudentDTO studentDTO) throws Exception {
        Student student = studentRepository.findById(sid).get();
        if (student.getSid() == null){
            throw new NotFoundException("This Id does not exists");
        }
        Course course = courseRepository.findById(cid).get();
        //check whether course are full or not?
        registerStudent(course);
        timeConflictChecked(course, student);
        mapDtoToEntity(studentDTO, student);
        Student studentSaved = studentRepository.save(student);

        return mapDtoToEntity(studentSaved);
    }
    private void timeConflictChecked(Course course, Student student) throws Exception {

        if (student.getCourses().stream().map(Course::getDate)
                .collect(Collectors.toList()).contains(course.getDate()))
        {
            throw new Exception("Cant register this course (Time conflicted)");
        }
    }

    private void registerStudent(Course course) throws Exception {
        if(course.getMaximum_Student() == course.getStudents().size()){
            throw new Exception("Full");
        }
    }

    private void mapDtoToEntity(StudentDTO studentDto, Student student) {

        if(null == student.getCourses()){
            student.setCourses(new HashSet<>());
        }
        studentDto.getCourses().forEach(courseName->{
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


    public String deleteStudentCourse(Long sid, String course) {

        Optional<Student> student = studentRepository.findById(sid) ;
        if (!student.isPresent()){
            throw new NotFoundException("Id not found");
        }
        student.get().getCourses().stream().filter(course1
                -> course.equals(course1.getSubject())).map(Course::getSubject)
                .collect(Collectors.toSet());
        student.get().removeCourse(courseRepository.findBySubject(course));
        studentRepository.save(student.get());

        return " Student " + sid + " delete " + course + " successfully";
    }
}
