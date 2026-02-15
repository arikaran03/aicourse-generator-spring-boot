package com.leaderboard.model.impl;

import com.leaderboard.service.LeaderboardService;

public class UserStatsService {

    private static LeaderboardService leaderboardService;

    public UserStatsService(LeaderboardService leaderboardService) {
        UserStatsService.leaderboardService = leaderboardService;
    }

//    public static UserRankDTO getUserRankDTO(UserStats user){
//
//        Integer score = leaderboardService.getScore(user);
//        Integer rank = leaderboardService.getRank(user);
//        return new UserRankDTO(rank, user.getUserId(), score);
//
//    }
}
