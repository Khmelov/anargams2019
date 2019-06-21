package com.helmes.anagrams;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Objects;

public class RunnerFindAll {
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
        Solution solution = new Solution();

        StringBuilder out = new StringBuilder();
        long time = System.currentTimeMillis();
        int count = 0;
        //находим все возможные анаграммы
        for (String word : dict) {
            Collection collection = solution.collectAnagrams(dict, word);
            if (collection.size() > 1) {
                out.append(++count)
                   .append(':')
                   .append(collection)
                   .append('\n');
            }
        }
        //подводим итоги
        long all = System.currentTimeMillis() - time;
        double one = (double) all / dict.length;
        System.out.printf("Анаграммы:\n%s\n"
                        + "Все возможные анаграммы словаря определены\n"
                        + "Всего анаграмм в словаре %d\n"
                        + "Использован только метод collectAnagrams(dict, word)\n"
                        + "Число обращений к методу равно числу слов: %d\n"
                        + "Затраченное время t = %d милисекунд\n"
                        + "Среднее время t = %f милисекунды\n\n"
                        + "P.S. Это просто demo достигаемой после прогрева скорости, хе-хе...\n"
                        + "Для приблизительной оценки одиночного поиска без прогрева запустите RunnerFindOne\n",
                out, count, dict.length, all, one
        );
    }
}
