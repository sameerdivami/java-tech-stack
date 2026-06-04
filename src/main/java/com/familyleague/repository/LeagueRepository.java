package com.familyleague.repository;

import com.familyleague.entity.League;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    Page<League> findAllByDeletedFalse(Pageable pageable);
    boolean existsByNameAndDeletedFalse(String name);
}
