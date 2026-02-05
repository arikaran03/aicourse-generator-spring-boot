package com.leaderboard.service.impl;

import com.leaderboard.dto.LeaderboardResponseDTO;
import com.leaderboard.model.UserStats;
import com.leaderboard.repository.UserStatsRepository;
import com.leaderboard.service.LeaderboardService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractLeaderboardService implements LeaderboardService{

    private final UserStatsRepository userStatsRepository;

    protected AbstractLeaderboardService(UserStatsRepository userStatsRepository) {
        this.userStatsRepository = userStatsRepository;
    }

    protected List<LeaderboardResponseDTO> buildLeaderBoard(List<UserStats> stats){

        AtomicInteger rank = new AtomicInteger(1);

        return stats.stream()
                .map(user -> new LeaderboardResponseDTO(
                        rank.getAndIncrement(),
                        user.getUserId(),
                        getScore(user)
                        )

                    ).toList();
    }
    protected abstract Integer getScore(UserStats user);
}