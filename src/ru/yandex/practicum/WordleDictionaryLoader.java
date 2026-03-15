package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    public static WordleDictionary load(String fileName) throws IOException {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String normalizedWord = WordleDictionary.normalize(line);

                if (WordleDictionary.isValidWord(normalizedWord)) {
                    words.add(normalizedWord);
                }
            }
        }

        if (words.isEmpty()) {
            throw new IOException("Не удалось загрузить слова из файла словаря.");
        }

        return new WordleDictionary(words);
    }
}