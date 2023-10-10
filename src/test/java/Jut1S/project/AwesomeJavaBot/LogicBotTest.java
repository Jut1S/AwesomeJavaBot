package Jut1S.project.AwesomeJavaBot;
 
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
 
public class LogicBotTest {
 
    // Тестирует команду start
    @Test
    public void testStartCommand() {
        LogicBot bot = new LogicBot();
        String result = bot.processInput("/start");
        String expected = String.format("Hello, %s!%nHow can I help you?", System.getProperty("user.name"));
        assertEquals(expected, result);
    }
 
    // Тестирует команду Exit
    @Test
    public void testExitCommand() {
        LogicBot bot = new LogicBot();
        String result = bot.processInput("/exit");
        assertEquals("Exiting the bot.", result);
    }
 
 
    // Тестирует команду Help
    @Test
    public void testHelpCommand() {
        LogicBot bot = new LogicBot();
        String result = bot.processInput("/help");
        assertTrue(result.contains("Hello, I am a simple java bot."));
    }
 
    // Тестирует команду Time
    @Test
    public void testTimeCommand() {
        LogicBot bot = new LogicBot();
        String result = bot.processInput("/time");
        assertFalse(result.isEmpty());
    }
 
    // Тестирует неправильный ввод команд
    @Test
    public void testInvalidCommand() {
        LogicBot bot = new LogicBot();
        String result = bot.processInput("/invalid");
        assertEquals("Sorry, command was not recognized!!!", result);
    }
}
