package com.hostel.management.service;

/**
 * Sends verification and other emails.
 */
public interface EmailService {

    /**
     * Sends verification email to the user with a link containing the token.
     * If mail is not configured, logs the link to console for development.
     */
    void sendVerificationEmail(String toEmail, String name, String verificationToken);
}
