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

    private final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    public Student registerStudent(Student student) throws Exception{

        String cleanEmail = student.getEmail() != null ? student.getEmail().trim().toLowerCase() : null;
        String cleanName = student.getName() != null ? student.getName().trim() : null;

        student.setEmail(cleanEmail);
        student.setEmail(cleanName);

        if(cleanName==null || !cleanName.matches("[A-Za-z ]+$")){
            throw new Exception("Registration failed: Name can only contain letters and spaces!");
        }

        if(cleanEmail == null || !cleanEmail.matches(EMAIL_REGEX)){
            throw new Exception("Registration failed: Invalid email format!");
        }

        if(student.getPassword() == null || student.getPassword().length()<6){
            throw new Exception("Registration failed: Password must be at le");
        }

        Optional<Student> existingStudent= studentRepository.findByEmail(cleanEmail);
        if(existingStudent.isPresent()){
            throw new Exception("Email is already Registered!");
        }
        String hashedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(hashedPassword);

        return studentRepository.save(student);
    }
    public Student loginStudent(String email, String rawPassword) throws Exception{
        String cleanEmail = email != null ? email.trim() : null;

        if(cleanEmail == null || !cleanEmail.matches(EMAIL_REGEX)){
            throw new Exception("Invalid email format!");
        }

        Student student = studentRepository.findByEmail(cleanEmail)
            .orElseThrow(() -> new Exception("User does not exist!"));

        if(!passwordEncoder.matches(rawPassword, student.getPassword())){
            throw new Exception("Invalid email or Password!");
        }
        return student;
    }
}
