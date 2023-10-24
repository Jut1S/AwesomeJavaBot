package Jut1S.project.AwesomeJavaBot.service;


import Jut1S.project.AwesomeJavaBot.config.BotConfig;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TelegramBot(BotConfig config){
        this.config = config;
    }

    @Override
    public String getBotToken(){
        return config.getToken();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        var originalMessage = update.getMessage();
        System.out.println(originalMessage.getText());

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId, "Hello, I am a simple Java bot. Here are some commands you can use: " +
                            "\n /help - to show info about me and my commands " +
                            "\n /start - to show a start message " +
                            "\n /time - to show the current time" +
                            "\n /search [query] - to search on Wikipedia");
                    break;
                case "/time":
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uu HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    sendMessage(chatId, dtf.format(now));
                    break;
                default:
                    if (messageText.startsWith("/search ")) {
                        String query = messageText.substring("/search ".length());
                        String searchResult = performWikipediaSearch(query);
                        sendMessage(chatId, searchResult);
                    } else {
                        sendMessage(chatId, "Sorry, the command was not recognized");
                    }
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        sendMessage(chatId, answer);
    }

    private String performWikipediaSearch(String query) {
        try {
            return WikipediaService.search(query);
        } catch (JSONException e) {
            e.printStackTrace();
            return "An error occurred while searching on Wikipedia.";
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
