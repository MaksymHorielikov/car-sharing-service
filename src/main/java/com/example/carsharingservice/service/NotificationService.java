package com.example.carsharingservice.service;

import com.example.carsharingservice.dto.response.RentalResponseDto;

public interface NotificationService {
    void sendMessage(String chatId, String textToSend);

    void sendMessageAboutNewRental(RentalResponseDto rental);
}
