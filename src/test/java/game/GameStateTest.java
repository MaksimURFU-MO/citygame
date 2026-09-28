package game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GameStateTest {
    // sut - system under test
    private GameState sut = new GameState();


    @Test
    void isNotStartedByDefaultTest() {
        Assertions.assertFalse(sut.isStarted());
    }

    @Test
    void startMarksGameAsStartedTest() {
        sut.start();
        Assertions.assertTrue(sut.isStarted());
    }


    @Test
    void addCityStoresInHistoryTest() {
        sut.addCity("Москва");
        Assertions.assertEquals(List.of("Москва"), sut.getHistory());
    }

    @Test
    void addCityKeepsOrderTest() {
        sut.addCity("Москва");
        sut.addCity("Астрахань");
        Assertions.assertEquals(List.of("Москва", "Астрахань"), sut.getHistory());
    }

    @Test
    void getHistoryReturnsImmutableCopyTest() {
        sut.addCity("Москва");
        List<String> history = sut.getHistory();

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> history.add("Казань"));
    }


    @Test
    void addCityMarksCityAsUsedTest() {
        sut.addCity("Москва");
        Assertions.assertTrue(sut.isUsed("Москва"));
    }

    @Test
    void isUsedIsCaseInsensitiveTest() {
        sut.addCity("Москва");
        Assertions.assertTrue(sut.isUsed("москва"));
    }

    @Test
    void isUsedTrimsWhitespaceTest() {
        sut.addCity("Москва");
        Assertions.assertTrue(sut.isUsed("  МОСКВА  "));
    }

    @Test
    void isUsedReturnsFalseForUnknownCityTest() {
        Assertions.assertFalse(sut.isUsed("Казань"));
    }


    @Test
    void expectedLetterCanBeSetAndReadTest() {
        sut.setExpectedLetter('к');
        Assertions.assertEquals('к', sut.getExpectedLetter());
    }


    @Test
    void resetClearsHistoryAndStartedFlagTest() {
        sut.start();
        sut.addCity("Москва");

        sut.reset();

        Assertions.assertFalse(sut.isStarted());
        Assertions.assertTrue(sut.getHistory().isEmpty());
    }

    @Test
    void resetClearsUsedCitiesTest() {
        sut.addCity("Москва");

        sut.reset();

        Assertions.assertFalse(sut.isUsed("Москва"));
    }

    @Test
    void resetClearsExpectedLetterTest() {
        sut.setExpectedLetter('к');

        sut.reset();

        Assertions.assertEquals('\0', sut.getExpectedLetter());
    }


    @Test
    void addCityStoresInSessionLogTest() {
        sut.addCity("Москва");
        Assertions.assertEquals(List.of("Москва"), sut.getSessionLog());
    }

    @Test
    void resetDoesNotClearSessionLogTest() {
        sut.addCity("Москва");

        sut.reset();

        Assertions.assertEquals(List.of("Москва"), sut.getSessionLog());
        Assertions.assertTrue(sut.getHistory().isEmpty());
    }

    @Test
    void sessionLogKeepsCitiesFromAllGamesTest() {
        sut.addCity("Москва");
        sut.reset();
        sut.addCity("Казань");

        Assertions.assertEquals(List.of("Москва", "Казань"), sut.getSessionLog());
    }

    @Test
    void getSessionLogReturnsImmutableCopyTest() {
        sut.addCity("Москва");
        List<String> log = sut.getSessionLog();

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> log.add("Казань"));
    }
}