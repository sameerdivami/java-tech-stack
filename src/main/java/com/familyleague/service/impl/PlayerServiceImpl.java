package com.familyleague.service.impl;

import com.familyleague.dto.request.PlayerRequest;
import com.familyleague.dto.response.PlayerResponse;
import com.familyleague.entity.Player;
import com.familyleague.entity.Team;
import com.familyleague.exception.ResourceNotFoundException;
import com.familyleague.repository.PlayerRepository;
import com.familyleague.repository.TeamRepository;
import com.familyleague.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public PlayerResponse create(Long teamId, PlayerRequest request) {
        Team team = findTeam(teamId);
        Player player = Player.builder()
                .team(team)
                .name(request.getName())
                .role(request.getRole())
                .build();
        log.info("Adding player '{}' to team {}", request.getName(), teamId);
        return toResponse(playerRepository.save(player));
    }

    @Override
    @Transactional
    public PlayerResponse update(Long id, PlayerRequest request) {
        Player player = findById(id);
        player.setName(request.getName());
        player.setRole(request.getRole());
        return toResponse(playerRepository.save(player));
    }

    @Override
    public Page<PlayerResponse> getByTeam(Long teamId, Pageable pageable) {
        return playerRepository.findAllByTeamIdAndDeletedFalse(teamId, pageable)
                .map(this::toResponse);
    }

    @Override
    public PlayerResponse getById(Long id) {
        return toResponse(findById(id));
    }

    private Player findById(Long id) {
        return playerRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + id));
    }

    private Team findTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + teamId));
    }

    public PlayerResponse toResponse(Player p) {
        return PlayerResponse.builder()
                .id(p.getId())
                .teamId(p.getTeam().getId())
                .teamName(p.getTeam().getName())
                .name(p.getName())
                .role(p.getRole())
                .active(p.isActive())
                .build();
    }
}
