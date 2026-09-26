package game;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class CityListPreparer {

    private static final int CITY_COLUMN_INDEX = 0;
    private static final int POPULATION_COLUMN_INDEX = 1;
    private static final double MIN_POPULATION_THOUSANDS = 10.0;

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("towns.csv");
        Path outputPath = Path.of("cities.txt");

        List<String> lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
        List<String> resultCities = new ArrayList<>();

        // Пропускаем первую строку — это заголовок (city,population,lat,lon,...)
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] columns = line.split(",");

            // Защита от повреждённых/неполных строк
            if (columns.length <= POPULATION_COLUMN_INDEX) {
                continue;
            }

            String city = columns[CITY_COLUMN_INDEX].trim();
            String populationRaw = columns[POPULATION_COLUMN_INDEX].trim();

            if (city.isEmpty() || populationRaw.isEmpty()) {
                continue;
            }

            double population;
            try {
                population = Double.parseDouble(populationRaw);
            } catch (NumberFormatException e) {
                // Значение population не является числом — пропускаем строку
                continue;
            }

            if (population >= MIN_POPULATION_THOUSANDS) {
                resultCities.add(city);
            }
        }

        Files.write(outputPath, resultCities, StandardCharsets.UTF_8);

        System.out.println("Готово. Городов отобрано: " + resultCities.size());
        System.out.println("Результат сохранён в: " + outputPath.toAbsolutePath());
    }
}
