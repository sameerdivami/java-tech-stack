package com.familyleague.service;

import com.familyleague.dto.request.MatchRequest;
import com.familyleague.dto.response.MatchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchService {
    MatchResponse create(Long seasonId, MatchRequest request);
    MatchResponse update(Long id, MatchRequest request);
    MatchResponse getById(Long id);
    Page<MatchResponse> getBySeason(Long seasonId, Pageable pageable);
}
