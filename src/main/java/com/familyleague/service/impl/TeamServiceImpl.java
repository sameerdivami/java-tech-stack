package com.familyleague.service.impl;

import com.familyleague.dto.request.TeamRequest;
import com.familyleague.dto.response.TeamResponse;
import com.familyleague.entity.Season;
import com.familyleague.entity.SeasonTeam;
import com.familyleague.entity.Team;
import com.familyleague.exception.DuplicateResourceException;
import com.familyleague.exception.ResourceNotFoundException;
import com.familyleague.repository.SeasonRepository;
import com.familyleague.repository.SeasonTeamRepository;
import com.familyleague.repository.TeamRepository;
import com.familyleague.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final SeasonTeamRepository seasonTeamRepository;

    @Override
    @Transactional
    public TeamResponse create(TeamRequest request) {
        if (teamRepository.existsByNameAndDeletedFalse(request.getName())) {
            throw new DuplicateResourceException("Team already exists: " + request.getName());
        }
        Team team = Team.builder()
                .name(request.getName())
                .shortName(request.getShortName())
                .logoUrl(request.getLogoUrl())
                .build();
        log.info("Creating team: {}", request.getName());
        return toResponse(teamRepository.save(team));
    }

    @Override
    @Transactional
    public TeamResponse update(Long id, TeamRequest request) {
        Team team = findById(id);
        team.setName(request.getName());
        team.setShortName(request.getShortName());
        team.setLogoUrl(request.getLogoUrl());
        return toResponse(teamRepository.save(team));
    }

    @Override
    public TeamResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public Page<TeamResponse> getAll(Pageable pageable) {
        return teamRepository.findAllByDeletedFalse(pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public void addTeamToSeason(Long seasonId, Long teamId) {
        if (seasonTeamRepository.existsBySeasonIdAndTeamIdAndDeletedFalse(seasonId, teamId)) {
            throw new DuplicateResourceException("Team already added to this season");
        }
        Season season = seasonRepository.findByIdAndDeletedFalse(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));
        Team team = findById(teamId);
        seasonTeamRepository.save(SeasonTeam.builder().season(season).team(team).build());
        log.info("Added team {} to season {}", teamId, seasonId);
    }

    @Override
    public List<TeamResponse> getTeamsBySeason(Long seasonId) {
        return seasonTeamRepository.findAllBySeasonIdAndDeletedFalse(seasonId)
                .stream()
                .map(st -> toResponse(st.getTeam()))
                .toList();
    }

    private Team findById(Long id) {
        return teamRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + id));
    }

    public TeamResponse toResponse(Team t) {
        return TeamResponse.builder()
                .id(t.getId())
                .name(t.getName())
                .shortName(t.getShortName())
                .logoUrl(t.getLogoUrl())
                .build();
    }
}
