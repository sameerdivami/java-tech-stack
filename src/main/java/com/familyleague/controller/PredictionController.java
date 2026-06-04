package com.familyleague.controller;

import com.familyleague.dto.request.LeaguePredictionRequest;
import com.familyleague.dto.request.MatchPredictionRequest;
import com.familyleague.dto.response.ApiResponse;
import com.familyleague.dto.response.LeaguePredictionResponse;
import com.familyleague.dto.response.MatchPredictionResponse;
import com.familyleague.service.PredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Predictions")
public class PredictionController {

    private final PredictionService predictionService;

    // ── Match Predictions ──────────────────────────────────────────────────

    @PostMapping("/api/matches/{matchId}/predictions")
    @Operation(summary = "Submit or update match prediction (before lock)")
    public ResponseEntity<ApiResponse<MatchPredictionResponse>> submitMatchPrediction(
            @PathVariable Long matchId,
            @Valid @RequestBody MatchPredictionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok("Prediction saved",
                predictionService.submitMatchPrediction(matchId, request, userDetails.getUsername())));
    }

    @GetMapping("/api/matches/{matchId}/predictions/me")
    @Operation(summary = "Get my match prediction")
    public ResponseEntity<ApiResponse<MatchPredictionResponse>> getMyMatchPrediction(
            @PathVariable Long matchId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                predictionService.getMyMatchPrediction(matchId, userDetails.getUsername())));
    }

    @GetMapping("/api/matches/{matchId}/predictions")
    @Operation(summary = "Get all predictions for a match (visible after lock only)")
    public ResponseEntity<ApiResponse<List<MatchPredictionResponse>>> getMatchPredictions(
            @PathVariable Long matchId) {
        return ResponseEntity.ok(ApiResponse.ok(predictionService.getMatchPredictions(matchId)));
    }

    // ── League Predictions ─────────────────────────────────────────────────

    @PostMapping("/api/seasons/{seasonId}/predictions/league")
    @Operation(summary = "Submit or update league prediction (before lock)")
    public ResponseEntity<ApiResponse<LeaguePredictionResponse>> submitLeaguePrediction(
            @PathVariable Long seasonId,
            @Valid @RequestBody LeaguePredictionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok("League prediction saved",
                predictionService.submitLeaguePrediction(seasonId, request, userDetails.getUsername())));
    }

    @GetMapping("/api/seasons/{seasonId}/predictions/league/me")
    @Operation(summary = "Get my league prediction for a season")
    public ResponseEntity<ApiResponse<LeaguePredictionResponse>> getMyLeaguePrediction(
            @PathVariable Long seasonId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                predictionService.getMyLeaguePrediction(seasonId, userDetails.getUsername())));
    }

    @GetMapping("/api/seasons/{seasonId}/predictions/league")
    @Operation(summary = "Get all league predictions for a season (visible after lock only)")
    public ResponseEntity<ApiResponse<List<LeaguePredictionResponse>>> getLeaguePredictions(
            @PathVariable Long seasonId) {
        return ResponseEntity.ok(ApiResponse.ok(predictionService.getLeaguePredictions(seasonId)));
    }
}
