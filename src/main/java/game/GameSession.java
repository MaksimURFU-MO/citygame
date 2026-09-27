package game;

import java.util.Optional;

public class GameSession {

    public static final String HELP_TEXT = String.join("\n",
            "Я бот для игры в города!",
            "Правила просты: я называю город, а ты называешь другой город,",
            "начинающийся на последнюю (значащую) букву моего.",
            "Команды:",
            "  \\help — показать это сообщение ещё раз",
            "  подскажи — попросить подсказку",
            "  сдаюсь — сдаться и начать заново"
    );

    private final CityDatabase database;
    private final LetterUtils letterUtils;
    private final Bot bot;
    private final GameState state = new GameState();

    public GameSession(CityDatabase database, LetterUtils letterUtils, Bot bot) {
        this.database = database;
        this.letterUtils = letterUtils;
        this.bot = bot;
    }


    public String handleMessage(String rawMessage) {
        String message = rawMessage == null ? "" : rawMessage.trim();

        if (message.equalsIgnoreCase("\\help") || message.equalsIgnoreCase("/help")) {
            return HELP_TEXT;
        }

        if (!state.isStarted()) {
            return startGame();
        }

        String normalized = message.toLowerCase();
        if (normalized.equals("сдаюсь")) {
            return giveUp();
        }
        if (normalized.equals("подскажи") || normalized.equals("подсказка")) {
            return giveHint();
        }
        return handleCityGuess(message);
    }

    private String startGame() {
        state.start();
        for (char letter = 'а'; letter <= 'я'; letter++) {
            Optional<String> city = bot.chooseCity(letter, state);
            if (city.isPresent()) {
                state.addCity(city.get());
                state.setExpectedLetter(letterUtils.lastSymbol(city.get()));
                return HELP_TEXT + "\n\nНачинаем! Мой город: " + city.get();
            }
        }
        throw new IllegalStateException("В базе городов нет ни одного города");
    }

    private String giveUp() {
        state.reset();
        return "Ура! Я победил! Ещё разик?";
    }

    private String giveHint() {
        char letter = state.getExpectedLetter();
        Optional<String> hintCity = bot.chooseCity(letter, state);
        if (hintCity.isEmpty()) {
            return "Даже я не знаю больше городов на букву \"" + Character.toUpperCase(letter)
                    + "\". Похоже, ты выиграл! Сыграем ещё?";
        }
        String city = hintCity.get();
        int prefixLength = Math.min(2, Math.max(1, city.length() - 1));
        return "Есть один город. На " + city.substring(0, prefixLength)
                + " начинается, на " + Character.toLowerCase(city.charAt(city.length() - 1))
                + " заканчивается…";
    }

    private String handleCityGuess(String cityName) {
        if (cityName.isEmpty()) {
            return "Назови город на букву \"" + Character.toUpperCase(state.getExpectedLetter()) + "\".";
        }

        char firstLetter = Character.toLowerCase(cityName.charAt(0));
        char expected = state.getExpectedLetter();
        if (firstLetter != expected) {
            return "Нужно называть город на последнюю букву предыдущего, "
                    + "на которую могут начинаться города. Конкретно сейчас нужен город на букву "
                    + Character.toUpperCase(expected) + ".";
        }

        if (!database.cityExists(cityName)) {
            return "Нет такого города!";
        }

        if (state.isUsed(cityName)) {
            return "Этот город уже называли! Назови другой на букву "
                    + Character.toUpperCase(expected) + ".";
        }

        state.addCity(cityName);
        char nextLetter = letterUtils.lastSymbol(cityName);

        Optional<String> botCity = bot.chooseCity(nextLetter, state);
        if (botCity.isEmpty()) {
            state.reset();
            return "Я не знаю больше городов на букву \"" + Character.toUpperCase(nextLetter)
                    + "\"! Ты победил! Сыграем ещё?";
        }

        state.addCity(botCity.get());
        state.setExpectedLetter(letterUtils.lastSymbol(botCity.get()));
        return botCity.get();
    }


    public GameState getState() {
        return state;
    }
}