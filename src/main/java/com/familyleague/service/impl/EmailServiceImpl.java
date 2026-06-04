package com.familyleague.service.impl;

import com.familyleague.entity.EmailLog;
import com.familyleague.enums.EmailStatus;
import com.familyleague.enums.EmailType;
import com.familyleague.repository.EmailLogRepository;
import com.familyleague.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Override
    @Async
    public void sendEmail(String to, String subject, String body, EmailType type) {
        EmailLog logEntry = EmailLog.builder()
                .toAddress(to)
                .subject(subject)
                .body(body)
                .type(type)
                .status(EmailStatus.PENDING)
                .build();
        emailLogRepository.save(logEntry);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            logEntry.setStatus(EmailStatus.SENT);
            logEntry.setSentAt(LocalDateTime.now());
            log.info("Email sent to {} | type={}", to, type);
        } catch (Exception ex) {
            logEntry.setStatus(EmailStatus.FAILED);
            logEntry.setFailureReason(ex.getMessage());
            log.error("Failed to send email to {} | reason={}", to, ex.getMessage());
        }
        emailLogRepository.save(logEntry);
    }

    @Override
    @Async
    public void sendBulk(List<String> toList, String subject, String body, EmailType type) {
        toList.forEach(to -> sendEmail(to, subject, body, type));
    }
}
