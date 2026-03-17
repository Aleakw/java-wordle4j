package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WordleGame {

    private static final int MAX_STEPS = 6;
    private static final int MAX_HINTS = 3;
    private static final String STOP_COMMAND = "стоп";

    private final WordleDictionary dictionary;
    private final PrintWriter log;

    private final List<String> previousWords;
    private final List<String> previousHints;

    private List<String> candidateWords;

    private String answer;
    private int stepsLeft;
    private int hintsLeft;
    private boolean win;
    private boolean stopped;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this(dictionary, log, dictionary.getRandomWord());
    }

    public WordleGame(WordleDictionary dictionary, PrintWriter log, String answer) {
        if (dictionary == null) {
            throw new IllegalArgumentException("Словарь не должен быть null.");
        }
        if (log == null) {
            throw new IllegalArgumentException("Лог не должен быть null.");
        }

        this.dictionary = dictionary;
        this.log = log;
        this.answer = WordleDictionary.normalize(answer);

        if (!dictionary.contains(this.answer)) {
            throw new IllegalArgumentException("Ответ должен присутствовать в словаре.");
        }

        this.stepsLeft = MAX_STEPS;
        this.hintsLeft = MAX_HINTS;
        this.win = false;
        this.stopped = false;

        this.previousWords = new ArrayList<>();
        this.previousHints = new ArrayList<>();
        this.candidateWords = new ArrayList<>(dictionary.getWords());
    }

    public boolean isRunning() {
        return !win && !stopped && stepsLeft > 0;
    }

    public int getStepsLeft() {
        return stepsLeft;
    }

    public int getHintsLeft() {
        return hintsLeft;
    }

    public boolean isWin() {
        return win;
    }

    public boolean isStopped() {
        return stopped;
    }

    public String getAnswer() {
        return answer;
    }

    public String processInput(String input) {
        if (!isRunning()) {
            return "Игра уже завершена.";
        }

        String normalizedInput = WordleDictionary.normalize(input);

        // Команда остановки игры
        if (normalizedInput.equals(STOP_COMMAND)) {
            stopped = true;
            return "Игра остановлена. Загаданное слово: " + answer;
        }

        // Запрос подсказки по пустой строке
        if (normalizedInput.isEmpty()) {
            if (hintsLeft == 0) {
                return "Подсказки закончились.";
            }

            String suggestion = findSuggestionWord();

            if (suggestion == null || suggestion.isBlank()) {
                return "Подсказку подобрать не удалось.";
            }

            hintsLeft--;
            return "Подсказка: " + suggestion + System.lineSeparator()
                    + "Осталось подсказок: " + hintsLeft;
        }

        // Проверка корректности слова
        if (!WordleDictionary.isValidWord(normalizedInput)) {
            return "Введите слово из 5 русских букв.";
        }

        if (!dictionary.contains(normalizedInput)) {
            return "Такого слова нет в словаре.";
        }

        String hint = buildHint(normalizedInput, answer);

        previousWords.add(normalizedInput);
        previousHints.add(hint);

        stepsLeft--;
        updateCandidateWords(normalizedInput, hint);

        if (normalizedInput.equals(answer)) {
            win = true;
            return normalizedInput + System.lineSeparator()
                    + hint + System.lineSeparator()
                    + "Вы выиграли! Загаданное слово: " + answer;
        }

        if (stepsLeft == 0) {
            return normalizedInput + System.lineSeparator()
                    + hint + System.lineSeparator()
                    + "Попытки закончились. Загаданное слово: " + answer;
        }

        return normalizedInput + System.lineSeparator() + hint;
    }

    private String findSuggestionWord() {
        for (String candidate : candidateWords) {
            if (!previousWords.contains(candidate)) {
                return candidate;
            }
        }

        for (String word : dictionary.getWords()) {
            if (!previousWords.contains(word)) {
                return word;
            }
        }

        return null;
    }

    private void updateCandidateWords(String guess, String hint) {
        List<String> filteredWords = new ArrayList<>();

        for (String candidate : candidateWords) {
            String candidateHint = buildHint(guess, candidate);

            if (candidateHint.equals(hint)) {
                filteredWords.add(candidate);
            }
        }

        candidateWords = filteredWords;
    }

    public static String buildHint(String guess, String answer) {
        String normalizedGuess = WordleDictionary.normalize(guess);
        String normalizedAnswer = WordleDictionary.normalize(answer);

        char[] result = {'-', '-', '-', '-', '-'};
        int[] answerLetterCounts = new int[33];

        for (int i = 0; i < 5; i++) {
            if (normalizedGuess.charAt(i) == normalizedAnswer.charAt(i)) {
                result[i] = '+';
            } else {
                char answerChar = normalizedAnswer.charAt(i);
                answerLetterCounts[answerChar - 'а']++;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (result[i] == '+') {
                continue;
            }

            char guessChar = normalizedGuess.charAt(i);
            int index = guessChar - 'а';

            if (index >= 0 && index < answerLetterCounts.length && answerLetterCounts[index] > 0) {
                result[i] = '^';
                answerLetterCounts[index]--;
            }
        }

        return new String(result);
    }
}