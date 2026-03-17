package com.example.studentapp.service;

import com.example.studentapp.dto.EnrollmentDto;
import java.util.List;

public interface EnrollmentService {
    EnrollmentDto enrollStudent(Long studentId, Long courseId);
    void unenrollStudent(Long studentId, Long courseId);
    List<EnrollmentDto> getStudentEnrollments(Long studentId);
}