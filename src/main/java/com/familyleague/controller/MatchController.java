package com.familyleague.controller;

import com.familyleague.dto.request.MatchRequest;
import com.familyleague.dto.request.MatchResultRequest;
import com.familyleague.dto.response.ApiResponse;
import com.familyleague.dto.response.MatchResponse;
import com.familyleague.dto.response.MatchResultResponse;
import com.familyleague.service.MatchResultService;
import com.familyleague.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Matches")
public class MatchController {

    private final MatchService matchService;
    private final MatchResultService matchResultService;

    @GetMapping("/api/seasons/{seasonId}/matches")
    @Operation(summary = "List matches in a season (paginated)")
    public ResponseEntity<ApiResponse<Page<MatchResponse>>> getBySeason(
            @PathVariable Long seasonId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(matchService.getBySeason(seasonId, pageable)));
    }

    @GetMapping("/api/matches/{id}")
    @Operation(summary = "Get match by ID")
    public ResponseEntity<ApiResponse<MatchResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(matchService.getById(id)));
    }

    @PostMapping("/api/seasons/{seasonId}/matches")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Schedule a match in a season (Admin only)")
    public ResponseEntity<ApiResponse<MatchResponse>> create(@PathVariable Long seasonId,
                                                              @Valid @RequestBody MatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Match scheduled", matchService.create(seasonId, request)));
    }

    @PutMapping("/api/matches/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a match (Admin only)")
    public ResponseEntity<ApiResponse<MatchResponse>> update(@PathVariable Long id,
                                                              @Valid @RequestBody MatchRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Match updated", matchService.update(id, request)));
    }

    @PostMapping("/api/matches/{matchId}/result")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Publish match result (Admin only)")
    public ResponseEntity<ApiResponse<MatchResultResponse>> publishResult(
            @PathVariable Long matchId,
            @Valid @RequestBody MatchResultRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok("Result published",
                matchResultService.publish(matchId, request, userDetails.getUsername())));
    }

    @GetMapping("/api/matches/{matchId}/result")
    @Operation(summary = "Get published result for a match")
    public ResponseEntity<ApiResponse<MatchResultResponse>> getResult(@PathVariable Long matchId) {
        return ResponseEntity.ok(ApiResponse.ok(matchResultService.getByMatch(matchId)));
    }
}
