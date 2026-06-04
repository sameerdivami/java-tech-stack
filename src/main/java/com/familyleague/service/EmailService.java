package com.familyleague.service;

import com.familyleague.enums.EmailType;

import java.util.List;

public interface EmailService {
    void sendEmail(String to, String subject, String body, EmailType type);
    void sendBulk(List<String> toList, String subject, String body, EmailType type);
}
