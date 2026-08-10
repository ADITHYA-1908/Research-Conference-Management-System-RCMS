package com.rcms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendEmailNotification(String recipientEmail, String subject, String body) {
        log.info("[NOTIFICATION SENT] To: {} | Subject: {} | Body: {}", recipientEmail, subject, body);
    }
}
