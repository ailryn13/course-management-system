package com.example.studentapp.repository;

import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.entity.Enrollment;
import com.example.studentapp.entity.StudentBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentAndCourse(StudentBean student, CourseBean course);

    List<Enrollment> findByStudentId(Long studentID);

    Optional<Enrollment> findByStudentAndCourse(StudentBean student, CourseBean course);
}
