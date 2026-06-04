package com.familyleague.scheduler;

import com.familyleague.entity.Match;
import com.familyleague.enums.MatchStatus;
import com.familyleague.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchLockScheduler {

    private final MatchRepository matchRepository;

    // Runs every minute; marks UPCOMING matches as LOCKED once their lockAt passes
    @Scheduled(fixedRateString = "${app.scheduler.lock-check-rate-ms:60000}")
    @Transactional
    public void lockExpiredMatches() {
        List<Match> upcoming = matchRepository.findAllByStatusAndDeletedFalse(MatchStatus.UPCOMING);
        LocalDateTime now = LocalDateTime.now();

        List<Match> toLock = upcoming.stream()
                .filter(m -> now.isAfter(m.getLockAt()))
                .toList();

        if (!toLock.isEmpty()) {
            toLock.forEach(m -> m.setStatus(MatchStatus.LOCKED));
            matchRepository.saveAll(toLock);
            log.info("Locked {} match(es)", toLock.size());
        }
    }
}
