package com.example.studentapp.service;

import com.example.studentapp.dto.CourseDto;
import java.util.List;

public interface CourseService {
    CourseDto createCourse(CourseDto courseDto);
    List<CourseDto> getAllCourses();
    CourseDto getCourseById(Long id);
}