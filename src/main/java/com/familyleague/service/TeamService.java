package com.familyleague.service;

import com.familyleague.dto.request.TeamRequest;
import com.familyleague.dto.response.TeamResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeamService {
    TeamResponse create(TeamRequest request);
    TeamResponse update(Long id, TeamRequest request);
    TeamResponse getById(Long id);
    Page<TeamResponse> getAll(Pageable pageable);
    void addTeamToSeason(Long seasonId, Long teamId);
    List<TeamResponse> getTeamsBySeason(Long seasonId);
}
