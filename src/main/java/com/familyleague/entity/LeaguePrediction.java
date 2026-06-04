package com.familyleague.entity;

import jakarta.persistence.*;
import lombok.*;

// One row per (user, season, team) — user ranks every team in the season
@Entity
@Table(name = "league_predictions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"season_id", "user_id", "team_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaguePrediction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // Position the user predicts for this team (1 = winner, 2 = runner-up, etc.)
    @Column(name = "predicted_position", nullable = false)
    private Integer predictedPosition;
}
