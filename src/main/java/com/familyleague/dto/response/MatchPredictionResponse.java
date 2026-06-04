package com.familyleague.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchPredictionResponse {
    private Long id;
    private Long matchId;
    private Long userId;
    private String username;
    private TeamResponse predictedWinner;
    private TeamResponse predictedTossWinner;
    private PlayerResponse predictedPlayerOfMatch;
}
