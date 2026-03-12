package com.example.studentapp.service;

import com.example.studentapp.entity.Course;
import com.example.studentapp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    public Course createCourse(Course course){
        course.setAvailableSeats(course.getTotalSeats());
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses(){
            return courseRepository.findAll();
    }

    public Course getCourseById(Long id) throws Exception{
        return courseRepository.findById(id)
                .orElseThrow(() -> new Exception("Course not found with ID: " + id));
    }
}
