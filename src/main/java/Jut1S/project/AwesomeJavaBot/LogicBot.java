package Jut1S.project.AwesomeJavaBot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LogicBot {

    private Map<Long, String> currentChallenges = new HashMap<>();
    private Map<Long, Integer> currentChallengeIndices = new HashMap<>();
    private Map<String, List<String>> challengeOptions = new HashMap<>();

    public LogicBot() {
        initializeChallengeOptions();
    }

    private void initializeChallengeOptions() {
        challengeOptions.put("дом", Arrays.asList("house", "water", "building", "cat"));
        challengeOptions.put("дерево", Arrays.asList("tree", "flag", "forest", "bird"));
        challengeOptions.put("кот", Arrays.asList("cat", "dog", "cucumber", "lamb"));
        // Добавьте другие слова и варианты ответов по аналогии
    }

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

    public void endChallenge(long chatId) {
        currentChallenges.remove(chatId);
        currentChallengeIndices.remove(chatId);
    }

    public String startCommandReceived(String name) {
        return "Привет, " + name + ", приятно познакомиться!";
    }

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

    private String getChallengeByIndex(int index) {
        List<String> challenges = new ArrayList<>(challengeOptions.keySet());
        return challenges.get(index);
    }

    public List<String> getAnswerOptions(long chatId) {
        String correctAnswer = currentChallenges.get(chatId);
        List<String> options = new ArrayList<>();
        options.add(correctAnswer);

        if (challengeOptions.containsKey(correctAnswer)) {
            options.addAll(challengeOptions.get(correctAnswer));
        }

        // Перемешиваем варианты ответов
        Collections.shuffle(options);

        return options;
    }

    public boolean checkAnswer(long chatId, String selectedOption) {
        String correctAnswer = getCorrectTranslation(currentChallenges.get(chatId));
        return selectedOption.equals(correctAnswer);
    }

    String getCorrectTranslation(String word) {
        return challengeOptions.get(word).get(0);
    }

    public Map<Long, String> getCurrentChallenges() {
        return currentChallenges;
    }

    public Map<Long, Integer> getCurrentChallengeIndices() {
        return currentChallengeIndices;
    }
}
