package com.familyleague.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchResultRequest {

    // Null if the match is a tie
    private Long winnerTeamId;

    @NotNull
    private Boolean tie;

    private Long tossWinnerTeamId;

    private Long playerOfMatchId;
}
