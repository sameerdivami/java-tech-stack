package com.familyleague.repository;

import com.familyleague.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
    Optional<MatchResult> findByMatchIdAndDeletedFalse(Long matchId);
    boolean existsByMatchIdAndDeletedFalse(Long matchId);
}
