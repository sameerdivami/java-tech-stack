package com.familyleague.service;

import com.familyleague.dto.request.SeasonRequest;
import com.familyleague.dto.response.SeasonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SeasonService {
    SeasonResponse create(Long leagueId, SeasonRequest request);
    SeasonResponse getById(Long id);
    Page<SeasonResponse> getByLeague(Long leagueId, Pageable pageable);
    SeasonResponse activate(Long id);
    SeasonResponse close(Long id);
}
