package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.config.BotConfig;
import com.example.carsharingservice.model.User;
import com.example.carsharingservice.service.NotificationService;
import com.example.carsharingservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class TelegramService extends TelegramLongPollingBot
        implements NotificationService {
    private final BotConfig botConfig;
    private final UserService userService;

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
            if (matchPattern(messageText)) {
                User user = userService.findByEmail(messageText).get();
                user.setChatId(String.valueOf(update.getMessage().getChatId()));
                userService.update(user);
                return;
            }
            switch (messageText) {
                case "/start":
                    startCommandReceived(String.valueOf(update.getMessage().getChatId()),
                            update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage(String.valueOf(update.getMessage().getChatId()), "unknown command");
            }
        }
    }

    private void startCommandReceived(String chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        sendMessage(chatId, answer);
    }


    @Override
    public void sendMessage(String chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can`t send message due to error occured: ", e);
        }
    }

    private boolean matchPattern(String email) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
