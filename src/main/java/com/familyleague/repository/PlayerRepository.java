package com.familyleague.repository;

import com.familyleague.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Page<Player> findAllByTeamIdAndDeletedFalse(Long teamId, Pageable pageable);
    List<Player> findAllByTeamIdAndActiveAndDeletedFalse(Long teamId, boolean active);
}
