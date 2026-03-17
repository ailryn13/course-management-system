package com.example.studentapp.service.impl;

import com.example.studentapp.dto.CourseDto;
import com.example.studentapp.dto.EnrollmentDto;
import com.example.studentapp.dto.StudentDto;
import com.example.studentapp.entity.Course;
import com.example.studentapp.entity.Enrollment;
import com.example.studentapp.entity.Student;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.CourseRepository;
import com.example.studentapp.repository.EnrollmentRepository;
import com.example.studentapp.repository.StudentRepository;
import com.example.studentapp.service.EnrollmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, CourseRepository courseRepository){
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // --- Private DTO Mapping Helper ---
    private EnrollmentDto convertToDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto();
        BeanUtils.copyProperties(enrollment, dto, "student", "course");

        if (enrollment.getStudent() != null) {
            StudentDto studentDto = new StudentDto();
            BeanUtils.copyProperties(enrollment.getStudent(), studentDto);
            studentDto.setPassword(null);
            dto.setStudent(studentDto);
        }

        if (enrollment.getCourse() != null) {
            CourseDto courseDto = new CourseDto();
            BeanUtils.copyProperties(enrollment.getCourse(), courseDto);
            dto.setCourse(courseDto);
        }

        return dto;
    }

    // --- Interface Methods ---
    @Override
    @Transactional
    public EnrollmentDto enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Course not found!"));

        if(enrollmentRepository.existsByStudentAndCourse(student,course)){
            throw new AppExceptions.DuplicateResourceException("Student is already Enrolled in this course!");
        }
        if(course.getAvailableSeats() <= 0){
            throw new AppExceptions.CourseFullException("Sorry, this course is fully booked!");
        }

        course.setAvailableSeats(course.getAvailableSeats()-1);
        courseRepository.save(course);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDto(savedEnrollment);
    }

    @Override
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

    @Override
    public List<EnrollmentDto> getStudentEnrollments(Long studentId){
        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}