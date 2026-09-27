package game;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Bot {
    private final CityDatabase database;
    private final Random random;

    public Bot(CityDatabase database) {
        this(database, new Random());

    }

    public Bot(CityDatabase database, Random random) {
        this.database = database;
        this.random = random;
    }

    public Optional<String> chooseCity(char letter, GameState state) {
        List<String> candidates = database.getCitiesByLetter(letter).stream()
                .filter(city -> !state.isUsed(city))
                .toList();

        if (candidates.isEmpty()) {
            return Optional.empty();

        }

        return Optional.of(candidates.get(random.nextInt(candidates.size())));

    }
}
