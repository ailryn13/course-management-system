package com.example.studentapp.service;

import com.example.studentapp.entity.Course;
import com.example.studentapp.entity.Enrollment;
import com.example.studentapp.entity.Student;
import com.example.studentapp.exception.AppExceptions;
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
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Course not found!"));

        if(enrollmentRepository.existsByStudentAndCourse(student,course)){
            throw new AppExceptions.DuplicateResourceException("Student is already Enrolled in this course!");
        }
        if(course.getAvailableSeats()<=0){
            throw new AppExceptions.CourseFullException("Sorry, this course is fully booked!");
        }

        course.setAvailableSeats(course.getAvailableSeats()-1);
        courseRepository.save(course);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void unenrollStudent(Long studentId, Long courseId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Course not found!"));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student,course)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Enrollment record not found"));

        enrollmentRepository.delete(enrollment);

        course.setAvailableSeats(course.getAvailableSeats()+1);

        courseRepository.save(course);
    }

    public List<Enrollment> getStudentEnrollments(Long studentId){
        return enrollmentRepository.findByStudentId(studentId);
    }
}
