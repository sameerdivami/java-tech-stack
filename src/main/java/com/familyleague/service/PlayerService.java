package com.familyleague.service;

import com.familyleague.dto.request.PlayerRequest;
import com.familyleague.dto.response.PlayerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayerService {
    PlayerResponse create(Long teamId, PlayerRequest request);
    PlayerResponse update(Long id, PlayerRequest request);
    Page<PlayerResponse> getByTeam(Long teamId, Pageable pageable);
    PlayerResponse getById(Long id);
}
