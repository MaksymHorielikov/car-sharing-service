package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.config.BotConfig;
import com.example.carsharingservice.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class TelegramService extends TelegramLongPollingBot
        implements NotificationService {
    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            switch (messageText) {
                case "/start":
                    startCommandReceived(update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage("unknown command");
            }
        }
    }

    private void startCommandReceived(String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        sendMessage(answer);
    }

    @Override
    public void sendMessage(String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(botConfig.getChatId());
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can`t send message due to error occured: ", e);
        }
    }
}
