package com.familyleague.repository;

import com.familyleague.entity.Season;
import com.familyleague.enums.SeasonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    Page<Season> findAllByLeagueIdAndDeletedFalse(Long leagueId, Pageable pageable);
    List<Season> findAllByStatusAndDeletedFalse(SeasonStatus status);
    Optional<Season> findByIdAndDeletedFalse(Long id);
}
