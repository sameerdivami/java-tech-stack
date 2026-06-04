package com.familyleague.dto.request;

import lombok.Data;

@Data
public class MatchPredictionRequest {
    private Long predictedWinnerId;
    private Long predictedTossWinnerId;
    private Long predictedPlayerOfMatchId;
}
