package com.leaderboard.dto;

public abstract class RankScoreDTO {
    protected int rank;
    protected Long userId;
    protected int score;

    public int getRank() { return rank; }
    public Long getUserId() { return userId; }
    public int getScore() { return score; }
}
