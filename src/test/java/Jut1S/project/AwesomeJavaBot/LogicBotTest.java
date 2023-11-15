package Jut1S.project.AwesomeJavaBot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс LogicBotTest представляет собой набор тестов для проверки функциональности класса LogicBot.
 * Эти тесты включают в себя проверки методов для обработки запросов пользователя и логики бота.
 */
class LogicBotTest {

    private LogicBot logicBot;

    /**
     * Инициализация объекта LogicBot перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        logicBot = new LogicBot();
    }

    /**
     * Тестирование метода startCommandReceived, который должен возвращать приветственное сообщение.
     */
    @Test
    void testStartCommandReceived() {
        String name = "John";
        String result = logicBot.startCommandReceived(name);
        assertEquals("Привет, " + name + ", приятно познакомиться!", result);
    }

    /**
     * Тестирование метода handleTextMessage для команды "/start".
     */
    @Test
    void testHandleTextMessageStartCommand() {
        String name = "Alice";
        String result = logicBot.handleTextMessage(12345, "/start", name);
        assertEquals("Привет, " + name + ", приятно познакомиться!", result);
    }

    /**
     * Тестирование метода handleTextMessage для команды "/help".
     * Проверяет, что результат содержит перечень доступных команд.
     */
    @Test
    void testHandleTextMessageHelpCommand() {
        String result = logicBot.handleTextMessage(12345, "/help", "Alice");
        assertTrue(result.contains("/help"));
        assertTrue(result.contains("/start"));
        assertTrue(result.contains("/time"));
        assertTrue(result.contains("/challenge"));
    }


    /**
     * Тест проверяет, что вызов метода endChallenge приводит к удалению текущего вызова пользователя.
     */
    @Test
    void endChallengeRemovesChallenge() {
        LogicBot logicBot = new LogicBot();
        long chatId = 123;

        logicBot.endChallenge(chatId);

        assertNull(logicBot.getCurrentChallenges().get(chatId));
        assertNull(logicBot.getCurrentChallengeIndices().get(chatId));
    }

    /**
     * Тест проверяет, что после успешного ответа пользователя предлагается новый вызов.
     */
    @Test
    public void testHandleCallbackQueryCorrect() {
        long chatId = 123;

        // Отправляем пользователю вопрос
        logicBot.sendChallengeOptions(chatId);

        // Получаем правильный перевод для текущего вызова
        String currentChallenge = logicBot.getCurrentChallenges().get(chatId);
        String correctTranslation = logicBot.getCorrectTranslation(currentChallenge);

        // Сохраняем текущее состояние вызовов
        Map<Long, String> initialChallenges = Map.copyOf(logicBot.getCurrentChallenges());

        // Вызываем handleCallbackQuery с правильным переводом
        logicBot.handleCallbackQuery(chatId, correctTranslation);

        // Проверяем, что текущий вызов изменился и новый вызов был отправлен
        assertNotEquals(initialChallenges, logicBot.getCurrentChallenges());
    }


    /**
     * Тест проверяет, что при неправильном ответе текущий вызов пользователя не удаляется.
     */
    @Test
    public void testHandleCallbackQueryIncorrect() {
        long chatId = 123;
        // Отправляем пользователю вопрос
        logicBot.sendChallengeOptions(chatId);

        // Получаем правильный перевод для текущего вызова
        String currentChallenge = logicBot.getCurrentChallenges().get(chatId);
        String incorrectTranslation = "incorrect_translation";

        // Вызываем handleCallbackQuery с неправильным переводом
        logicBot.handleCallbackQuery(chatId, incorrectTranslation);

        // Проверяем, что текущий вызов не был удален
        assertEquals(currentChallenge, logicBot.getCurrentChallenges().get(chatId));
    }

    /**
     * Тест проверяет, что метод getCorrectTranslation возвращает правильный перевод для заданного вызова.
     */
    @Test
    void getCorrectTranslationReturnsCorrectTranslation() {
        LogicBot logicBot = new LogicBot();
        String challenge = "дом";

        assertEquals("house", logicBot.getCorrectTranslation(challenge));
    }

    /**
     * Тест проверяет, что вызов метода sendChallengeOptions увеличивает индекс текущего вызова.
     */
    @Test
    void sendChallengeOptionsIncreasesIndex() {
        LogicBot logicBot = new LogicBot();
        long chatId = 123;

        int initialIndex = logicBot.getCurrentChallengeIndices().getOrDefault(chatId, 0);

        logicBot.sendChallengeOptions(chatId);

        int newIndex = logicBot.getCurrentChallengeIndices().get(chatId);

        assertEquals((initialIndex + 1) % logicBot.getChallengeOptions().size(), newIndex);
    }

}


