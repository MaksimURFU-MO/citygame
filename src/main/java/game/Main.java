package game;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;


public class Main {

    private static final String EXIT_COMMAND = "\\exit";


    private static final Path CITIES_FILE = Path.of("src/main/resources/cities.txt");

    public static void main(String[] args) throws IOException {
        CityDatabase database = new CityDatabase();
        database.loadFromFile(CITIES_FILE);

        LetterUtils letterUtils = new LetterUtils();
        Bot bot = new Bot(database);
        GameSession session = new GameSession(database, letterUtils, bot);
        ResultWriter resultWriter = new ResultWriter();

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);


        out.println(session.handleMessage(""));

        while (true) {
            out.print("> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine();
            if (line.trim().equalsIgnoreCase(EXIT_COMMAND)) {
                out.println("Пока! Спасибо за игру.");
                break;
            }
            out.println(session.handleMessage(line));
        }

        resultWriter.writeResult(session.getState().getHistory());
        scanner.close();
    }
}