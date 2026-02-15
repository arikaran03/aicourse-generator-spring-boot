package com.aicourse.service.courses.impl;

import com.aicourse.geminiConnection.GeminiConnection;
import com.aicourse.model.Lesson;
import com.aicourse.model.Module;
import com.aicourse.repo.LessonRepo;
import com.aicourse.service.courses.LessonService;
import com.aicourse.utils.json.JsonParserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LessonServiceImpl implements LessonService {

    private static final Logger LOGGER = Logger.getLogger(LessonServiceImpl.class.getName());

    @Autowired
    private LessonRepo lessonRepo;

    @Autowired
    private GeminiConnection geminiConnection;

    @Override
    @Transactional
    public Lesson generateLessonContent(Long courseId, Long moduleId, Long lessonId) throws Exception {
        LOGGER.log(Level.INFO, "Generating content for Lesson ID: {0} (Module ID: {1}, Course ID: {2})",
                new Object[]{lessonId, moduleId, courseId});

        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> {
                    LOGGER.log(Level.SEVERE, "Lesson not found with id: {0}", new Object[]{lessonId});
                    return new RuntimeException("Lesson not found with id: " + lessonId);
                });

        Module module = lesson.getModule();

        if (!module.getId().equals(moduleId)) {
            LOGGER.log(Level.SEVERE, "Lesson {0} does not belong to Module {1}", new Object[]{lessonId, moduleId});
            throw new RuntimeException("Lesson " + lessonId + " does not belong to Module " + moduleId);
        }

        if (!module.getCourse().getId().equals(courseId)) {
            LOGGER.log(Level.SEVERE, "Module {0} does not belong to Course {1}", new Object[]{moduleId, courseId});
            throw new RuntimeException("Module " + moduleId + " does not belong to Course " + courseId);
        }

        String courseTitle = module.getCourse().getTitle();
        String moduleTitle = module.getTitle();
        String lessonTitle = lesson.getTitle();

        String prompt = """
                Generate detailed content for a lesson titled "%s".
                Context:
                - Course: "%s"
                - Module: "%s"

                The content must be educational, engaging, and formatted as a JSON array.
                Each block must contain:
                - "type"
                - "content"

                Example:
                [
                    { "type": "text", "content": "Introduction..." },
                    { "type": "code", "content": "print('Hello')" }
                ]

                Respond ONLY with a raw JSON array.
                """.formatted(lessonTitle, courseTitle, moduleTitle);

        try {
            LOGGER.log(Level.FINE, "Sending prompt to AI for lesson ''{0}''", new Object[]{lessonTitle});
            String response = geminiConnection.getResponse(prompt);
            LOGGER.log(Level.FINE, "Received response from AI for lesson ''{0}''", new Object[]{lessonTitle});

            String cleanJson = JsonParserUtil.extractRawJson(response);
            JsonNode contentJson = JsonParserUtil.parseStringToJsonObject(cleanJson);

            lesson.setContent(contentJson);
            lesson.setEnriched(true);

            Lesson savedLesson = lessonRepo.save(lesson);

            LOGGER.log(Level.INFO, "Lesson ID: {0} content generated and saved successfully", new Object[]{lessonId});
            return savedLesson;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to generate content for lesson ID: {0}: {1}",
                    new Object[]{lessonId, e.getMessage()});
            throw e;
        }
    }

    @Override
    @Transactional
    public void enrichPendingLessonsLimited() throws Exception {
        LOGGER.log(Level.FINE, "Checking for pending lessons to enrich...");
        List<Lesson> lessons = lessonRepo.findNext2PendingLessons();

        if (lessons.isEmpty()) {
            LOGGER.log(Level.FINE, "No pending lessons found.");
            return;
        }

        LOGGER.log(Level.INFO, "Found {0} pending lessons. Starting enrichment...", new Object[]{lessons.size()});

        for (Lesson lesson : lessons) {
            try {
                Long courseId = lesson.getModule().getCourse().getId();
                Long moduleId = lesson.getModule().getId();
                Long lessonId = lesson.getId();

                LOGGER.log(Level.INFO, "Enriching Lesson ID: {0}...", new Object[]{lessonId});
                generateLessonContent(courseId, moduleId, lessonId);
            } catch (Exception e) {
                // IMPORTANT: don't kill scheduler
                LOGGER.log(Level.SEVERE, "Error enriching pending lesson ID: {0}: {1}",
                        new Object[]{lesson.getId(), e.getMessage()});
            }
        }
    }

    @Override
    public Lesson getLesson(Long lessonId) throws Exception {
        LOGGER.log(Level.FINE, "Retrieving lesson by ID: {0}", new Object[]{lessonId});
        return lessonRepo.findById(lessonId)
                .orElseThrow(() -> {
                    LOGGER.log(Level.SEVERE, "Lesson not found with id: {0}", new Object[]{lessonId});
                    return new RuntimeException("Lesson not found with id: " + lessonId);
                });
    }
}
