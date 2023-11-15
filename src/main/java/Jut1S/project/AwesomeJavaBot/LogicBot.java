/**
 * LogicBot - это телеграм-бот, предназначенный для выполнения различных команд и предоставления
 * вызовов. Поддерживаются команды, такие как /start, /help, /time и /challenge. Команда /challenge
 * предлагает пользователю слово на русском языке и просит его перевести на английский. Бот отслеживает
 * текущие вызовы и предоставляет обратную связь по ответам пользователя.
 */


package Jut1S.project.AwesomeJavaBot;


import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogicBot {


    private Map<Long, String> currentChallenges = new HashMap<>();
    private Map<Long, Integer> currentChallengeIndices = new HashMap<>();

    private Map<String, List<String>> challengeOptions = new HashMap<>();


    /**
     * Конструктор LogicBot инициализирует варианты вызовов.
     */
    public LogicBot() {
        initializeChallengeOptions();
    }

    /**
     * Обрабатывает входящие текстовые сообщения и выполняет соответствующие команды.
     *
     * @param chatId       Идентификатор чата в Telegram.
     * @param messageText  Текст входящего сообщения.
     * @param name         Имя пользователя, отправившего сообщение.
     * @return Ответное сообщение на основе полученной команды.
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
                sendChallengeOptions(chatId);
            case "/endchallenge":
                endChallenge(chatId);
                return "Вы завершили вызов. Можете начать новый вызов с командой /challenge.";
            default:
                return "Извините, команда не распознана";
        }
    }


    /**
     * Инициализирует варианты слов для вызовов.
     */
    private void initializeChallengeOptions() {
        challengeOptions.put("дом", Arrays.asList("house", "water", "building", "cat"));
        challengeOptions.put("дерево", Arrays.asList("tree", "flag", "forest", "bird"));
        challengeOptions.put("кот", Arrays.asList("cat", "dog", "cucumber", "lamb"));
        // Добавьте другие слова и варианты ответов по аналогии
    }


    /**
     * Обрабатывает команду /start и возвращает приветственное сообщение с именем пользователя.
     *
     * @param name Имя пользователя.
     * @return Приветственное сообщение.
     */
    public String startCommandReceived(String name) {
        return "Привет, " + name + ", приятно познакомиться!";
    }


    /**
     * Завершает текущий вызов для указанного идентификатора чата.
     *
     * @param chatId Идентификатор чата в Telegram.
     */
    public void endChallenge(long chatId) {
        currentChallenges.remove(chatId);
        currentChallengeIndices.remove(chatId);
        System.out.println("Вы завершили вызов. Можете начать новый вызов с вызовом метода sendChallengeOptions.");
    }


    /**
     * Обрабатывает ответ пользователя на вызов и проверяет его правильность.
     *
     * @param chatId Идентификатор чата в Telegram.
     * @param data   Ответ пользователя.
     */
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


    /**
     * Отправляет варианты ответов пользователю для указанного вызова.
     *
     * @param chatId    Идентификатор чата в Telegram.
     * @param challenge Слово вызова.
     */
    public void sendChallengeOptionsTwo(long chatId, String challenge) {
        System.out.println("Выберите правильный перевод слова '" + challenge + "':");

        List<String> answerOptions = getAnswerOptions(challenge);

        for (String option : answerOptions) {
            if (!option.equals(challenge)) {
                System.out.println(option);
            }
        }
    }


    /**
     * Возвращает список вариантов ответов для указанного слова вызова.
     *
     * @param correctAnswer Правильный ответ на вызов.
     * @return Список вариантов ответов, включая правильный ответ.
     */
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

    /**
     * Возвращает правильный перевод для указанного слова вызова.
     *
     * @param word Слово вызова.
     * @return Правильный перевод слова вызова.
     */
    String getCorrectTranslation(String word) {
        List<String> translations = challengeOptions.get(word);

        if (translations != null && !translations.isEmpty()) {
            return translations.get(0);
        } else {
            // Обработайте случай, когда переводы равны null или пусты
            return "Перевод не найден";
        }
    }

    /**
     * Возвращает слово вызова по указанному индексу.
     *
     * @param index Индекс слова вызова.
     * @return Слово вызова.
     */
    private String getChallengeByIndex(int index) {
        List<String> challenges = new ArrayList<>(challengeOptions.keySet());
        return challenges.get(index);
    }


    /**
     * Отправляет варианты ответов для вызова пользователю в указанном чате.
     *
     * @param chatId Идентификатор чата в Telegram.
     */
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

    /**
     * Возвращает текущие вызовы для всех чатов.
     *
     * @return Карта с текущими вызовами по идентификаторам чатов.
     */
    public Map<Long, String> getCurrentChallenges() {
        return currentChallenges;
    }


    /**
     * Возвращает текущие индексы вызовов для всех чатов.
     *
     * @return Карта с текущими индексами вызовов по идентификаторам чатов.
     */
    public Map<Long, Integer> getCurrentChallengeIndices() {
        return currentChallengeIndices;
    }


    /**
     * Возвращает варианты слов для вызовов.
     *
     * @return Карта с вариантами слов для вызовов.
     */
    public Map<String, List<String>> getChallengeOptions() {
        return challengeOptions;
    }

}



