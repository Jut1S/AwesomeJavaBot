/**
 * Класс LogicBotTest представляет собой набор юнит-тестов для класса LogicBot.
 * Этот класс использует библиотеку JUnit для тестирования различных методов LogicBot.
 * Все методы этого класса проверяют корректное выполнение функциональности LogicBot.
 */
package Jut1S.project.AwesomeJavaBot;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LogicBotTest {

    private LogicBot logicBot;
    private long chatId = 123456789;
    

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
     * Тестирование метода handleTextMessage для команды "/challenge".
     * Проверяет, что результат содержит текст о выборе правильного перевода.
     */
    @Test
    void testHandleTextMessageChallengeCommand() {
        String result = logicBot.handleTextMessage(12345, "/challenge", "Alice");
        assertNotNull(result);
        assertTrue(result.contains("Выберите правильный перевод слова"));
    }

    /**
     * Тестирование метода testSendChallengeOptions для команды "/challenge" с выбором правильного ответа.
     */
    @Test
    public void testSendChallengeOptions() {
        String result = logicBot.handleTextMessage(chatId, "/challenge", "TestUser");


        // Проверяем, что возвращаемый текст начинается с ожидаемой строки
        assertTrue(result.startsWith("Выберите правильный перевод слова '"));

        // Проверяем, что возвращаемый текст не содержит имя пользователя
        assertTrue(!result.contains("TestUser"));

        // Проверяем, что вызов добавлен в текущие вызовы
        Map<Long, String> currentChallenges = logicBot.getCurrentChallenges();
        assertTrue(currentChallenges.containsKey(chatId));


        // Проверяем, что текст вызова содержит одно из слов, предложенных вариантами ответа
        List<String> answerOptions = logicBot.getAnswerOptions(chatId);

        // Проверяем, что хотя бы один из вариантов ответа совпадает с вызовом
        assertTrue(answerOptions.stream().anyMatch(option -> logicBot.checkAnswer(chatId, option)));
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
