package ru.yandex.practicum;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Wordle {

    private static final String DICTIONARY_FILE_NAME = "words_ru.txt";
    private static final String LOG_FILE_NAME = "log.txt";

    public static void main(String[] args) {
        try (
                PrintWriter log = new PrintWriter(
                        Files.newBufferedWriter(Path.of(LOG_FILE_NAME), StandardCharsets.UTF_8)
                );
                Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)
        ) {
            WordleDictionary dictionary = WordleDictionaryLoader.load(DICTIONARY_FILE_NAME);
            WordleGame game = new WordleGame(dictionary, log);

            System.out.println("Добро пожаловать в Wordle!");
            System.out.println("Нужно угадать слово из 5 букв.");
            System.out.println("Символы подсказки:");
            System.out.println("+ — буква есть и стоит на своём месте");
            System.out.println("^ — буква есть, но стоит не на своём месте");
            System.out.println("- — буквы нет в слове");
            System.out.println("Нажмите Enter на пустой строке, чтобы получить подсказку.");
            System.out.println("Количество подсказок ограничено: " + game.getHintsLeft());
            System.out.println("Введите \"стоп\", чтобы завершить игру.");
            System.out.println();

            while (game.isRunning()) {
                System.out.println("Осталось попыток: " + game.getStepsLeft());
                System.out.print("> ");

                String input = scanner.nextLine();
                String resultMessage = game.processInput(input);

                System.out.println(resultMessage);
                System.out.println();
            }

        } catch (IOException e) {
            System.out.println("Ошибка работы с файлами: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }
}