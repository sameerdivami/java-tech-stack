package com.familyleague.service.impl;

import com.familyleague.dto.response.LeaderboardEntryResponse;
import com.familyleague.entity.Match;
import com.familyleague.entity.MatchPrediction;
import com.familyleague.entity.MatchResult;
import com.familyleague.entity.User;
import com.familyleague.exception.ResourceNotFoundException;
import com.familyleague.repository.*;
import com.familyleague.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {

    private final MatchRepository matchRepository;
    private final MatchResultRepository matchResultRepository;
    private final MatchPredictionRepository matchPredictionRepository;
    private final SeasonRepository seasonRepository;

    @Override
    @Async
    @Transactional
    public void recalculateForMatch(Long matchId) {
        log.info("Recalculating points for match {}", matchId);

        MatchResult result = matchResultRepository.findByMatchIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found for match: " + matchId));

        List<MatchPrediction> predictions =
                matchPredictionRepository.findAllByMatchIdAndDeletedFalse(matchId);

        for (MatchPrediction prediction : predictions) {
            int points = computePoints(prediction, result);
            log.debug("User {} earned {} point(s) for match {}", prediction.getUser().getUsername(), points, matchId);
            // Points are logged here; leaderboard is computed on-the-fly in getLeaderboard()
        }

        log.info("Point recalculation complete for match {}", matchId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaderboardEntryResponse> getLeaderboard(Long seasonId) {
        seasonRepository.findByIdAndDeletedFalse(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        // Load all completed matches for the season
        List<Match> completedMatches = matchRepository.findAllBySeasonIdAndDeletedFalse(seasonId,
                org.springframework.data.domain.Pageable.unpaged()).stream()
                .filter(m -> m.getStatus().name().equals("COMPLETED"))
                .toList();

        Map<Long, Integer> pointsByUser = new HashMap<>();
        Map<Long, User> userMap = new HashMap<>();

        for (Match match : completedMatches) {
            matchResultRepository.findByMatchIdAndDeletedFalse(match.getId()).ifPresent(result -> {
                List<MatchPrediction> predictions =
                        matchPredictionRepository.findAllByMatchIdAndDeletedFalse(match.getId());
                for (MatchPrediction prediction : predictions) {
                    int points = computePoints(prediction, result);
                    User user = prediction.getUser();
                    pointsByUser.merge(user.getId(), points, Integer::sum);
                    userMap.put(user.getId(), user);
                }
            });
        }

        // Sort descending by points, assign ranks
        List<Map.Entry<Long, Integer>> sorted = pointsByUser.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .toList();

        List<LeaderboardEntryResponse> board = new ArrayList<>();
        int rank = 1;
        for (Map.Entry<Long, Integer> entry : sorted) {
            User user = userMap.get(entry.getKey());
            board.add(LeaderboardEntryResponse.builder()
                    .rank(rank++)
                    .userId(user.getId())
                    .username(user.getUsername())
                    .avatarName(user.getAvatarName())
                    .totalPoints(entry.getValue())
                    .build());
        }
        return board;
    }

    // 1 point per correct prediction field
    // Tie rule: if result is a tie, user gets 1 point regardless of which team they picked
    private int computePoints(MatchPrediction prediction, MatchResult result) {
        int points = 0;

        // Winner prediction
        if (prediction.getPredictedWinner() != null) {
            if (result.isTie()) {
                // Both sides get 1 point on a tie
                points += 1;
            } else if (result.getWinnerTeam() != null &&
                    result.getWinnerTeam().getId().equals(prediction.getPredictedWinner().getId())) {
                points += 1;
            }
        }

        // Toss winner prediction
        if (prediction.getPredictedTossWinner() != null && result.getTossWinnerTeam() != null &&
                result.getTossWinnerTeam().getId().equals(prediction.getPredictedTossWinner().getId())) {
            points += 1;
        }

        // Player of the match prediction
        if (prediction.getPredictedPlayerOfMatch() != null && result.getPlayerOfMatch() != null &&
                result.getPlayerOfMatch().getId().equals(prediction.getPredictedPlayerOfMatch().getId())) {
            points += 1;
        }

        return points;
    }
}
