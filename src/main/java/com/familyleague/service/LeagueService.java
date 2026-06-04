package com.familyleague.service;

import com.familyleague.dto.request.LeagueRequest;
import com.familyleague.dto.response.LeagueResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeagueService {
    LeagueResponse create(LeagueRequest request);
    LeagueResponse update(Long id, LeagueRequest request);
    LeagueResponse getById(Long id);
    Page<LeagueResponse> getAll(Pageable pageable);
    void delete(Long id);
}
