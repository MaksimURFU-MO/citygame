package game;
//Bot тесты
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Random;

public class BotTest {

    @TempDir
    Path tempDir;

    private CityDatabase database;
    private Bot sut;

    @BeforeEach
    void setUp() throws IOException {
        Path file = tempDir.resolve("cities.txt");
        Files.writeString(file, "Казань\nКурск\n", StandardCharsets.UTF_8);
        database = new CityDatabase();
        database.loadFromFile(file);
        sut = new Bot(database, new Random(1));
    }

    @Test
    void choosesCityStartingWithGivenLetterTest() {
        GameState state = new GameState();
        Optional<String> city = sut.chooseCity('к', state);

        Assertions.assertTrue(city.isPresent());
        Assertions.assertTrue(city.get().toLowerCase().startsWith("к"));
    }

    @Test
    void excludesAlreadyUsedCitiesTest() {
        GameState state = new GameState();
        state.addCity("Казань");
        state.addCity("Курск");

        Optional<String> city = sut.chooseCity('к', state);

        Assertions.assertEquals(Optional.empty(), city);
    }

    @Test
    void returnsEmptyForLetterWithNoCitiesTest() {
        GameState state = new GameState();
        Assertions.assertEquals(Optional.empty(), sut.chooseCity('ю', state));
    }
}