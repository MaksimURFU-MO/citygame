package game;
import java.util.*;

public class GameState {
    private final  List<String> history = new ArrayList<>();
    private final Set<String> usedCitiesLowerCase = new HashSet<>();
    private char expectedLetter;
    private boolean started = false;

    public boolean isStarted() {
        return started;
    }

    public void start() {
        started = true;
    }

    public char getExpectedLetter() {
        return expectedLetter;
    }

    public void setExpectedLetter(char letter) {
        this.expectedLetter = letter;
    }

    public void reset() {
        history.clear();
        usedCitiesLowerCase.clear();
        started = false;
        expectedLetter = '\0';
    }

    public void addCity(String city) {
        history.add(city);
        usedCitiesLowerCase.add(city.trim().toLowerCase());
    }

    public boolean isUsed(String city) {
        return usedCitiesLowerCase.contains(city.trim().toLowerCase());
    }

    public List<String> getHistory() {
        return List.copyOf(history);
    }




}
