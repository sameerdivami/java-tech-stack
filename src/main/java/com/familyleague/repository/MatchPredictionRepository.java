package com.familyleague.repository;

import com.familyleague.entity.MatchPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchPredictionRepository extends JpaRepository<MatchPrediction, Long> {
    Optional<MatchPrediction> findByMatchIdAndUserIdAndDeletedFalse(Long matchId, Long userId);
    List<MatchPrediction> findAllByMatchIdAndDeletedFalse(Long matchId);
    // Used to check which users have NOT predicted for a match (for reminder emails)
    List<MatchPrediction> findAllByUserIdAndDeletedFalse(Long userId);
}
