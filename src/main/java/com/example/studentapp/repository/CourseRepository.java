package com.example.studentapp.repository;

import com.example.studentapp.entity.CourseBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseBean,Long> {

}
