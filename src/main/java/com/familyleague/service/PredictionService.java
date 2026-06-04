package com.familyleague.service;

import com.familyleague.dto.request.LeaguePredictionRequest;
import com.familyleague.dto.request.MatchPredictionRequest;
import com.familyleague.dto.response.LeaguePredictionResponse;
import com.familyleague.dto.response.MatchPredictionResponse;

import java.util.List;

public interface PredictionService {
    MatchPredictionResponse submitMatchPrediction(Long matchId, MatchPredictionRequest request, String username);
    MatchPredictionResponse getMyMatchPrediction(Long matchId, String username);
    List<MatchPredictionResponse> getMatchPredictions(Long matchId);

    LeaguePredictionResponse submitLeaguePrediction(Long seasonId, LeaguePredictionRequest request, String username);
    LeaguePredictionResponse getMyLeaguePrediction(Long seasonId, String username);
    List<LeaguePredictionResponse> getLeaguePredictions(Long seasonId);
}
