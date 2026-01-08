package com.aicourse.controller;

import com.aicourse.model.Course;
import com.aicourse.model.Module;
import com.aicourse.repo.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepo courseRepo;

    @PostMapping("/generate")
    public Course createCourse(@RequestBody Map<String, String> payload, Authentication auth) {
        String title = payload.get("title");

        Course course = new Course();
        course.setTitle(title);
        course.setDescription("Generated course for " + title);
        course.setCreator(auth.getName()); // user from JWT

        // Mock AI Generation - creating sample modules
        List<Module> modules = new ArrayList<>();
        for(int i=1; i<=3; i++) {
            Module m = new Module();
            m.setTitle("Module " + i + ": Intro to " + title);
            m.setCourse(course);
            modules.add(m);
        }
        course.setModules(modules);

        return courseRepo.save(course);
    }

    @GetMapping
    public List<Course> getMyCourses(Authentication auth) {
        return courseRepo.findByCreator(auth.getName());
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id) {
        return courseRepo.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }
}