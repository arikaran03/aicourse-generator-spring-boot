package com.leaderboard.dto;

public class LeaderboardResponseDTO {

    private int rank;
    private Long userId;
    private int score;

    public LeaderboardResponseDTO(int rank, Long userId, int score) {
        this.rank = rank;
        this.userId = userId;
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public Long getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }

}
