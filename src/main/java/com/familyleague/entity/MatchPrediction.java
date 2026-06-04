package com.familyleague.entity;

import jakarta.persistence.*;
import lombok.*;

// One row per (user, match) — user predicts winner, toss winner, player of match
@Entity
@Table(name = "match_predictions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"match_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchPrediction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_winner_id")
    private Team predictedWinner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_toss_winner_id")
    private Team predictedTossWinner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_player_of_match_id")
    private Player predictedPlayerOfMatch;
}
