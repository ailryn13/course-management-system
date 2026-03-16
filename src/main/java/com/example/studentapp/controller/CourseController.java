package com.example.studentapp.controller;

import com.example.studentapp.entity.Course;
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
    public ResponseEntity<Course> createCourse(@RequestBody Course course){
        logger.info("Attempting to create a new Course: {}",course.getName());
        Course savedCourse = courseService.createCourse(course);
        logger.info("Succesfully created course with ID: {}", savedCourse.getId());
        return ResponseEntity.ok(savedCourse);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses(){
        logger.info("Fetching All available courses");
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
}
