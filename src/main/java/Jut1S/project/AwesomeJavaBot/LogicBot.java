package Jut1S.project.AwesomeJavaBot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogicBot {

    public String processInput(String userInput) {
        switch (userInput) {
            case "/start":
                return String.format("Hello, %s!%nHow can I help you?", System.getProperty("user.name"));
            case "/exit":
                return "Exiting the bot.";
            case "/help":
                return "Hello, I am a simple java bot. There are some commands you can use: \n" +
                        "/help - to show info about me and my commands \n" +
                        "/start - to show start message \n" +
                        "/time - to show time";
            case "/time": {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uu HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                return dtf.format(now);
            }
            default:
                return "Sorry, command was not recognized!!!";
        }
    }
}
