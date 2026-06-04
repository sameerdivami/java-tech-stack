package com.familyleague.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchRequest {

    @NotNull
    private Long team1Id;

    @NotNull
    private Long team2Id;

    @NotNull
    @Future
    private LocalDateTime scheduledAt;

    private String venue;

    private Integer matchNumber;
}
