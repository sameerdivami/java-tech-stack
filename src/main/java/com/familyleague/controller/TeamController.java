package com.familyleague.controller;

import com.familyleague.dto.request.TeamRequest;
import com.familyleague.dto.response.ApiResponse;
import com.familyleague.dto.response.TeamResponse;
import com.familyleague.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/api/teams")
    @Operation(summary = "List all teams (paginated)")
    public ResponseEntity<ApiResponse<Page<TeamResponse>>> getAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(teamService.getAll(pageable)));
    }

    @GetMapping("/api/teams/{id}")
    @Operation(summary = "Get team by ID")
    public ResponseEntity<ApiResponse<TeamResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(teamService.getById(id)));
    }

    @PostMapping("/api/teams")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a team (Admin only)")
    public ResponseEntity<ApiResponse<TeamResponse>> create(@Valid @RequestBody TeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Team created", teamService.create(request)));
    }

    @PutMapping("/api/teams/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a team (Admin only)")
    public ResponseEntity<ApiResponse<TeamResponse>> update(@PathVariable Long id,
                                                             @Valid @RequestBody TeamRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Team updated", teamService.update(id, request)));
    }

    @PostMapping("/api/seasons/{seasonId}/teams/{teamId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a team to a season (Admin only)")
    public ResponseEntity<ApiResponse<Void>> addTeamToSeason(@PathVariable Long seasonId,
                                                               @PathVariable Long teamId) {
        teamService.addTeamToSeason(seasonId, teamId);
        return ResponseEntity.ok(ApiResponse.ok("Team added to season", null));
    }

    @GetMapping("/api/seasons/{seasonId}/teams")
    @Operation(summary = "Get all teams in a season")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getTeamsBySeason(@PathVariable Long seasonId) {
        return ResponseEntity.ok(ApiResponse.ok(teamService.getTeamsBySeason(seasonId)));
    }
}
