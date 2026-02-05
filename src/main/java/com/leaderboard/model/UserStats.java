package com.leaderboard.model;

import com.aicourse.model.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "userstats")
public class UserStats {

    @Id
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userstateId;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private Integer totalPoints;

    @Column(nullable = false)
    private Integer weeklyPoints;

    @Column(nullable = false)
    private Integer coursesCompleted;

    @Column(nullable = false)
    private Integer lessonsCompleted;

    @Column(nullable = false)
    private Integer currentStreak;

    protected UserStats(){}

    public UserStats(Long userId) {
        this.userId = userId;
        this.totalPoints = 0;
        this.weeklyPoints = 0;
        this.coursesCompleted = 0;
        this.lessonsCompleted = 0;
        this.currentStreak = 0;
    }

    public void addPoints(int points) {
        this.totalPoints += points;
        this.weeklyPoints += points;
    }

    public void incrementCoursesCompleted() {
        this.coursesCompleted++;
    }

    public void incrementLessonsCompleted() {
        this.lessonsCompleted++;
    }

    public void incrementStreak() {
        this.currentStreak++;
    }

    public void resetWeeklyPoints() {
        this.weeklyPoints = 0;
    }

    public void resetStreak() {
        this.currentStreak = 0;
    }



    public Long getUserstateid() {
        return userstateId;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public Integer getLessonsCompleted() {
        return lessonsCompleted;
    }

    public Integer getCoursesCompleted() {
        return coursesCompleted;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public Integer getWeeklyPoints() {
        return weeklyPoints;
    }
}
