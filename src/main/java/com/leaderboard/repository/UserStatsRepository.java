package com.leaderboard.repository;

import com.leaderboard.model.UserStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatsRepository extends JpaRepository<UserStats, Long> {

    Optional<UserStats> findByUserId(Long userId);

    @Query("""
        SELECT u FROM UserStats u
        ORDER BY u.totalPoints DESC
    """)
    Page<UserStats> findGlobalLeaderboard(Pageable pageable);

    @Query("""
        SELECT u FROM UserStats u
        ORDER BY u.weeklyPoints DESC
    """)
    Page<UserStats> findWeeklyLeaderboard(Pageable pageable);

    @Modifying
    @Query("""
        UPDATE UserStats u
        SET u.weeklyPoints = 0
    """)
    void resetWeeklyPoints();
}
