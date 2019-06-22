package com.helmes.fastanagrams;

import com.helmes.anagrams.Solution;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class RunnerFindOne {
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

        long time = System.currentTimeMillis();
        StringBuilder out = new StringBuilder();
        //находим анаграммы для слова автор
        String word = "автор";
        Collection collection=new HashSet();
        for (int i = 0; i < 333; i++) {
            collection = solution.collectAnagrams(dict, word);
        }
        if (collection.size() > 1) out
                .append(collection)
                .append('\n');
        //подводим итоги
        System.out.printf("Ищем по слову [%s].%n"
                        + "Анаграммы в словаре: %s"
                        + "Затраченное время t = %f ms%n",
                word,
                out,
                (System.currentTimeMillis() - time)/333.0
        );
    }
}
