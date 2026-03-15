package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordleDictionary {

    private final List<String> words;
    private final Random random;

    public WordleDictionary(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Словарь не должен быть пустым.");
        }

        this.words = new ArrayList<>();
        for (String word : words) {
            String normalizedWord = normalize(word);
            if (isValidWord(normalizedWord) && !this.words.contains(normalizedWord)) {
                this.words.add(normalizedWord);
            }
        }

        if (this.words.isEmpty()) {
            throw new IllegalArgumentException("После обработки словарь оказался пустым.");
        }

        this.random = new Random();
    }

    public boolean contains(String word) {
        return words.contains(normalize(word));
    }

    public String getRandomWord() {
        int index = random.nextInt(words.size());
        return words.get(index);
    }

    public List<String> getWords() {
        return Collections.unmodifiableList(words);
    }

    public int size() {
        return words.size();
    }

    public static String normalize(String word) {
        if (word == null) {
            return "";
        }

        return word.trim()
                .toLowerCase()
                .replace('ё', 'е');
    }

    public static boolean isValidWord(String word) {
        String normalizedWord = normalize(word);
        return normalizedWord.matches("[а-я]{5}");
    }
}