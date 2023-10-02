package Jut1S.project.AwesomeJavaBot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class JavaBot {

    public static String main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        StringBuilder output = new StringBuilder();

        while (!exit) {
            System.out.print("> ");
            String userInput = scanner.nextLine();
            switch (userInput) {
                case "/start":
                    output.append(String.format("Hello, %s!%nHow can I help you?%n", System.getProperty("user.name")));
                    break;
                case "/exit" :
                    exit = true;
                    break;
                case "/help" :
                    output.append("Hello, I am a simple java bot. There are some commands you can use: " +
                            "\n /help - to show info about me and my commands " +
                            "\n /start - to show start message " +
                            "\n /time - to show time\n");
                    break;
                case "/time" : {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uu HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    output.append(dtf.format(now)).append("\n");
                    break;
                }
                default :
                    output.append("Sorry, command was not recognized\n");
            }
        }

        return output.toString();
    }
}
