package Jut1S.project.AwesomeJavaBot.service;

import Jut1S.project.AwesomeJavaBot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private Random random = new Random();

    private Map<Long, String> currentChallenges = new HashMap<>();
    private Map<Long, Integer> currentChallengeIndices = new HashMap<>(); // Добавили для отслеживания индексов слов

    private Map<String, List<String>> challengeOptions = new HashMap<>();

    public TelegramBot(BotConfig config) {
        this.config = config;
        initializeChallengeOptions();
    }

    private void initializeChallengeOptions() {
        challengeOptions.put("дом", Arrays.asList("house", "water", "building", "cat"));
        challengeOptions.put("дерево", Arrays.asList("tree", "flag", "forest", "bird"));
        challengeOptions.put("кот", Arrays.asList("cat", "dog", "cucumber", "lamb"));
        // Добавьте другие слова и варианты ответов по аналогии
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleTextMessage(Update update) {
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        switch (messageText) {
            case "/start":
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                break;
            case "/help":
                sendMessage(chatId, "Привет, я Telegram bot. Вот несколько команд, которые ты можешь использовать: " +
                        "\n /help - вывести информацию о командах " +
                        "\n /start - показать стартовое сообщение " +
                        "\n /time - показать время" +
                        "\n /challenge - перевести слово на английский");
                break;
            case "/time":
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uu HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                sendMessage(chatId, dtf.format(now));
                break;
            case "/challenge":

                break;
            case "/endchallenge":
                endChallenge(chatId);
                break;
            default:
                sendMessage(chatId, "Извините, команда не распознана");
        }
    }

    private void endChallenge(long chatId) {
        currentChallenges.remove(chatId);
        currentChallengeIndices.remove(chatId);
        sendMessage(chatId, "Вы завершили вызов. Можете начать новый вызов с командой /challenge.");
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        if (data.equals("correct")) {
            sendMessage(chatId, "Правильно! Отличная работа!");

        } else if (data.equals("incorrect")) {
            sendMessage(chatId, "Увы, неправильно. Попробуйте еще раз.");
            // Не отправляем новые варианты, а повторно отправляем текущий вызов
            String currentChallenge = currentChallenges.get(chatId);
            sendChallengeOptions(chatId, currentChallenge);
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Привет, " + name + ", приятно познакомиться!";
        sendMessage(chatId, answer);
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



    private void sendChallengeOptions(long chatId, String challenge) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите правильный перевод слова '" + challenge + "':");
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<String> answerOptions = getAnswerOptions(challenge);

        List<InlineKeyboardButton> topRow = new ArrayList<>();
        List<InlineKeyboardButton> bottomRow = new ArrayList<>();

        for (String option : answerOptions) {
            if (!option.equals(challenge)) { // Исключаем угадываемое слово
                InlineKeyboardButton answerButton = new InlineKeyboardButton();
                answerButton.setText(option);
                answerButton.setCallbackData(option.equals(getCorrectTranslation(challenge)) ? "correct" : "incorrect");

                if (topRow.size() < 2) {
                    topRow.add(answerButton);
                } else {
                    bottomRow.add(answerButton);
                }
            }
        }

        rowsInLine.add(topRow);
        rowsInLine.add(bottomRow);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private List<String> getAnswerOptions(String correctAnswer) {
        List<String> options = new ArrayList<>();
        options.add(correctAnswer);

        if (challengeOptions.containsKey(correctAnswer)) {
            options.addAll(challengeOptions.get(correctAnswer));
        }

        // Перемешиваем варианты ответов
        Collections.shuffle(options);

        return options;
    }

    private String getCorrectTranslation(String word) {
        return challengeOptions.get(word).get(0);
    }
}