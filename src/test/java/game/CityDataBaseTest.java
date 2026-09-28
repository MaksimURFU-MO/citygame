package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CityDatabaseTest {

    // Временная папка: JUnit сам создаёт её перед тестом и удаляет после
    @TempDir
    Path tempDir;

    private CityDatabase database;

    // Перед каждым тестом создаём маленькую тестовую базу из 5 городов.
    // Пустая строка и лишние пробелы добавлены специально: база должна их отфильтровать.
    @BeforeEach
    void setUp() throws IOException {
        Path file = tempDir.resolve("test_cities.txt");
        Files.write(file,
                List.of("Москва", "Мурманск", "Сочи", "Йошкар-Ола", "", "  Самара  "),
                StandardCharsets.UTF_8);

        database = new CityDatabase();
        database.loadFromFile(file);
    }

    @Test
    void size_countsOnlyNonEmptyLines() {
        assertEquals(5, database.size());
    }

    @Test
    void newDatabase_isEmpty() {
        assertEquals(0, new CityDatabase().size());
    }

    @Test
    void getCitiesByLetter_returnsAllCitiesOnThatLetter() {
        List<String> cities = database.getCitiesByLetter('м');

        assertEquals(2, cities.size());
        assertTrue(cities.contains("Москва"));
        assertTrue(cities.contains("Мурманск"));
    }

    @Test
    void getCitiesByLetter_ignoresLetterCase() {
        assertEquals(2, database.getCitiesByLetter('М').size());
    }

    @Test
    void getCitiesByLetter_unknownLetter_returnsEmptyListNotNull() {
        List<String> cities = database.getCitiesByLetter('ф');

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void getCitiesByLetter_letterY_returnsYoshkarOla() {
        assertEquals(List.of("Йошкар-Ола"), database.getCitiesByLetter('й'));
    }

    @Test
    void loadFromFile_trimsSpacesAroundCityNames() {
        assertTrue(database.cityExists("Самара"));
    }

    @Test
    void cityExists_existingCity_returnsTrue() {
        assertTrue(database.cityExists("Москва"));
        assertTrue(database.cityExists("Йошкар-Ола"));
    }

    @Test
    void cityExists_ignoresCase() {
        assertTrue(database.cityExists("москва"));
        assertTrue(database.cityExists("МОСКВА"));
    }

    @Test
    void cityExists_ignoresSpacesAroundInput() {
        assertTrue(database.cityExists("  Москва  "));
    }

    @Test
    void cityExists_cityWithTypo_returnsFalse() {
        assertFalse(database.cityExists("Москваааа"));
    }

    @Test
    void cityExists_emptyString_returnsFalse() {
        assertFalse(database.cityExists(""));
    }

    @Test
    void cityExists_null_returnsFalse() {
        assertFalse(database.cityExists(null));
    }

    @Test
    void loadFromFile_missingFile_throwsIOException() {
        Path missing = tempDir.resolve("no_such_file.txt");

        assertThrows(IOException.class, () -> new CityDatabase().loadFromFile(missing));
    }
}