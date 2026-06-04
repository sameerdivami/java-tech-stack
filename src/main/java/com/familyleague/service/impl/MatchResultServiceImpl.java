package com.familyleague.service.impl;

import com.familyleague.dto.request.MatchResultRequest;
import com.familyleague.dto.response.MatchResultResponse;
import com.familyleague.dto.response.PlayerResponse;
import com.familyleague.dto.response.TeamResponse;
import com.familyleague.entity.*;
import com.familyleague.enums.EmailType;
import com.familyleague.enums.MatchStatus;
import com.familyleague.exception.DuplicateResourceException;
import com.familyleague.exception.ResourceNotFoundException;
import com.familyleague.repository.*;
import com.familyleague.service.EmailService;
import com.familyleague.service.LeaderboardService;
import com.familyleague.service.MatchResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchResultServiceImpl implements MatchResultService {

    private final MatchResultRepository matchResultRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final LeaderboardService leaderboardService;
    private final EmailService emailService;

    @Override
    @Transactional
    public MatchResultResponse publish(Long matchId, MatchResultRequest request, String adminUsername) {
        if (matchResultRepository.existsByMatchIdAndDeletedFalse(matchId)) {
            throw new DuplicateResourceException("Result already published for match: " + matchId);
        }

        Match match = matchRepository.findByIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + matchId));
        User admin = userRepository.findByUsernameAndDeletedFalse(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + adminUsername));

        Team winner = null;
        if (!Boolean.TRUE.equals(request.getTie()) && request.getWinnerTeamId() != null) {
            winner = teamRepository.findById(request.getWinnerTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Winner team not found"));
        }
        Team tossWinner = null;
        if (request.getTossWinnerTeamId() != null) {
            tossWinner = teamRepository.findById(request.getTossWinnerTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Toss winner team not found"));
        }
        Player pom = null;
        if (request.getPlayerOfMatchId() != null) {
            pom = playerRepository.findById(request.getPlayerOfMatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player not found"));
        }

        MatchResult result = MatchResult.builder()
                .match(match)
                .winnerTeam(winner)
                .tie(Boolean.TRUE.equals(request.getTie()))
                .tossWinnerTeam(tossWinner)
                .playerOfMatch(pom)
                .publishedBy(admin)
                .publishedAt(LocalDateTime.now())
                .build();

        matchResultRepository.save(result);
        match.setStatus(MatchStatus.COMPLETED);
        matchRepository.save(match);

        log.info("Result published for match {} by {}", matchId, adminUsername);

        // Async: recalculate points and notify admin
        leaderboardService.recalculateForMatch(matchId);
        notifyAdminResultPublished(match, adminUsername);

        return toResponse(result);
    }

    @Override
    public MatchResultResponse getByMatch(Long matchId) {
        MatchResult result = matchResultRepository.findByMatchIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found for match: " + matchId));
        return toResponse(result);
    }

    private void notifyAdminResultPublished(Match match, String adminUsername) {
        userRepository.findByUsernameAndDeletedFalse(adminUsername).ifPresent(admin -> {
            String subject = "Result Published: Match #" + match.getMatchNumber();
            String body = "Result for " + match.getTeam1().getName() + " vs " +
                    match.getTeam2().getName() + " has been published. Leaderboard recalculation is in progress.";
            emailService.sendEmail(admin.getEmail(), subject, body, EmailType.RESULT_UPDATE_ALERT);
        });
    }

    private MatchResultResponse toResponse(MatchResult r) {
        return MatchResultResponse.builder()
                .id(r.getId())
                .matchId(r.getMatch().getId())
                .winnerTeam(r.getWinnerTeam() != null ? teamToResponse(r.getWinnerTeam()) : null)
                .tie(r.isTie())
                .tossWinnerTeam(r.getTossWinnerTeam() != null ? teamToResponse(r.getTossWinnerTeam()) : null)
                .playerOfMatch(r.getPlayerOfMatch() != null ? playerToResponse(r.getPlayerOfMatch()) : null)
                .publishedBy(r.getPublishedBy().getUsername())
                .publishedAt(r.getPublishedAt())
                .build();
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
