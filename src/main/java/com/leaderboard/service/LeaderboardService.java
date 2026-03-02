package com.leaderboard.service;

import com.leaderboard.dto.LeaderboardResponseDTO;
import com.leaderboard.dto.UserRankDTO;

import java.util.List;

public interface LeaderboardService {
    List<LeaderboardResponseDTO> getLeaderBorad() throws Exception;

    UserRankDTO getUserRankDTO(Long userId) throws Exception;
}