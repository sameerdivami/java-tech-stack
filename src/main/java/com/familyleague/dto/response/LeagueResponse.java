package com.familyleague.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LeagueResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
