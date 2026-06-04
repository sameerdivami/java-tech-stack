package com.familyleague.controller;

import com.familyleague.dto.request.SeasonRequest;
import com.familyleague.dto.response.ApiResponse;
import com.familyleague.dto.response.SeasonResponse;
import com.familyleague.service.SeasonService;
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
@RequestMapping("/api/leagues/{leagueId}/seasons")
@RequiredArgsConstructor
@Tag(name = "Seasons")
public class SeasonController {

    private final SeasonService seasonService;

    @GetMapping
    @Operation(summary = "List seasons for a league (paginated)")
    public ResponseEntity<ApiResponse<Page<SeasonResponse>>> getByLeague(
            @PathVariable Long leagueId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(seasonService.getByLeague(leagueId, pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get season by ID")
    public ResponseEntity<ApiResponse<SeasonResponse>> getById(@PathVariable Long leagueId,
                                                                @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(seasonService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a season (Admin only)")
    public ResponseEntity<ApiResponse<SeasonResponse>> create(@PathVariable Long leagueId,
                                                               @Valid @RequestBody SeasonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Season created", seasonService.create(leagueId, request)));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate a season (Admin only)")
    public ResponseEntity<ApiResponse<SeasonResponse>> activate(@PathVariable Long leagueId,
                                                                 @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Season activated", seasonService.activate(id)));
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Close a season (Admin only) — irreversible")
    public ResponseEntity<ApiResponse<SeasonResponse>> close(@PathVariable Long leagueId,
                                                              @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Season closed", seasonService.close(id)));
    }
}
