package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    @Test
    void shouldNormalizeWord() {
        String result = WordleDictionary.normalize(" Ёлка ");

        assertEquals("елка", result);
    }

    @Test
    void shouldCheckWordFromDictionary() {
        WordleDictionary dictionary = new WordleDictionary(List.of("герой", "город", "маска"));

        assertTrue(dictionary.contains("герой"));
        assertTrue(dictionary.contains("ГЕРОЙ"));
        assertFalse(dictionary.contains("лампа"));
    }

    @Test
    void shouldBuildHintCorrectly() {
        String hint = WordleGame.buildHint("гонец", "герой");

        assertEquals("+^-^-", hint);
    }

    @Test
    void shouldWinWhenWordIsCorrect() {
        WordleDictionary dictionary = new WordleDictionary(List.of("герой", "город", "маска"));
        StringWriter writer = new StringWriter();
        PrintWriter log = new PrintWriter(writer);

        WordleGame game = new WordleGame(dictionary, log, "герой");

        String result = game.processInput("герой");

        assertTrue(result.contains("+++++"));
        assertTrue(result.contains("Вы выиграли"));
        assertTrue(game.isWin());
        assertFalse(game.isRunning());
    }

    @Test
    void shouldNotSpendStepOnInvalidWord() {
        WordleDictionary dictionary = new WordleDictionary(List.of("герой", "город", "маска"));
        StringWriter writer = new StringWriter();
        PrintWriter log = new PrintWriter(writer);

        WordleGame game = new WordleGame(dictionary, log, "герой");

        String result = game.processInput("abc");

        assertEquals("Введите слово из 5 русских букв.", result);
        assertEquals(6, game.getStepsLeft());
    }

    @Test
    void shouldReturnSuggestionOnEmptyInput() {
        WordleDictionary dictionary = new WordleDictionary(List.of("герой", "город", "маска"));
        StringWriter writer = new StringWriter();
        PrintWriter log = new PrintWriter(writer);

        WordleGame game = new WordleGame(dictionary, log, "герой");

        String result = game.processInput("");

        assertTrue(result.startsWith("Подсказка: "));
        assertEquals(6, game.getStepsLeft());
    }
}