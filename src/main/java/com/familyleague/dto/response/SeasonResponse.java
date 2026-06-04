package com.familyleague.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SeasonResponse {
    private Long id;
    private Long leagueId;
    private String leagueName;
    private String name;
    private String status;
    private LocalDateTime firstMatchAt;
    private LocalDateTime leaguePredictionLockAt;
    private LocalDateTime createdAt;
}
