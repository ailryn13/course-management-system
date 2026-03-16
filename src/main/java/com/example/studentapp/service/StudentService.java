package com.example.studentapp.service;

import com.example.studentapp.entity.Student;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    private final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    public Student registerStudent(Student student){

        String cleanEmail = student.getEmail() != null ? student.getEmail().trim().toLowerCase() : null;
        String cleanName = student.getName() != null ? student.getName().trim() : null;

        student.setEmail(cleanEmail);
        student.setName(cleanName);

        if(cleanName==null || !cleanName.matches("[A-Za-z ]+$")){
            throw new AppExceptions.BadRequestException("Registration failed: Name can only contain letters and spaces!");
        }

        if(cleanEmail == null || !cleanEmail.matches(EMAIL_REGEX)){
            throw new AppExceptions.BadRequestException("Registration failed: Invalid email format!");
        }

        if(student.getPassword() == null || student.getPassword().length()<6){
            throw new AppExceptions.BadRequestException("Registration failed: Password must be at le");
        }

        Optional<Student> existingStudent= studentRepository.findByEmail(cleanEmail);
        if(existingStudent.isPresent()){
            throw new AppExceptions.DuplicateResourceException("Email is already Registered!");
        }
        String hashedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(hashedPassword);

        return studentRepository.save(student);
    }
    public Student loginStudent(String email, String rawPassword){
        String cleanEmail = email != null ? email.trim() : null;

        if(cleanEmail == null || !cleanEmail.matches(EMAIL_REGEX)){
            throw new AppExceptions.BadRequestException("Invalid email format!");
        }

        Student student = studentRepository.findByEmail(cleanEmail)
            .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("User does not exist!"));

        if(!passwordEncoder.matches(rawPassword, student.getPassword())){
            throw new AppExceptions.UnauthorizedException("Invalid email or Password!");
        }
        return student;
    }
}
