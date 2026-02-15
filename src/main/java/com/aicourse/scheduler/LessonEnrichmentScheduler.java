package com.aicourse.scheduler;

// @Component
// public class LessonEnrichmentScheduler {
//
// private static final Logger logger =
// LoggerFactory.getLogger(LessonEnrichmentScheduler.class);
//
// private final LessonService lessonService;
//
// public LessonEnrichmentScheduler(LessonService lessonService) {
// this.lessonService = lessonService;
// }
//
// @Scheduled(fixedRate = 60_000)
// public void enrichLessonsJob() {
// logger.debug("[SCHEDULER] Triggered on thread: {}",
// Thread.currentThread().getName());
//
// try {
// lessonService.enrichPendingLessonsLimited(); // Temporarily commented out due
// to a high number of API calls when multiple courses are being created
// } catch (Exception e) {
// logger.error("Job failed: {}", e.getMessage(), e);
// }
// }
// }