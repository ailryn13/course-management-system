package com.example.studentapp.service;

import com.example.studentapp.dto.StudentDto;

public interface StudentService {
    StudentDto registerStudent(StudentDto studentDto);
    StudentDto loginStudent(String email, String rawPassword);
}