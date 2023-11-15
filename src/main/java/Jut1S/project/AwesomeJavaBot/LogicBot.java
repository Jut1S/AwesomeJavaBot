package Jut1S.project.AwesomeJavaBot;


import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogicBot {


    private Map<Long, String> currentChallenges = new HashMap<>();
    private Map<Long, Integer> currentChallengeIndices = new HashMap<>();

    private Map<String, List<String>> challengeOptions = new HashMap<>();

    public LogicBot() {
        initializeChallengeOptions();
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
                sendChallengeOptions(chatId);
            case "/endchallenge":
                endChallenge(chatId);
                return "Вы завершили вызов. Можете начать новый вызов с командой /challenge.";
            default:
                return "Извините, команда не распознана";
        }
    }

    private void initializeChallengeOptions() {
        challengeOptions.put("дом", Arrays.asList("house", "water", "building", "cat"));
        challengeOptions.put("дерево", Arrays.asList("tree", "flag", "forest", "bird"));
        challengeOptions.put("кот", Arrays.asList("cat", "dog", "cucumber", "lamb"));
        // Добавьте другие слова и варианты ответов по аналогии
    }

    public String startCommandReceived(String name) {
        return "Привет, " + name + ", приятно познакомиться!";
    }



    public void endChallenge(long chatId) {
        currentChallenges.remove(chatId);
        currentChallengeIndices.remove(chatId);
        System.out.println("Вы завершили вызов. Можете начать новый вызов с вызовом метода sendChallengeOptions.");
    }

    public void handleCallbackQuery(long chatId, String data) {
        String currentChallenge = currentChallenges.get(chatId);
        String correctTranslation = getCorrectTranslation(currentChallenge);

        if (data.equals(correctTranslation)) {
            System.out.println("correct");
            sendChallengeOptions(chatId);
        } else {
            System.out.println("incorrect");
            System.out.println("Неправильный ответ. Попробуйте еще раз.");
            // Повторно отправляем текущий вызов
            sendChallengeOptionsTwo(chatId, currentChallenge);
        }
    }

    public void sendChallengeOptionsTwo(long chatId, String challenge) {
        System.out.println("Выберите правильный перевод слова '" + challenge + "':");

        List<String> answerOptions = getAnswerOptions(challenge);

        for (String option : answerOptions) {
            if (!option.equals(challenge)) {
                System.out.println(option);
            }
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

    String getCorrectTranslation(String word) {
        List<String> translations = challengeOptions.get(word);

        if (translations != null && !translations.isEmpty()) {
            return translations.get(0);
        } else {
            // Обработайте случай, когда переводы равны null или пусты
            return "Перевод не найден";
        }
    }


    private String getChallengeByIndex(int index) {
        List<String> challenges = new ArrayList<>(challengeOptions.keySet());
        return challenges.get(index);
    }

    public void sendChallengeOptions(long chatId) {
        int challengeIndex = currentChallengeIndices.getOrDefault(chatId, 0);
        int totalChallenges = challengeOptions.size();

        if (totalChallenges == 0) {
            System.out.println("Список слов пуст.");
            return;
        }

        String challenge = getChallengeByIndex(challengeIndex);
        currentChallenges.put(chatId, challenge);
        sendChallengeOptionsTwo(chatId, challenge);

        challengeIndex = (challengeIndex + 1) % totalChallenges;
        currentChallengeIndices.put(chatId, challengeIndex);
    }

    public Map<Long, String> getCurrentChallenges() {
        return currentChallenges;
    }

    public Map<Long, Integer> getCurrentChallengeIndices() {
        return currentChallengeIndices;
    }

    public Map<String, List<String>> getChallengeOptions() {
        return challengeOptions;
    }

    public static void main(String[] args) {
        LogicBot logicBot = new LogicBot();
        Scanner scanner = new Scanner(System.in);

        long chatId = 123; // Замените 123 на реальный идентификатор чата

        // Цикл взаимодействия с пользователем
        while (true) {
            logicBot.sendChallengeOptions(chatId);

            System.out.println("Введите ваш ответ (или 'exit' для завершения):");
            String userAnswer = scanner.nextLine(); // Получаем ответ пользователя

            if ("exit".equalsIgnoreCase(userAnswer)) {
                System.out.println("Бот завершает работу.");
                break;
            }

            logicBot.handleCallbackQuery(chatId, userAnswer);
        }

        scanner.close();
    }
}



