package ru.max.external.services;

public interface EmailService {

    void sendMessage(String recipient, String subject, String text);
}
