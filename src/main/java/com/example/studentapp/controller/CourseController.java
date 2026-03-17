package com.example.studentapp.controller;

import com.example.studentapp.dto.CourseDto; // <-- Added DTO import
import com.example.studentapp.service.CourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private static final Logger logger = LogManager.getLogger(CourseController.class);

    private final CourseService courseService;

    @Autowired
    private CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @PostMapping
    // <-- Changed Course to CourseDto in the return type and parameter
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto){
        logger.info("Attempting to create a new Course: {}", courseDto.getName());

        // <-- Changed Course to CourseDto here
        CourseDto savedCourse = courseService.createCourse(courseDto);

        logger.info("Successfully created course with ID: {}", savedCourse.getId());
        return ResponseEntity.ok(savedCourse);
    }

    @GetMapping
    // <-- Changed Course to CourseDto in the return type
    public ResponseEntity<List<CourseDto>> getAllCourses(){
        logger.info("Fetching All available courses");

        // <-- Changed Course to CourseDto here
        List<CourseDto> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
}