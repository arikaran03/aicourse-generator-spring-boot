package com.aicourse.scheduler;

import com.aicourse.service.courses.LessonService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LessonEnrichmentScheduler {

    private final LessonService lessonService;

    public LessonEnrichmentScheduler(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Scheduled(fixedRate = 60_000)
    public void enrichLessonsJob() {
        System.out.println(
                "[SCHEDULER] Triggered on thread: " +
                        Thread.currentThread().getName()
        );

//        lessonService.enrichPendingLessonsLimited(); // Temporarily commented out due to a high number of API calls when multiple courses are being created
    }
}