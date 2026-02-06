package com.leaderboard.service.impl;

import com.leaderboard.dto.LeaderboardResponseDTO;
import com.leaderboard.dto.UserRankDTO;
import com.leaderboard.model.UserStats;
import com.leaderboard.repository.UserStatsRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class GlobalLeaderboardService extends AbstractLeaderboardService {
    protected GlobalLeaderboardService(UserStatsRepository userStatsRepository) {
        super(userStatsRepository);
    }

    @Override
    protected Integer getScore(UserStats user) {
        return 0;
    }

    @Override
    public List<LeaderboardResponseDTO> getLeaderBorad() {
        return buildLeaderBoard(
                getTopGlobalUsers()
        );
    }

    @Override
    public UserRankDTO getUserRank(Long userId) {
        return null;
    }

    @Cacheable(value = "globalLeaderboard")
    public List<UserStats> getTopGlobalUsers() {
        return userStatsRepository
                .findGlobalLeaderboard(PageRequest.of(0, 10))
                .getContent();
    }

}
