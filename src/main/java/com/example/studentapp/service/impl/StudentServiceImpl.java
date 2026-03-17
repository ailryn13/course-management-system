package com.example.studentapp.service.impl;

import com.example.studentapp.dto.StudentDto;
import com.example.studentapp.entity.Student;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.StudentRepository;
import com.example.studentapp.service.StudentService;
import com.example.studentapp.util.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder){
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private StudentDto convertToDto(Student student) {
        StudentDto dto = new StudentDto();
        BeanUtils.copyProperties(student, dto);
        dto.setPassword(null); // Mask password
        return dto;
    }

    private Student convertToEntity(StudentDto dto) {
        Student student = new Student();
        BeanUtils.copyProperties(dto, student, "id"); // Ignore ID on creation
        return student;
    }

    // --- Interface Methods ---
    @Override
    public StudentDto registerStudent(StudentDto studentDto){

        if(!ValidationUtils.isValidName(studentDto.getName())){
            throw new AppExceptions.BadRequestException("Registration failed: Name can only contain letters and spaces!");
        }

        if(!ValidationUtils.isValidEmail(studentDto.getEmail())){
            throw new AppExceptions.BadRequestException("Registration failed: Invalid email format!");
        }

        if(!ValidationUtils.isValidPassword(studentDto.getPassword())){
            throw new AppExceptions.BadRequestException("Registration failed: Password must be at least 6 characters!");
        }

        String cleanEmail = studentDto.getEmail().trim().toLowerCase();
        String cleanName = studentDto.getName().trim();

        Optional<Student> existingStudent = studentRepository.findByEmail(cleanEmail);
        if(existingStudent.isPresent()){
            throw new AppExceptions.DuplicateResourceException("Email is already Registered!");
        }

        Student student = convertToEntity(studentDto);
        student.setEmail(cleanEmail);
        student.setName(cleanName);
        student.setPassword(passwordEncoder.encode(studentDto.getPassword()));

        Student savedStudent = studentRepository.save(student);
        return convertToDto(savedStudent);
    }

    @Override
    public StudentDto loginStudent(String email, String rawPassword){

        if(!ValidationUtils.isValidEmail(email)){
            throw new AppExceptions.BadRequestException("Invalid email format!");
        }

        String cleanEmail = email.trim().toLowerCase();

        Student student = studentRepository.findByEmail(cleanEmail)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("User does not exist!"));

        if(!passwordEncoder.matches(rawPassword, student.getPassword())){
            throw new AppExceptions.UnauthorizedException("Invalid email or Password!");
        }

        return convertToDto(student);
    }
}