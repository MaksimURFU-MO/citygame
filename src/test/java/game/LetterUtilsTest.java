package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LetterUtilsTest {

    private LetterUtils letterUtils;

    // Выполняется перед КАЖДЫМ тестом - у каждого теста свой новый объект
    @BeforeEach
    void setUp() {
        letterUtils = new LetterUtils();
    }

    @Test
    void ordinaryCity_returnsLastLetter() {
        assertEquals('и', letterUtils.lastSymbol("Сочи"));
        assertEquals('ч', letterUtils.lastSymbol("Углич"));
        assertEquals('а', letterUtils.lastSymbol("Москва"));
    }

    @Test
    void softSign_isSkipped() {
        assertEquals('м', letterUtils.lastSymbol("Пермь"));
        assertEquals('л', letterUtils.lastSymbol("Ярославль"));
    }

    @Test
    void hardSign_isSkipped() {
        // Реальных городов на "ъ" нет, поэтому проверяем на условном слове
        assertEquals('т', letterUtils.lastSymbol("Тестъ"));
    }

    @Test
    void letterY_isSkipped() {
        // "Гай" - реальный город в Оренбургской области
        assertEquals('а', letterUtils.lastSymbol("Гай"));
    }

    @Test
    void severalSkippedLettersInARow_areAllSkipped() {
        // "Грозный": сначала пропускается "й", потом "ы" -> остаётся "н"
        assertEquals('н', letterUtils.lastSymbol("Грозный"));
    }

    @Test
    void letterYo_isReplacedWithYe() {
        // Условное слово, чтобы проверить замену ё -> е
        assertEquals('е', letterUtils.lastSymbol("Тестё"));
    }

    @Test
    void upperCase_isHandled() {
        assertEquals('а', letterUtils.lastSymbol("МОСКВА"));
    }

    @Test
    void spacesAroundCity_areIgnored() {
        assertEquals('а', letterUtils.lastSymbol("  Москва  "));
    }

    @Test
    void compoundNames_useLastLetterOfWholeName() {
        assertEquals('д', letterUtils.lastSymbol("Нижний Новгород"));
        assertEquals('у', letterUtils.lastSymbol("Ростов-на-Дону"));
    }

    @Test
    void nullCity_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> letterUtils.lastSymbol(null));
    }

    @Test
    void emptyCity_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> letterUtils.lastSymbol(""));
    }

    // ВНИМАНИЕ: этот тест сейчас УПАДЁТ - он нашёл настоящую ошибку в LetterUtils.
    // Строка из одних пробелов проходит проверку isEmpty(), но после trim() становится
    // пустой, и charAt(-1) выбрасывает StringIndexOutOfBoundsException вместо
    // IllegalArgumentException.
    // Исправление в LetterUtils: заменить проверку в начале метода на
    //     if (city == null || city.trim().isEmpty())
    @Test
    void onlySpaces_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> letterUtils.lastSymbol("   "));
    }
}