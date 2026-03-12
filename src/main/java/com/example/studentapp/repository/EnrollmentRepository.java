package com.example.studentapp.repository;

import com.example.studentapp.entity.Course;
import com.example.studentapp.entity.Enrollment;
import com.example.studentapp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentAndCourse(Student student, Course course);

    List<Enrollment> findByStudentId(Long studentID);

    Optional<Enrollment> findByStudentAndCourse(Student student,Course course);
}
