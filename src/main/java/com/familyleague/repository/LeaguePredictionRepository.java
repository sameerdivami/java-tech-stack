package com.familyleague.repository;

import com.familyleague.entity.LeaguePrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaguePredictionRepository extends JpaRepository<LeaguePrediction, Long> {
    List<LeaguePrediction> findAllBySeasonIdAndUserIdAndDeletedFalse(Long seasonId, Long userId);
    List<LeaguePrediction> findAllBySeasonIdAndDeletedFalse(Long seasonId);
    Optional<LeaguePrediction> findBySeasonIdAndUserIdAndTeamIdAndDeletedFalse(Long seasonId, Long userId, Long teamId);
    boolean existsBySeasonIdAndUserIdAndDeletedFalse(Long seasonId, Long userId);
}
