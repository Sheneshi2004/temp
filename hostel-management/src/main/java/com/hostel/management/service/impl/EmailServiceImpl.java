package com.hostel.management.service.impl;

import com.hostel.management.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Sends verification emails. Falls back to console log if mail is not configured.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String baseUrl;
    private final String fromEmail;

    public EmailServiceImpl(
            @Value("${app.base-url:http://localhost:8080}") String baseUrl,
            @Value("${spring.mail.username:}") String fromEmail,
            @org.springframework.beans.factory.annotation.Autowired(required = false) JavaMailSender mailSender) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.fromEmail = fromEmail;
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String toEmail, String name, String verificationToken) {
        String verifyLink = baseUrl + "/verify.html?token=" + verificationToken;

        if (mailSender != null && fromEmail != null && !fromEmail.isBlank()) {
            try {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom(fromEmail);
                msg.setTo(toEmail);
                msg.setSubject("Verify your HostelHub account");
                msg.setText("Hi " + (name != null ? name : "there") + ",\n\n" +
                        "Please verify your email by clicking the link below:\n\n" +
                        verifyLink + "\n\n" +
                        "This link expires in 24 hours.\n\n" +
                        "If you did not register, please ignore this email.\n\n" +
                        "— HostelHub");
                mailSender.send(msg);
            } catch (Exception e) {
                System.err.println("Failed to send verification email: " + e.getMessage());
                logVerificationLink(toEmail, verifyLink);
            }
        } else {
            logVerificationLink(toEmail, verifyLink);
        }
    }

    private void logVerificationLink(String toEmail, String link) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("📧 [DEV] Verification email for " + toEmail);
        System.out.println("   → Open this link to verify: " + link);
        System.out.println("   Configure spring.mail.* to send real emails.");
        System.out.println("═══════════════════════════════════════════════════════");
    }
}
