package com.aicourse.controller;

import com.aicourse.model.Lesson;
import com.aicourse.service.courses.impl.LessonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/courses")
public class LessonController {

    private static final Logger LOGGER = Logger.getLogger(LessonController.class.getName());

    @Autowired
    private LessonServiceImpl lessonServiceImpl;

    @PostMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/generate")
    public ResponseEntity<Lesson> generateLesson(@PathVariable Long courseId, @PathVariable Long moduleId,
                                                 @PathVariable Long lessonId) throws Exception {
        LOGGER.log(Level.INFO, "Request received to generate lesson ID: {0} for module ID: {1} in course ID: {2}",
                new Object[]{lessonId, moduleId, courseId});
        try {
            Lesson lesson = lessonServiceImpl.generateLessonContent(courseId, moduleId, lessonId);
            LOGGER.log(Level.INFO, "Lesson ID: {0} generated successfully", new Object[]{lessonId});
            return ResponseEntity.ok(lesson);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating lesson ID: {0}: {1}", new Object[]{lessonId, e.getMessage()});
            throw e;
        }
    }

    @GetMapping("/lessons/{id}")
    public ResponseEntity<Lesson> getLesson(@PathVariable Long id) throws Exception {
        LOGGER.log(Level.INFO, "Fetching lesson details for ID: {0}", new Object[]{id});
        try {
            Lesson lesson = lessonServiceImpl.getLesson(id);
            LOGGER.log(Level.INFO, "Lesson details retrieved for ID: {0}", new Object[]{id});
            return ResponseEntity.ok(lesson);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching lesson ID: {0}: {1}", new Object[]{id, e.getMessage()});
            throw e;
        }
    }
}
