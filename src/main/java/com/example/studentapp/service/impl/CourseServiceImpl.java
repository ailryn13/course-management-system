package com.example.studentapp.service.impl;

import com.example.studentapp.dto.CourseDto;
import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.CourseRepository;
import com.example.studentapp.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // The annotation goes on the implementation!
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    // --- Private Helper Methods ---
    private CourseDto convertToDto(CourseBean course) {
        CourseDto courseDto = new CourseDto();
        BeanUtils.copyProperties(course, courseDto);
        return courseDto;
    }

    private CourseBean convertToEntity(CourseDto courseDto) {
        CourseBean course = new CourseBean();
        BeanUtils.copyProperties(courseDto, course, "id");
        return course;
    }

    // --- Interface Methods ---
    @Override
    public CourseDto createCourse(CourseDto courseDto){
        CourseBean course = convertToEntity(courseDto);
        course.setAvailableSeats(course.getTotalSeats());
        CourseBean savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    @Override
    public List<CourseDto> getAllCourses(){
        return courseRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseById(Long id){
        CourseBean course = courseRepository.findById(id)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Course not found with ID: " + id));
        return convertToDto(course);
    }
}