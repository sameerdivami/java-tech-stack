package com.familyleague.service;

import com.familyleague.dto.request.MatchResultRequest;
import com.familyleague.dto.response.MatchResultResponse;

public interface MatchResultService {
    MatchResultResponse publish(Long matchId, MatchResultRequest request, String adminUsername);
    MatchResultResponse getByMatch(Long matchId);
}
