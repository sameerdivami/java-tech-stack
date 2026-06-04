package com.familyleague.service;

import com.familyleague.dto.response.LeaderboardEntryResponse;

import java.util.List;

public interface LeaderboardService {
    // Triggered async after a result is published
    void recalculateForMatch(Long matchId);
    List<LeaderboardEntryResponse> getLeaderboard(Long seasonId);
}
