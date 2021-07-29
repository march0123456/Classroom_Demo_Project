package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/classroom")
public class ClassroomController {
    private final ClassroomService classroomService;
    private final TeacherRepository teacherRepository;

    private final CourseRepository courseRepository;
    @Autowired
    public ClassroomController(ClassroomService classroomService,
                               TeacherRepository teacherRepository,
                               CourseRepository courseRepository)
    { this.classroomService = classroomService;
        this.courseRepository = courseRepository;
    this.teacherRepository = teacherRepository;}



    //GetAllStudent
    @GetMapping(path = "/students")
    public List<Student> getAllStudent() {
        return  classroomService.getAllStudent();}


    //GetAllTeacher
    @GetMapping(path = "/teachers")
    public List<Teacher> getAllTeacher(){
        return  classroomService.getAllTeacher();
    }
    //GetSingleStudent
    @GetMapping("/student/{sid}")
    public ResponseEntity<StudentDTO> getSingleStudent(
            @PathVariable long sid){
        List<StudentDTO> student = classroomService.getSingleStudent(sid);
        return new ResponseEntity(student, HttpStatus.OK);

    }
    //GetSingleTeacher
    @GetMapping(path = "/teacher/{tid}")
    public ResponseEntity<Teacher> getSingleTeacher(@PathVariable long tid){
        return  classroomService.getSingleTeacher(tid);
    }
    //AddTeacher
    @PostMapping(path = "/teacher")
    public ResponseEntity<Teacher> addNewTeacher (@RequestBody Teacher teacher){
        Teacher teacher1 =classroomService.addNewTeacher(teacher);
        return new ResponseEntity<>(teacher1,HttpStatus.CREATED);
    }
    //AddStudent
    @PostMapping(path ="/student")
    public ResponseEntity<Student> addNewStudent (@RequestBody Student student)
    {
        Student stdd = classroomService.addNewStudent(student);
        return new ResponseEntity(stdd, HttpStatus.CREATED);
    }

    //TeacherCreateCourse
    @PostMapping(path = "/course/{teacherId}")
    public ResponseEntity<Course> createCourse(@PathVariable (value = "teacherId")Long tid,
                             @RequestBody Course course) {
        if (teacherRepository.findById(tid).isPresent()){
            teacherRepository.findById(tid).map(teacher1 -> {
                teacher1.setTid(tid);
                course.setTid(teacher1);
                return courseRepository.save(course);
            });
        return new ResponseEntity(course, HttpStatus.CREATED);
    }
        return new ResponseEntity(course, HttpStatus.NO_CONTENT);
    }
    //TeacherDeleteCourse
    @DeleteMapping("/teacher/{teacherId}/course/{courseId}")
    public ResponseEntity<Object> deleteCourse(
            @PathVariable (value = "teacherId") Long tid
    ,@PathVariable(value = "courseId")Long cid){

        return courseRepository.findById(cid).map(course -> {
            courseRepository.delete(course);
            course.removeCourseFromStudent();
            return  ResponseEntity.ok().build();
        }).orElseThrow(()-> new NotFoundException("Course already doesn't exists"));
    }



    //TeacherUpdateCourse
    @PutMapping("/course/{courseId}")
    public Course updateCourse(@PathVariable(value = "courseId")Long cid,
                               @RequestBody Course courseUpdate){
        return courseRepository.findById(cid)
                .map(course -> {
                    course.setSubject(courseUpdate.getSubject());
                    course.setSchedule_Time(courseUpdate.getSchedule_Time());
                    course.setMaximum_Student(courseUpdate.getMaximum_Student());
                    course.setDate(courseUpdate.getDate());
                    course.setTid(courseUpdate.getTid());
                    return courseRepository.save(course);
                }).orElseThrow(()-> new NotFoundException("Course not found with id" + cid));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>>getCourses(){
        List<CourseDTO> courses = classroomService.getCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/stu/addStudent/{sid}/{cid}")
    public ResponseEntity<StudentDTO> addStudentCourse(@PathVariable(name = "sid")Long sid,
                                                 @PathVariable(name = "cid")Long cid,
                                                 @RequestBody StudentDTO studentDTO) throws Exception {
        StudentDTO std = classroomService.addStudentCourse(sid, cid, studentDTO);
        return new ResponseEntity<>(std, HttpStatus.OK);
    }
    @DeleteMapping("student/{sid}/course/{course}")
    public String  deleteStudentCourse(
            @PathVariable(value = "sid")Long sid,
            @PathVariable(value = "course")String course
    ){
        return classroomService.deleteStudentCourse(sid, course);
    }
}
