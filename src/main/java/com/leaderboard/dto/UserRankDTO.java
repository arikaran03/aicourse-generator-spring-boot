package com.leaderboard.dto;

public class UserRankDTO extends RankScoreDTO {
    public UserRankDTO(int rank, Long userId, int score) {
        this.rank = rank;
        this.userId = userId;
        this.score = score;
    }
}
