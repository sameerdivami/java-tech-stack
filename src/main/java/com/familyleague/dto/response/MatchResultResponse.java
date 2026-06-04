package com.familyleague.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MatchResultResponse {
    private Long id;
    private Long matchId;
    private TeamResponse winnerTeam;
    private boolean tie;
    private TeamResponse tossWinnerTeam;
    private PlayerResponse playerOfMatch;
    private String publishedBy;
    private LocalDateTime publishedAt;
}
