/**
 * Класс LogicBotTest представляет собой набор юнит-тестов для класса LogicBot.
 * Этот класс использует библиотеку JUnit для тестирования различных методов LogicBot.
 * Все методы этого класса проверяют корректное выполнение функциональности LogicBot.
 *
 *
 */
package Jut1S.project.AwesomeJavaBot;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LogicBotTest {

    private LogicBot logicBot;

    /**
     * Метод setUp выполняется перед каждым тестовым методом и инициализирует экземпляр LogicBot.
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
     * Тестирование метода handleTextMessage для команды "/time".
     * Проверяет, что результат не равен null.
     * Точное время не проверяется из-за изменчивости.
     */
    @Test
    void testHandleTextMessageTimeCommand() {
        String result = logicBot.handleTextMessage(12345, "/time", "Alice");
        assertNotNull(result);
    }

    /**
     * Тестирование метода handleTextMessage для неизвестной команды.
     * Проверяет, что результат содержит сообщение о нераспознанной команде.
     */
    @Test
    void testHandleTextMessageUnknownCommand() {
        String result = logicBot.handleTextMessage(12345, "/unknown", "Alice");
        assertEquals("Извините, команда не распознана", result);
    }

    /**
     * Тестирование метода sendChallengeOptions.
     * Проверяет, что результат не равен null и содержит текст о выборе правильного перевода.
     */
    @Test
        void testSendChallengeOptions() {
        long chatId = 12345;
        String result = logicBot.sendChallengeOptions(chatId);
        assertNotNull(result);
        assertTrue(result.contains("Выберите правильный перевод слова"));
    }

    /**
     * Тестирование метода endChallenge.
     * Проверяет, что после завершения вызова sendChallengeOptions
     * текущий вызов задачи и индекс задачи обнуляются.
     */
    @Test
    void testEndChallenge() {
        long chatId = 12345;
        logicBot.sendChallengeOptions(chatId);
        logicBot.endChallenge(chatId);
        assertNull(logicBot.getCurrentChallenges().get(chatId));
        assertNull(logicBot.getCurrentChallengeIndices().get(chatId));
    }

    /**
     * Тестирование метода handleCallbackQuery для корректного ответа.
     * Проверяет, что результат содержит сообщение о правильном ответе.
     */
    @Test
    void testHandleCallbackQueryCorrect() {
        long chatId = 12345;
        logicBot.sendChallengeOptions(chatId);
        String result = logicBot.handleCallbackQuery(chatId, "correct");
        assertNotNull(result);
        assertTrue(result.contains("Правильно"));
    }

    /**
     * Тестирование метода handleCallbackQuery для неправильного ответа.
     * Проверяет, что результат содержит сообщение о неправильном ответе.
     */
    @Test
    void testHandleCallbackQueryIncorrect() {
        long chatId = 12345;
        logicBot.sendChallengeOptions(chatId);
        String result = logicBot.handleCallbackQuery(chatId, "incorrect");
        assertNotNull(result);
        assertTrue(result.contains("Увы, неправильно"));
    }

    /**
     * Тестирование метода handleCallbackQuery для неизвестного ответа.
     * Проверяет, что результат содержит сообщение о неверной команде.
     */
    @Test
    void testHandleCallbackQueryUnknown() {
        long chatId = 12345;
        String result = logicBot.handleCallbackQuery(chatId, "unknown");
        assertEquals("Неверная команда.", result);
    }
}