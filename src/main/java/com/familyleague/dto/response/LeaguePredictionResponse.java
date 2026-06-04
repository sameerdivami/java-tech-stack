package com.familyleague.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LeaguePredictionResponse {
    private Long seasonId;
    private Long userId;
    private String username;
    private List<TeamRankEntry> rankings;

    @Data
    @Builder
    public static class TeamRankEntry {
        private Long teamId;
        private String teamName;
        private Integer position;
    }
}
