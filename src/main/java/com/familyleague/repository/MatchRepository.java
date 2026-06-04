package com.familyleague.repository;

import com.familyleague.entity.Match;
import com.familyleague.enums.MatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    Page<Match> findAllBySeasonIdAndDeletedFalse(Long seasonId, Pageable pageable);
    Optional<Match> findByIdAndDeletedFalse(Long id);
    List<Match> findAllByStatusAndDeletedFalse(MatchStatus status);
    // Used by scheduler to find matches whose lock window opens soon
    List<Match> findAllByStatusAndLockAtBetweenAndDeletedFalse(
            MatchStatus status, LocalDateTime from, LocalDateTime to);
}
