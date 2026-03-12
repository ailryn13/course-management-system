package com.example.studentapp.controller;

import com.example.studentapp.dto.EnrollmentRequest;
import com.example.studentapp.entity.Enrollment;
import com.example.studentapp.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/enroll")
@CrossOrigin(origins = "*")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService){
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> enrollStudent(@PathVariable Long courseId,@RequestParam Long studentId){
        try{
            Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> unenrollStudent(@PathVariable Long courseId,@RequestParam Long studentId){
        try{
            enrollmentService.unenrollStudent(studentId,courseId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Succesfully unenrolled from the course!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
