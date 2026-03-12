package com.example.studentapp.service;

import com.example.studentapp.entity.Student;
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

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    public Student registerStudent(Student student) throws Exception{
        if(student.getName()==null || !student.getName().matches("[A-Za-z ]+$")){
            throw new Exception("Registration failed: Name can only contain letters and spaces!");
        }
        Optional<Student> existingStudent= studentRepository.findByEmail(student.getEmail());
        if(existingStudent.isPresent()){
            throw new Exception("Email is already Registered!");
        }
        String hashedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(hashedPassword);

        return studentRepository.save(student);
    }
    public Student loginStudent(String email, String rawPassword) throws Exception{
        Student student = studentRepository.findByEmail(email)
            .orElseThrow(() -> new Exception("User does not exist!"));

        if(!passwordEncoder.matches(rawPassword, student.getPassword())){
            throw new Exception("Invalid email or Password!");
        }
        return student;
    }
}
