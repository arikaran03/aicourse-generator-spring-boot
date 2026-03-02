package com.leaderboard.repository;

import com.leaderboard.model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStatsRepository extends JpaRepository<UserStats, Long> {

    Optional<UserStats> findByUserId(Long userId);

    @Query(value = """
                SELECT * FROM userstats
                ORDER BY total_points DESC
                LIMIT 10
            """, nativeQuery = true)
    List<UserStats> findGlobalLeaderboard();

    @Query(value = """
                SELECT * FROM userstats
                ORDER BY weekly_points DESC
                LIMIT 10
            """, nativeQuery = true)
    List<UserStats> findWeeklyLeaderboard();

    @Modifying
    @Query("""
        UPDATE UserStats u
        SET u.weeklyPoints = 0
    """)
    void resetWeeklyPoints();
}