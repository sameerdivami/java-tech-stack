package com.familyleague.service.impl;

import com.familyleague.dto.request.LeagueRequest;
import com.familyleague.dto.response.LeagueResponse;
import com.familyleague.entity.League;
import com.familyleague.exception.DuplicateResourceException;
import com.familyleague.exception.ResourceNotFoundException;
import com.familyleague.repository.LeagueRepository;
import com.familyleague.service.LeagueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeagueServiceImpl implements LeagueService {

    private final LeagueRepository leagueRepository;

    @Override
    @Transactional
    public LeagueResponse create(LeagueRequest request) {
        if (leagueRepository.existsByNameAndDeletedFalse(request.getName())) {
            throw new DuplicateResourceException("League already exists: " + request.getName());
        }
        League league = League.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        log.info("Creating league: {}", request.getName());
        return toResponse(leagueRepository.save(league));
    }

    @Override
    @Transactional
    public LeagueResponse update(Long id, LeagueRequest request) {
        League league = findById(id);
        league.setName(request.getName());
        league.setDescription(request.getDescription());
        return toResponse(leagueRepository.save(league));
    }

    @Override
    public LeagueResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public Page<LeagueResponse> getAll(Pageable pageable) {
        return leagueRepository.findAllByDeletedFalse(pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        League league = findById(id);
        league.setDeleted(true);
        leagueRepository.save(league);
        log.info("Soft-deleted league id={}", id);
    }

    private League findById(Long id) {
        return leagueRepository.findById(id)
                .filter(l -> !l.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("League not found: " + id));
    }

    private LeagueResponse toResponse(League l) {
        return LeagueResponse.builder()
                .id(l.getId())
                .name(l.getName())
                .description(l.getDescription())
                .createdAt(l.getCreatedAt())
                .build();
    }
}
