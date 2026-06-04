package com.familyleague.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MatchResponse {
    private Long id;
    private Long seasonId;
    private String seasonName;
    private TeamResponse team1;
    private TeamResponse team2;
    private LocalDateTime scheduledAt;
    private LocalDateTime lockAt;
    private String venue;
    private String status;
    private Integer matchNumber;
}
