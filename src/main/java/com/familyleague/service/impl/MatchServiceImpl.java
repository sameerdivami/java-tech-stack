package com.familyleague.service.impl;

import com.familyleague.dto.request.MatchRequest;
import com.familyleague.dto.response.MatchResponse;
import com.familyleague.dto.response.TeamResponse;
import com.familyleague.entity.Match;
import com.familyleague.entity.Season;
import com.familyleague.entity.Team;
import com.familyleague.enums.MatchStatus;
import com.familyleague.enums.SeasonStatus;
import com.familyleague.exception.ResourceNotFoundException;
import com.familyleague.exception.SeasonClosedException;
import com.familyleague.repository.MatchRepository;
import com.familyleague.repository.SeasonRepository;
import com.familyleague.repository.TeamRepository;
import com.familyleague.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final SeasonRepository seasonRepository;
    private final TeamRepository teamRepository;

    @Value("${app.prediction.match-lock-minutes}")
    private int matchLockMinutes;

    @Value("${app.prediction.league-lock-hours}")
    private int leagueLockHours;

    @Override
    @Transactional
    public MatchResponse create(Long seasonId, MatchRequest request) {
        Season season = findSeason(seasonId);
        if (season.getStatus() == SeasonStatus.CLOSED) {
            throw new SeasonClosedException("Cannot add matches to a closed season");
        }

        Team team1 = findTeam(request.getTeam1Id());
        Team team2 = findTeam(request.getTeam2Id());

        LocalDateTime lockAt = request.getScheduledAt().minusMinutes(matchLockMinutes);

        Match match = Match.builder()
                .season(season)
                .team1(team1)
                .team2(team2)
                .scheduledAt(request.getScheduledAt())
                .lockAt(lockAt)
                .venue(request.getVenue())
                .matchNumber(request.getMatchNumber())
                .status(MatchStatus.UPCOMING)
                .build();

        Match saved = matchRepository.save(match);
        updateSeasonFirstMatch(season, request.getScheduledAt());
        log.info("Match created: {} vs {} at {}", team1.getName(), team2.getName(), request.getScheduledAt());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public MatchResponse update(Long id, MatchRequest request) {
        Match match = findById(id);
        if (match.getStatus() != MatchStatus.UPCOMING) {
            throw new IllegalStateException("Only UPCOMING matches can be updated");
        }
        Team team1 = findTeam(request.getTeam1Id());
        Team team2 = findTeam(request.getTeam2Id());
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setScheduledAt(request.getScheduledAt());
        match.setLockAt(request.getScheduledAt().minusMinutes(matchLockMinutes));
        match.setVenue(request.getVenue());
        match.setMatchNumber(request.getMatchNumber());
        updateSeasonFirstMatch(match.getSeason(), request.getScheduledAt());
        return toResponse(matchRepository.save(match));
    }

    @Override
    public MatchResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public Page<MatchResponse> getBySeason(Long seasonId, Pageable pageable) {
        return matchRepository.findAllBySeasonIdAndDeletedFalse(seasonId, pageable)
                .map(this::toResponse);
    }

    // Keeps season.firstMatchAt in sync whenever a match is added or rescheduled
    private void updateSeasonFirstMatch(Season season, LocalDateTime newMatchTime) {
        if (season.getFirstMatchAt() == null || newMatchTime.isBefore(season.getFirstMatchAt())) {
            season.setFirstMatchAt(newMatchTime);
            season.setLeaguePredictionLockAt(newMatchTime.minusHours(leagueLockHours));
            seasonRepository.save(season);
        }
    }

    private Match findById(Long id) {
        return matchRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + id));
    }

    private Season findSeason(Long id) {
        return seasonRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + id));
    }

    private Team findTeam(Long id) {
        return teamRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + id));
    }

    public MatchResponse toResponse(Match m) {
        return MatchResponse.builder()
                .id(m.getId())
                .seasonId(m.getSeason().getId())
                .seasonName(m.getSeason().getName())
                .team1(teamToResponse(m.getTeam1()))
                .team2(teamToResponse(m.getTeam2()))
                .scheduledAt(m.getScheduledAt())
                .lockAt(m.getLockAt())
                .venue(m.getVenue())
                .status(m.getStatus().name())
                .matchNumber(m.getMatchNumber())
                .build();
    }

    private TeamResponse teamToResponse(Team t) {
        return TeamResponse.builder()
                .id(t.getId())
                .name(t.getName())
                .shortName(t.getShortName())
                .logoUrl(t.getLogoUrl())
                .build();
    }
}
