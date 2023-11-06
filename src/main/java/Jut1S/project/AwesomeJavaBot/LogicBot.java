package Jut1S.project.AwesomeJavaBot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс `LogicBot` представляет собой бота для обработки текстовых сообщений и выполнения вызовов.
 * Он предоставляет функциональность для обработки команд `/start`, `/help`, `/time`, `/challenge` и `/endchallenge`.
 * Также класс управляет списком доступных вызовов и вариантами ответов.
 */
public class LogicBot {


    // Список текущих вызовов для каждого пользователя
    private Map<Long, String> currentChallenges = new HashMap<>();

    // Индексы текущих вызовов для каждого пользователя
    private Map<Long, Integer> currentChallengeIndices = new HashMap<>();

    // Словарь с вариантами ответов на вызовы
    private Map<String, List<String>> challengeOptions = new HashMap<>();

    /**
     * Конструктор класса `LogicBot`. Инициализирует список доступных вызовов и вариантов ответов.
     */
    public LogicBot() {
        initializeChallengeOptions();
    }

    /**
     * Инициализирует список доступных вызовов и их вариантов ответов.
     */
    private void initializeChallengeOptions() {
        challengeOptions.put("дом", Arrays.asList("house", "water", "building", "cat"));
        challengeOptions.put("дерево", Arrays.asList("tree", "flag", "forest", "bird"));
        challengeOptions.put("кот", Arrays.asList("cat", "dog", "cucumber", "lamb"));
        // Добавьте другие слова и варианты ответов по аналогии
    }

    /**
     * Обрабатывает текстовое сообщение от пользователя и выполняет соответствующее действие в зависимости от команды.
     *
     * @param chatId Идентификатор чата пользователя.
     * @param messageText Текстовое сообщение от пользователя.
     * @param name Имя пользователя.
     * @return Ответ на сообщение или информацию о доступных командах.
     */
    public String handleTextMessage(long chatId, String messageText, String name) {
        switch (messageText) {
            case "/start":
                return startCommandReceived(name);
            case "/help":
                return "Привет, я Telegram bot. Вот несколько команд, которые ты можешь использовать: " +
                        "\n /help - вывести информацию о командах " +
                        "\n /start - показать стартовое сообщение " +
                        "\n /time - показать время" +
                        "\n /challenge - перевести слово на английский";
            case "/time":
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uu HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                return dtf.format(now);
            case "/challenge":
                return sendChallengeOptions(chatId);
            case "/endchallenge":
                endChallenge(chatId);
                return "Вы завершили вызов. Можете начать новый вызов с командой /challenge.";
            default:
                return "Извините, команда не распознана";
        }
    }

    /**
     * Завершает текущий вызов для пользователя.
     *
     * @param chatId Идентификатор чата пользователя.
     */
    public void endChallenge(long chatId) {
        currentChallenges.remove(chatId);
        currentChallengeIndices.remove(chatId);
    }

    /**
     * Обрабатывает команду `/start` и возвращает приветственное сообщение.
     *
     * @param name Имя пользователя.
     * @return Приветственное сообщение для пользователя.
     */
    public String startCommandReceived(String name) {
        return "Привет, " + name + ", приятно познакомиться!";
    }

    /**
     * Отправляет пользователю вызов с вариантами ответов.
     *
     * @param chatId Идентификатор чата пользователя.
     * @return Текущий вызов для угадывания.
     */
    public String sendChallengeOptions(long chatId) {
        int challengeIndex = currentChallengeIndices.getOrDefault(chatId, 0);
        int totalChallenges = challengeOptions.size();

        if (totalChallenges == 0) {
            return "Список слов пуст.";
        }

        String challenge = getChallengeByIndex(challengeIndex);
        currentChallenges.put(chatId, challenge);
        currentChallengeIndices.put(chatId, challengeIndex);
        return "Выберите правильный перевод слова '" + challenge + "':";
    }

    /**
     * Обрабатывает callback-запрос от пользователя, связанный с текущим вызовом.
     *
     * @param chatId Идентификатор чата пользователя.
     * @param data Данные callback-запроса (например, "correct" или "incorrect").
     * @return Результат обработки callback-запроса.
     */
    public String handleCallbackQuery(long chatId, String data) {
        if (data.equals("correct")) {
            sendChallengeOptions(chatId);
            return "Правильно! Отличная работа!";
        } else if (data.equals("incorrect")) {
            String currentChallenge = currentChallenges.get(chatId);
            return "Увы, неправильно. Попробуйте еще раз. Вызов: " + currentChallenge;
        }
        return "Неверная команда.";
    }

    /**
     * Возвращает вызов по индексу из списка доступных вызовов.
     *
     * @param index Индекс вызова.
     * @return Слово для перевода в текущем вызове.
     */
    private String getChallengeByIndex(int index) {
        List<String> challenges = new ArrayList<>(challengeOptions.keySet());
        return challenges.get(index);
    }
    public Map<Long, String> getCurrentChallenges() {
        return currentChallenges;
    }
    public Map<Long, Integer> getCurrentChallengeIndices() {
        return currentChallengeIndices;
    }

}
