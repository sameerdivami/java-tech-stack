package com.familyleague.controller;

import com.familyleague.dto.response.ApiResponse;
import com.familyleague.dto.response.LeaderboardEntryResponse;
import com.familyleague.service.LeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons/{seasonId}/leaderboard")
@RequiredArgsConstructor
@Tag(name = "Leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping
    @Operation(summary = "Get leaderboard for a season")
    public ResponseEntity<ApiResponse<List<LeaderboardEntryResponse>>> getLeaderboard(
            @PathVariable Long seasonId) {
        return ResponseEntity.ok(ApiResponse.ok(leaderboardService.getLeaderboard(seasonId)));
    }
}
