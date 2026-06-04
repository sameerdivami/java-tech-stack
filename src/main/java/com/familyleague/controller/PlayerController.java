package com.familyleague.controller;

import com.familyleague.dto.request.PlayerRequest;
import com.familyleague.dto.response.ApiResponse;
import com.familyleague.dto.response.PlayerResponse;
import com.familyleague.service.PlayerService;
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

@RestController
@RequestMapping("/api/teams/{teamId}/players")
@RequiredArgsConstructor
@Tag(name = "Players")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    @Operation(summary = "List players for a team (paginated)")
    public ResponseEntity<ApiResponse<Page<PlayerResponse>>> getByTeam(
            @PathVariable Long teamId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(playerService.getByTeam(teamId, pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get player by ID")
    public ResponseEntity<ApiResponse<PlayerResponse>> getById(@PathVariable Long teamId,
                                                                @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(playerService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a player to a team (Admin only)")
    public ResponseEntity<ApiResponse<PlayerResponse>> create(@PathVariable Long teamId,
                                                               @Valid @RequestBody PlayerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Player added", playerService.create(teamId, request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a player (Admin only)")
    public ResponseEntity<ApiResponse<PlayerResponse>> update(@PathVariable Long teamId,
                                                               @PathVariable Long id,
                                                               @Valid @RequestBody PlayerRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Player updated", playerService.update(id, request)));
    }
}
