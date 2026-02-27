package com.leaderboard.model.impl;

import com.aicourse.repo.LessonRepo;
import com.leaderboard.model.UserStats;
import com.leaderboard.repository.UserStatsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserStatsService {

    private static final Logger LOGGER = Logger.getLogger(UserStatsService.class.getName());
    private static final int LESSON_POINTS = 10;
    private static final int COURSE_COMPLETION_BONUS = 50;

    @Autowired
    private UserStatsRepository userStatsRepository;

    @Autowired
    private LessonRepo lessonRepo;

    @Transactional
    public void recordLessonCompleted(Long userId, Long courseId) {

        UserStats stats = userStatsRepository.findByUserId(userId)
                .orElseGet(() -> userStatsRepository.save(new UserStats(userId)));

        stats.addPoints(LESSON_POINTS);
        stats.incrementLessonsCompleted();

        boolean allLessonsDone =
                lessonRepo.countUnenrichedLessonsByCourseId(courseId) == 0;

        if (allLessonsDone) {
            stats.addPoints(COURSE_COMPLETION_BONUS);
            stats.incrementCoursesCompleted();
        }

        userStatsRepository.save(stats);
    }
}
