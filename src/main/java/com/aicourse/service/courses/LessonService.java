package com.aicourse.service.courses;

import com.aicourse.model.Lesson;

public interface LessonService {

    Lesson generateLessonContent(Long courseId, Long moduleId, Long lessonId) throws Exception;

    void enrichPendingLessonsLimited() throws Exception;

    Lesson getLesson(Long lessonId) throws Exception;

}
