package com.helmes.fastanagrams;

import com.helmes.anagrams.Solution;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Objects;

public class RunnerSentence {
    public static void main(String[] args) throws IOException, URISyntaxException {
        //получаем словарь
        URI uriDictionary = Objects.requireNonNull(Solution
                .class
                .getClassLoader()
                .getResource("pldf-win.txt")).toURI();

        String[] dict = Files
                .lines(Paths.get(uriDictionary), Charset.forName("CP1251"))
                .toArray(String[]::new);

        //создаем экземпляр алгоритма
        SolutionSimple solution = new SolutionSimple();

        StringBuilder out = new StringBuilder();
        long time = System.currentTimeMillis();
        //находим анаграммы для слова автор
        String word = "кафельный автор это секта и штука";
        Collection collection = solution.collectAnagrams(dict, word);
        for (Object phrase : collection) {
            out.append(phrase).append('\n');
        }
        //подводим итоги
        System.out.printf("Ищем по фразе [%s].%n"
                        + "Анаграммы фразы: \n%s"
                        + "\nЗатраченное время t = %d ms%n",
                word,
                out,
                System.currentTimeMillis() - time
        );
    }
}
