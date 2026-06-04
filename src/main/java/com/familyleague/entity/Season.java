package com.familyleague.entity;

import com.familyleague.enums.SeasonStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seasons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Season extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeasonStatus status;

    // Start time of the earliest match in this season
    @Column(name = "first_match_at")
    private LocalDateTime firstMatchAt;

    // Auto-computed: firstMatchAt minus configured league-lock-hours
    @Column(name = "league_prediction_lock_at")
    private LocalDateTime leaguePredictionLockAt;
}
