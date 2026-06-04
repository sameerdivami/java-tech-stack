package com.familyleague.scheduler;

import com.familyleague.entity.Match;
import com.familyleague.entity.User;
import com.familyleague.enums.EmailType;
import com.familyleague.enums.MatchStatus;
import com.familyleague.repository.MatchPredictionRepository;
import com.familyleague.repository.MatchRepository;
import com.familyleague.repository.UserRepository;
import com.familyleague.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PredictionReminderScheduler {

    private final MatchRepository matchRepository;
    private final MatchPredictionRepository matchPredictionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.notification.reminder-before-lock-minutes}")
    private int reminderBeforeLockMinutes;

    // Runs every 5 minutes; sends reminders to users who haven't predicted
    @Scheduled(fixedRateString = "${app.scheduler.reminder-check-rate-ms:300000}")
    public void sendPredictionReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now;
        LocalDateTime windowEnd = now.plusMinutes(reminderBeforeLockMinutes);

        // Find UPCOMING matches whose lock falls within the next reminderBeforeLockMinutes
        List<Match> upcomingMatches = matchRepository
                .findAllByStatusAndLockAtBetweenAndDeletedFalse(MatchStatus.UPCOMING, windowStart, windowEnd);

        if (upcomingMatches.isEmpty()) return;

        List<User> allActiveUsers = userRepository.findAll().stream()
                .filter(u -> u.isActive() && !u.isDeleted())
                .toList();

        for (Match match : upcomingMatches) {
            Set<Long> predictedUserIds = matchPredictionRepository
                    .findAllByMatchIdAndDeletedFalse(match.getId())
                    .stream()
                    .map(p -> p.getUser().getId())
                    .collect(Collectors.toSet());

            List<String> unpredictedEmails = allActiveUsers.stream()
                    .filter(u -> !predictedUserIds.contains(u.getId()))
                    .map(User::getEmail)
                    .toList();

            if (!unpredictedEmails.isEmpty()) {
                String subject = "Reminder: Predict before lock — Match #" + match.getMatchNumber();
                String body = match.getTeam1().getName() + " vs " + match.getTeam2().getName() +
                        " locks in " + reminderBeforeLockMinutes + " minutes. Submit your prediction now!";
                emailService.sendBulk(unpredictedEmails, subject, body, EmailType.PREDICTION_REMINDER);
                log.info("Sent prediction reminders for match {} to {} user(s)",
                        match.getId(), unpredictedEmails.size());
            }
        }
    }
}
