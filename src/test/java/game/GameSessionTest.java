package game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;


public class GameSessionTest {

    @TempDir
    Path tempDir;

    private CityDatabase database;
    private LetterUtils letterUtils;
    private GameSession sut;

    @BeforeEach
    void setUp() throws IOException {
        Path file = tempDir.resolve("cities.txt");
        Files.writeString(file,
                "Александров\nВолгоград\nДно\nОрёл\nЛипецк\nКурск\n", StandardCharsets.UTF_8);
        database = new CityDatabase();
        database.loadFromFile(file);
        letterUtils = new LetterUtils();
        sut = new GameSession(database, letterUtils, new Bot(database, new Random(0)));
    }

    @Test
    void firstMessageStartsGameWithBotCityTest() {
        String reply = sut.handleMessage("Начинаем!");
        Assertions.assertTrue(reply.contains("Начинаем! Мой город: Александров"));
        Assertions.assertEquals('в', sut.getState().getExpectedLetter());
    }

    @Test
    void wrongLetterIsRejectedTest() {
        sut.handleMessage("Начинаем!");
        String reply = sut.handleMessage("Курск");
        Assertions.assertTrue(reply.contains("нужен город на букву В"));
    }

    @Test
    void unknownCityIsRejectedTest() {
        sut.handleMessage("Начинаем!");
        String reply = sut.handleMessage("Вымышленск");
        Assertions.assertEquals("Нет такого города!", reply);
    }

    @Test
    void correctCityIsAcceptedAndBotAnswersTest() {
        sut.handleMessage("Начинаем!");
        String reply = sut.handleMessage("Волгоград");
        Assertions.assertEquals("Дно", reply); //
        Assertions.assertEquals('о', sut.getState().getExpectedLetter());
    }

    @Test
    void botConcedesWhenNoCitiesLeftTest() {
        sut.handleMessage("Начинаем!");
        sut.handleMessage("Волгоград");
        sut.handleMessage("Орёл");
        String reply = sut.handleMessage("Курск");
        Assertions.assertTrue(reply.contains("Ты победил"));
        Assertions.assertFalse(sut.getState().isStarted());
    }

    @Test
    void giveUpResetsGameTest() {
        sut.handleMessage("Начинаем!");
        String reply = sut.handleMessage("сдаюсь");
        Assertions.assertEquals("Ура! Я победил! Ещё разик?", reply);
        Assertions.assertFalse(sut.getState().isStarted());
    }

    @Test
    void hintMentionsExpectedCityTest() {
        sut.handleMessage("Начинаем!");
        String reply = sut.handleMessage("подскажи");
        Assertions.assertEquals("Есть один город. На Во начинается, на д заканчивается…", reply);
    }

    @Test
    void helpCommandWorksAtAnyMomentTest() {
        sut.handleMessage("Начинаем!");
        String reply = sut.handleMessage("\\help");
        Assertions.assertTrue(reply.contains("Я бот для игры в города"));
    }
}