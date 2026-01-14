package com.aicourse.controller;

import com.aicourse.model.Course;
import com.aicourse.model.Module;
import com.aicourse.service.courses.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/generate")
    public Course createCourse(@RequestBody Map<String, String> payload, Authentication auth) {
        return courseService.generateCourse(payload, auth.getName());
    }

    @GetMapping
    public List<Course> getMyCourses(Authentication auth) {
        return courseService.getCoursesByCreator(auth.getName());
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @GetMapping("/{courseName}/modules")
    public List<Module> getModulesByCourseName(@PathVariable String courseName) {
        return courseService.getModulesByCourseName(courseName);
    }
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
}