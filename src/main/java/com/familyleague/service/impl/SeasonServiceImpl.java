package com.familyleague.service.impl;

import com.familyleague.dto.request.SeasonRequest;
import com.familyleague.dto.response.SeasonResponse;
import com.familyleague.entity.League;
import com.familyleague.entity.Season;
import com.familyleague.enums.SeasonStatus;
import com.familyleague.exception.ResourceNotFoundException;
import com.familyleague.exception.SeasonClosedException;
import com.familyleague.repository.LeagueRepository;
import com.familyleague.repository.SeasonRepository;
import com.familyleague.service.SeasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {

    private final SeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;

    @Override
    @Transactional
    public SeasonResponse create(Long leagueId, SeasonRequest request) {
        League league = leagueRepository.findById(leagueId)
                .filter(l -> !l.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("League not found: " + leagueId));

        Season season = Season.builder()
                .league(league)
                .name(request.getName())
                .status(SeasonStatus.UPCOMING)
                .build();
        log.info("Creating season '{}' for league id={}", request.getName(), leagueId);
        return toResponse(seasonRepository.save(season));
    }

    @Override
    public SeasonResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public Page<SeasonResponse> getByLeague(Long leagueId, Pageable pageable) {
        return seasonRepository.findAllByLeagueIdAndDeletedFalse(leagueId, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional
    public SeasonResponse activate(Long id) {
        Season season = findById(id);
        if (season.getStatus() == SeasonStatus.CLOSED) {
            throw new SeasonClosedException("Cannot re-activate a closed season");
        }
        season.setStatus(SeasonStatus.ACTIVE);
        log.info("Activated season id={}", id);
        return toResponse(seasonRepository.save(season));
    }

    @Override
    @Transactional
    public SeasonResponse close(Long id) {
        Season season = findById(id);
        season.setStatus(SeasonStatus.CLOSED);
        log.info("Closed season id={}", id);
        return toResponse(seasonRepository.save(season));
    }

    private Season findById(Long id) {
        return seasonRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + id));
    }

    public SeasonResponse toResponse(Season s) {
        return SeasonResponse.builder()
                .id(s.getId())
                .leagueId(s.getLeague().getId())
                .leagueName(s.getLeague().getName())
                .name(s.getName())
                .status(s.getStatus().name())
                .firstMatchAt(s.getFirstMatchAt())
                .leaguePredictionLockAt(s.getLeaguePredictionLockAt())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
