package game;

import java.nio.file.Path;
import java.io.IOException;

public class main {
    static void main() throws IOException {

        CityDatabase database = new CityDatabase();
        database.loadFromFile(Path.of("src/main/resources/cities.txt"));

        System.out.println(database.size());

        System.out.println(database.getCitiesByLetter('п'));

        System.out.println(database.cityExists("Екатеринбург"));
    }
}
