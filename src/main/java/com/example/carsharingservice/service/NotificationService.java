package com.example.carsharingservice.service;

public interface NotificationService {
    void sendMessage(Long chatId, String messageToSend);
}
