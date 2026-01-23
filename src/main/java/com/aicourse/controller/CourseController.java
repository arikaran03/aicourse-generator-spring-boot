package com.aicourse.controller;

import com.aicourse.model.Course;
import com.aicourse.model.Module;
import com.aicourse.service.courses.impl.CourseServiceImpl;
import com.aicourse.utils.api.ApiResponse;
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
    private CourseServiceImpl courseServiceImpl;

    @PostMapping("/generate")
    public Course createCourse(@RequestBody Map<String, String> payload, Authentication auth) {
        return courseServiceImpl.generateCourse(payload, auth.getName());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateCourseName(@PathVariable("id") Long courseId, @RequestBody Course courseDO
    ) {
        courseServiceImpl.updateCourse(courseId, courseDO);
        return ResponseEntity.ok(
                ApiResponse.success("Course updated successfully", null)
        );
    }

    @GetMapping
    public List<Course> getMyCourses(Authentication auth) {
        return courseServiceImpl.getCoursesByCreator(auth.getName());
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id) {
        return courseServiceImpl.getCourseById(id);
    }

    @GetMapping("/{courseName}/modules")
    public List<Module> getModulesByCourseName(@PathVariable String courseName) {
        return courseServiceImpl.getModulesByCourseName(courseName);
    }
    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Object>> deleteCourse(
            @PathVariable Long courseId) {

        courseServiceImpl.deleteCourse(courseId);

        return ResponseEntity.ok(
                ApiResponse.success("Course deleted successfully", null)
        );
    }
}