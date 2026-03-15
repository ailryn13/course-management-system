package com.example.studentapp.controller;

import com.example.studentapp.entity.Student;
import com.example.studentapp.service.EnrollmentService;
import com.example.studentapp.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

public StudentController(StudentService studentService, EnrollmentService enrollmentService){
    this.studentService = studentService;
    this.enrollmentService = enrollmentService;
}

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody Student student) throws Exception{
        Student savedStudent = studentService.registerStudent(student);

        Map<String,Object> response = new HashMap<>();
        response.put("message","Registration Successful!");
        response.put("studentId",savedStudent.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginStudent(@RequestBody Map<String,String> credentials) throws Exception{
        String email = credentials.get("email");
        String password = credentials.get("password");

        Student loggedInStudent = studentService.loginStudent(email,password);

        Map<String, Object>response = new HashMap<>();
        response.put("message","login Successful!");
        response.put("studentId", loggedInStudent.getId());
        response.put("name", loggedInStudent.getName());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/enrollments")
    public ResponseEntity<?> getStudentEnrollments(@PathVariable Long id){
        return ResponseEntity.ok(enrollmentService.getStudentEnrollments(id));
    }
}
