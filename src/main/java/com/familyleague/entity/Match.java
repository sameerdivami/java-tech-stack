package com.familyleague.entity;

import com.familyleague.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team1_id", nullable = false)
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team2_id", nullable = false)
    private Team team2;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    // Enforced at DB level: scheduledAt minus configured match-lock-minutes
    @Column(name = "lock_at", nullable = false)
    private LocalDateTime lockAt;

    @Column(length = 200)
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchStatus status;

    @Column(name = "match_number")
    private Integer matchNumber;
}
