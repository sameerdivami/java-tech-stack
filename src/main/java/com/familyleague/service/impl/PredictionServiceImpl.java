package com.familyleague.service.impl;

import com.familyleague.dto.request.LeaguePredictionRequest;
import com.familyleague.dto.request.MatchPredictionRequest;
import com.familyleague.dto.response.LeaguePredictionResponse;
import com.familyleague.dto.response.MatchPredictionResponse;
import com.familyleague.dto.response.PlayerResponse;
import com.familyleague.dto.response.TeamResponse;
import com.familyleague.entity.*;
import com.familyleague.enums.SeasonStatus;
import com.familyleague.exception.PredictionLockedException;
import com.familyleague.exception.ResourceNotFoundException;
import com.familyleague.exception.SeasonClosedException;
import com.familyleague.repository.*;
import com.familyleague.service.PredictionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {

    private final MatchPredictionRepository matchPredictionRepository;
    private final LeaguePredictionRepository leaguePredictionRepository;
    private final MatchRepository matchRepository;
    private final SeasonRepository seasonRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    // ── Match Predictions ──────────────────────────────────────────────────

    @Override
    @Transactional
    public MatchPredictionResponse submitMatchPrediction(Long matchId, MatchPredictionRequest request, String username) {
        Match match = findMatch(matchId);
        enforcMatchLock(match);

        User user = findUser(username);

        // Upsert: update existing prediction or create new one
        MatchPrediction prediction = matchPredictionRepository
                .findByMatchIdAndUserIdAndDeletedFalse(matchId, user.getId())
                .orElse(MatchPrediction.builder().match(match).user(user).build());

        if (request.getPredictedWinnerId() != null) {
            prediction.setPredictedWinner(findTeam(request.getPredictedWinnerId()));
        }
        if (request.getPredictedTossWinnerId() != null) {
            prediction.setPredictedTossWinner(findTeam(request.getPredictedTossWinnerId()));
        }
        if (request.getPredictedPlayerOfMatchId() != null) {
            prediction.setPredictedPlayerOfMatch(findPlayer(request.getPredictedPlayerOfMatchId()));
        }

        log.info("Match prediction submitted by {} for match {}", username, matchId);
        return toMatchPredictionResponse(matchPredictionRepository.save(prediction));
    }

    @Override
    public MatchPredictionResponse getMyMatchPrediction(Long matchId, String username) {
        User user = findUser(username);
        MatchPrediction prediction = matchPredictionRepository
                .findByMatchIdAndUserIdAndDeletedFalse(matchId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No prediction found for this match"));
        return toMatchPredictionResponse(prediction);
    }

    @Override
    public List<MatchPredictionResponse> getMatchPredictions(Long matchId) {
        Match match = findMatch(matchId);
        // Predictions visible only after lock
        if (LocalDateTime.now().isBefore(match.getLockAt())) {
            throw new PredictionLockedException("Predictions are visible only after the prediction window closes");
        }
        return matchPredictionRepository.findAllByMatchIdAndDeletedFalse(matchId)
                .stream().map(this::toMatchPredictionResponse).collect(Collectors.toList());
    }

    // ── League Predictions ─────────────────────────────────────────────────

    @Override
    @Transactional
    public LeaguePredictionResponse submitLeaguePrediction(Long seasonId, LeaguePredictionRequest request, String username) {
        Season season = findSeason(seasonId);
        enforceLeagueLock(season);

        User user = findUser(username);

        // Delete existing predictions for this user+season before saving fresh ones
        List<LeaguePrediction> existing =
                leaguePredictionRepository.findAllBySeasonIdAndUserIdAndDeletedFalse(seasonId, user.getId());
        existing.forEach(p -> p.setDeleted(true));
        leaguePredictionRepository.saveAll(existing);

        for (LeaguePredictionRequest.TeamRankEntry entry : request.getRankings()) {
            Team team = findTeam(entry.getTeamId());
            LeaguePrediction prediction = LeaguePrediction.builder()
                    .season(season).user(user).team(team)
                    .predictedPosition(entry.getPosition())
                    .build();
            leaguePredictionRepository.save(prediction);
        }

        log.info("League prediction submitted by {} for season {}", username, seasonId);
        return buildLeaguePredictionResponse(seasonId, user);
    }

    @Override
    public LeaguePredictionResponse getMyLeaguePrediction(Long seasonId, String username) {
        User user = findUser(username);
        return buildLeaguePredictionResponse(seasonId, user);
    }

    @Override
    public List<LeaguePredictionResponse> getLeaguePredictions(Long seasonId) {
        Season season = findSeason(seasonId);
        // Visible only after league prediction lock
        if (season.getLeaguePredictionLockAt() != null &&
                LocalDateTime.now().isBefore(season.getLeaguePredictionLockAt())) {
            throw new PredictionLockedException("League predictions are visible only after the prediction window closes");
        }
        List<LeaguePrediction> all = leaguePredictionRepository.findAllBySeasonIdAndDeletedFalse(seasonId);
        return all.stream()
                .collect(Collectors.groupingBy(p -> p.getUser().getId()))
                .values().stream()
                .map(predictions -> {
                    User user = predictions.get(0).getUser();
                    return toLeaguePredictionResponse(user, predictions);
                }).collect(Collectors.toList());
    }

    // ── Lock enforcement ───────────────────────────────────────────────────

    private void enforcMatchLock(Match match) {
        if (LocalDateTime.now().isAfter(match.getLockAt())) {
            throw new PredictionLockedException("Prediction window for this match is closed");
        }
    }

    private void enforceLeagueLock(Season season) {
        if (season.getStatus() == SeasonStatus.CLOSED) {
            throw new SeasonClosedException("This season is closed");
        }
        if (season.getLeaguePredictionLockAt() != null &&
                LocalDateTime.now().isAfter(season.getLeaguePredictionLockAt())) {
            throw new PredictionLockedException("League prediction window is closed");
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private LeaguePredictionResponse buildLeaguePredictionResponse(Long seasonId, User user) {
        List<LeaguePrediction> predictions =
                leaguePredictionRepository.findAllBySeasonIdAndUserIdAndDeletedFalse(seasonId, user.getId());
        return toLeaguePredictionResponse(user, predictions);
    }

    private LeaguePredictionResponse toLeaguePredictionResponse(User user, List<LeaguePrediction> predictions) {
        List<LeaguePredictionResponse.TeamRankEntry> rankings = predictions.stream()
                .sorted(java.util.Comparator.comparingInt(LeaguePrediction::getPredictedPosition))
                .map(p -> LeaguePredictionResponse.TeamRankEntry.builder()
                        .teamId(p.getTeam().getId())
                        .teamName(p.getTeam().getName())
                        .position(p.getPredictedPosition())
                        .build())
                .collect(Collectors.toList());

        return LeaguePredictionResponse.builder()
                .seasonId(predictions.isEmpty() ? null : predictions.get(0).getSeason().getId())
                .userId(user.getId())
                .username(user.getUsername())
                .rankings(rankings)
                .build();
    }

    private MatchPredictionResponse toMatchPredictionResponse(MatchPrediction p) {
        return MatchPredictionResponse.builder()
                .id(p.getId())
                .matchId(p.getMatch().getId())
                .userId(p.getUser().getId())
                .username(p.getUser().getUsername())
                .predictedWinner(p.getPredictedWinner() != null ? teamToResponse(p.getPredictedWinner()) : null)
                .predictedTossWinner(p.getPredictedTossWinner() != null ? teamToResponse(p.getPredictedTossWinner()) : null)
                .predictedPlayerOfMatch(p.getPredictedPlayerOfMatch() != null ? playerToResponse(p.getPredictedPlayerOfMatch()) : null)
                .build();
    }

    private Match findMatch(Long id) {
        return matchRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + id));
    }

    private Season findSeason(Long id) {
        return seasonRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + id));
    }

    private User findUser(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private Team findTeam(Long id) {
        return teamRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + id));
    }

    private Player findPlayer(Long id) {
        return playerRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + id));
    }

    private TeamResponse teamToResponse(Team t) {
        return TeamResponse.builder().id(t.getId()).name(t.getName())
                .shortName(t.getShortName()).logoUrl(t.getLogoUrl()).build();
    }

    private PlayerResponse playerToResponse(Player p) {
        return PlayerResponse.builder().id(p.getId()).name(p.getName())
                .teamId(p.getTeam().getId()).teamName(p.getTeam().getName())
                .role(p.getRole()).active(p.isActive()).build();
    }
}
