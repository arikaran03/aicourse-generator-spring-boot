package com.aicourse.service.courses;

import com.aicourse.model.Lesson;

public interface LessonService {

    Lesson generateLessonContent(Long courseId, Long moduleId, Long lessonId);

    void enrichPendingLessonsLimited();

    Lesson getLesson(Long lessonId);

}
