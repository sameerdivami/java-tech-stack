package com.familyleague.controller;

import com.familyleague.dto.request.LeagueRequest;
import com.familyleague.dto.response.ApiResponse;
import com.familyleague.dto.response.LeagueResponse;
import com.familyleague.service.LeagueService;
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
@RequestMapping("/api/leagues")
@RequiredArgsConstructor
@Tag(name = "Leagues")
public class LeagueController {

    private final LeagueService leagueService;

    @GetMapping
    @Operation(summary = "List all leagues (paginated)")
    public ResponseEntity<ApiResponse<Page<LeagueResponse>>> getAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(leagueService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get league by ID")
    public ResponseEntity<ApiResponse<LeagueResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(leagueService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a league (Admin only)")
    public ResponseEntity<ApiResponse<LeagueResponse>> create(@Valid @RequestBody LeagueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("League created", leagueService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a league (Admin only)")
    public ResponseEntity<ApiResponse<LeagueResponse>> update(@PathVariable Long id,
                                                               @Valid @RequestBody LeagueRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("League updated", leagueService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft-delete a league (Admin only)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        leagueService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("League deleted", null));
    }
}
