package com.familyleague.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class LeaguePredictionRequest {
    // User submits a full ranked list of teams for the season
    @NotNull
    private List<TeamRankEntry> rankings;

    @Data
    public static class TeamRankEntry {
        @NotNull
        private Long teamId;

        @NotNull
        @Min(1)
        private Integer position;
    }
}
