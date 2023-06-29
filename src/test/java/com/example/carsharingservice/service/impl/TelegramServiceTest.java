package com.example.carsharingservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.carsharingservice.config.BotConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TelegramServiceTest {
    @Mock
    private BotConfig botConfig;

    @InjectMocks
    private TelegramService telegramService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBotUsername() {
        String botUsername = "TestBot";
        when(botConfig.getName()).thenReturn(botUsername);

        String result = telegramService.getBotUsername();

        assertEquals(botUsername, result);
    }

    @Test
    void testGetBotToken() {
        String botToken = "123456789:ABCDEF";
        when(botConfig.getToken()).thenReturn(botToken);

        String result = telegramService.getBotToken();

        assertEquals(botToken, result);
    }
}