package com.aicourse.controller;

import com.aicourse.model.Lesson;
import com.aicourse.service.courses.impl.LessonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class LessonController {

    @Autowired
    private LessonServiceImpl lessonServiceImpl;

    @PostMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/generate")
    public ResponseEntity<Lesson> generateLesson(@PathVariable Long courseId, @PathVariable Long moduleId, @PathVariable Long lessonId) {
        Lesson lesson = lessonServiceImpl.generateLessonContent(courseId, moduleId, lessonId);
        return ResponseEntity.ok(lesson);
    }

    @GetMapping("/lessons/{id}")
    public ResponseEntity<Lesson> getLesson(@PathVariable Long id) {
        return ResponseEntity.ok(lessonServiceImpl.getLesson(id));
    }
}
