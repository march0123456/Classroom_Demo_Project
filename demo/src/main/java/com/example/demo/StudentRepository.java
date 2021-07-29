package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, Long> {
    //    List<Student> findStudentByNameContaining(String name);
    Optional<Student> findStudentByNameContaining(String name);

    public static final String find_Students = "SELECT sid, name, courses FROM student";

    @Query(value = find_Students, nativeQuery = true)
    public List<Object[]> findStudents();

    @Modifying
    @Query(nativeQuery = true, value = "delete from Student as s where s.sid = :sid")
    @Transactional
    public int deletedCourses(
                                  @Param("sid")Long sid);


}