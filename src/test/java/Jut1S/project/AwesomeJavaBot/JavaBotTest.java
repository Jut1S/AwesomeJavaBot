package Jut1S.project.AwesomeJavaBot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaBotTest {



    @Test
    public void testExitCommand() {
        String input = "/exit\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        String expectedOutput = "";

        String output = JavaBot.main(new String[]{});

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testHelpCommand() {
        String input = "/help\n/exit\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        String expectedOutput = "Hello, I am a simple java bot. There are some commands you can use: \n" +
                " /help - to show info about me and my commands \n" +
                " /start - to show start message \n" +
                " /time - to show time\n";

        String output = JavaBot.main(new String[]{});

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTimeCommand() {
        String input = "/time\n/exit\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        String expectedOutput = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/uu HH:mm:ss")) + "\n";

        String output = JavaBot.main(new String[]{});

        assertEquals(expectedOutput, output);
    }
}
