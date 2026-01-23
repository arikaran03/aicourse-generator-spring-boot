package com.aicourse.service.courses.impl;

import com.aicourse.geminiConnection.geminiConnection;
import com.aicourse.model.Lesson;
import com.aicourse.model.Module;
import com.aicourse.repo.LessonRepo;
import com.aicourse.service.courses.LessonService;
import com.aicourse.utils.json.JsonParserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepo lessonRepo;

    @Autowired
    private geminiConnection geminiConnection;

    @Override
    @Transactional
    public Lesson generateLessonContent(Long courseId, Long moduleId, Long lessonId) {

        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() ->
                        new RuntimeException("Lesson not found with id: " + lessonId));

        Module module = lesson.getModule();

        if (!module.getId().equals(moduleId)) {
            throw new RuntimeException("Lesson " + lessonId + " does not belong to Module " + moduleId);
        }

        if (!module.getCourse().getId().equals(courseId)) {
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

        String response = geminiConnection.getResponse(prompt);
        String cleanJson = JsonParserUtil.extractRawJson(response);
        JsonNode contentJson = JsonParserUtil.parseStringToJsonObject(cleanJson);
        lesson.setContent(contentJson);


        lesson.setContent(contentJson);
        lesson.setEnriched(true);

        return lessonRepo.save(lesson);
    }

    @Override
    @Transactional
    public void enrichPendingLessonsLimited() {

        List<Lesson> lessons = lessonRepo.findNext2PendingLessons();

        if (lessons.isEmpty()) {
            return;
        }

        for (Lesson lesson : lessons) {
            try {
                Long courseId = lesson.getModule().getCourse().getId();
                Long moduleId = lesson.getModule().getId();
                Long lessonId = lesson.getId();

                generateLessonContent(courseId, moduleId, lessonId);
            } catch (Exception e) {
                // IMPORTANT: don't kill scheduler
                e.printStackTrace();
            }
        }
    }


    @Override
    public Lesson getLesson(Long lessonId) {
        return lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));
    }
}
