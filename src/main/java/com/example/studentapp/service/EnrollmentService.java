package com.example.studentapp.service;

import com.example.studentapp.entity.Course;
import com.example.studentapp.entity.Enrollment;
import com.example.studentapp.entity.Student;
import com.example.studentapp.exception.GlobalExceptionHandler;
import com.example.studentapp.repository.CourseRepository;
import com.example.studentapp.repository.EnrollmentRepository;
import com.example.studentapp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository,StudentRepository studentRepository,CourseRepository courseRepository){
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Course not found!"));

        if(enrollmentRepository.existsByStudentAndCourse(student,course)){
            throw new GlobalExceptionHandler.DuplicateResourceException("Student is already Enrolled in this course!");
        }
        if(course.getAvailableSeats()<=0){
            throw new GlobalExceptionHandler.CourseFullException("Sorry, this course is fully booked!");
        }

        course.setAvailableSeats(course.getAvailableSeats()-1);
        courseRepository.save(course);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void unenrollStudent(Long studentId, Long courseId) throws Exception{
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new Exception("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception("Course not found!"));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student,course)
                .orElseThrow(() -> new Exception("Enrollment record not found"));

        enrollmentRepository.delete(enrollment);

        course.setAvailableSeats(course.getAvailableSeats()+1);

        courseRepository.save(course);
    }

    public List<Enrollment> getStudentEnrollments(Long studentId){
        return enrollmentRepository.findByStudentId(studentId);
    }
}
