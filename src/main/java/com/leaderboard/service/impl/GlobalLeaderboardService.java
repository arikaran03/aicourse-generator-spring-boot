package com.leaderboard.service.impl;

import com.leaderboard.dto.LeaderboardResponseDTO;
import com.leaderboard.dto.UserRankDTO;
import com.leaderboard.model.UserStats;
import com.leaderboard.repository.UserStatsRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GlobalLeaderboardService extends AbstractLeaderboardService {

    public GlobalLeaderboardService(UserStatsRepository userStatsRepository) {
        super(userStatsRepository);
    }

    @Override
    protected int getScore(UserStats user) {
        return user.getTotalPoints();
    }

    @Override
    public List<LeaderboardResponseDTO> getLeaderBorad() {
        return buildLeaderBoard(getTopGlobalUsers(0, 10));
    }

    @Override
    public UserRankDTO getUserRankDTO(Long userId) {
        List<UserStats> users = getTopGlobalUsers(0, 10);

        AtomicInteger rank = new AtomicInteger(1);

        for (UserStats user : users) {
            if (userId.equals(user.getUserId())) {
                return new UserRankDTO(
                        rank.get(),
                        user.getUserId(),
                        getScore(user)
                );
            }
            rank.incrementAndGet();
        }
        return null;
    }

    @Cacheable(value = "globalLeaderboard")
    public List<UserStats> getTopGlobalUsers(int page, int size) {
        return userStatsRepository
                .findGlobalLeaderboard(PageRequest.of(page, size))
                .getContent();
    }
}