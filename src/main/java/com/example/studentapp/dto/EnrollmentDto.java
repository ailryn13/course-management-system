package com.example.studentapp.dto;

import java.time.LocalDateTime;

public class EnrollmentDto {
    private Long id;
    private StudentDto student;
    private CourseDto course;
    private LocalDateTime enrollmentDate;

    public EnrollmentDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentDto getStudent() { return student; }
    public void setStudent(StudentDto student) { this.student = student; }

    public CourseDto getCourse() { return course; }
    public void setCourse(CourseDto course) { this.course = course; }

    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDateTime enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}