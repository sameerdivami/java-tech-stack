package com.familyleague.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaderboardEntryResponse {
    private Integer rank;
    private Long userId;
    private String username;
    private String avatarName;
    private Integer totalPoints;
}
