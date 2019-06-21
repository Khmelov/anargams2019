package com.helmes.anagrams;

import java.util.*;
import java.util.stream.*;

public class Solution {

    public static void main(String[] args) {
        Solution demo = new Solution();
        String[] dictionary = {
                "тесто", "тавро", "тавры", "ворот",
                "товары", "товар", "втора", "тавро",
                "рвота", "отвар", "12345", "hi"
        };
        String word = "автор";
        Collection result = demo.collectAnagrams(dictionary, word);
        System.out.printf("Для слова '%s' анаграммы: %s\n", word, result);
        // прочитать словарь тут будет неудобно, т.к. ограничения
        // на импорты не дают это нормально сделать
        // смотрите применение в классах com.helmes.Runner...
    }

    //определим способ: direct - в лоб, smart - с кешем, split - для предложений
    Collection collectAnagrams(String[] dictionary, String searchAnagramsFor) {
        if (searchAnagramsFor.indexOf(' ') < 0) {
            Set<String> words = smartCollect(dictionary, searchAnagramsFor);
            return words == null
                    ? directCollect(dictionary, searchAnagramsFor)
                    : words;
        } else {
            return splitAndCollect(dictionary, searchAnagramsFor);
        }
    }

    //----------------- начало решения ---------------------------------
    private Collection directCollect(String[] dictionary, String word) {
        long id = getId((word));
        int length = word.length();
        return Arrays
                .stream(dictionary)
                .parallel()
                .filter(w -> w.length() == length && getId(w) == id)
                .collect(Collectors.toSet());
    }

    long getId(String word) {
        //если регистр не важен, то нужно раскомментировать строку:
        //word=word.toLowerCase();
        long id = 31;
        char[] array = word.toCharArray();
        for (char c : array) id *= c;
        return id;
    }
    //----------------- конец решения ---------------------------------

    /*
     FIX для оговорки в конце задания:
     При условии, что исходный элемент состоит из нескольких слов, ожидается,
     что и анаграммы на выходе будут представлять собой многословные конструкции
     */
    @SuppressWarnings(value = "all")
    private Collection splitAndCollect(String[] dictionary, String phrase) {
        String[] words = phrase.split(" ", 2);
        Collection first = collectAnagrams(dictionary, words[0]);
        Collection second = collectAnagrams(dictionary, words[1]); //тут рекурсия
        if (first.size() == 0) first.add(words[0]);
        if (second.size() == 0) second.add(words[1]);
        Collection<String> sentences = new HashSet<>();
        for (Object one : first) {
            for (Object two : second) {
                sentences.add(one.toString().concat(" ").concat(two.toString()));
            }
        }
        return sentences;
    }

    /*
    --------------------------------------------------------------------------------------------------
    Все что ниже, это уже навороты - разгон для повторного опроса словаря,
    это сделано, т.к. сказано, что словарь по которому производится поиск подразумевается один
    поэтому, видимо, нет смысла многократно обрабатывать его.

    Впрочем, указание другого словаря приведет к автоматическому обновлению кеша.

    Если же имелось ввиду, что словарь один, но он mutable по значениям,
    то тогда метод directCollect выше - это и есть решение,
    его производительность на заданном словаре pldf-win.txt примерно 25 ms на двух ядрах ноута.
    Будет расти с мощностью CPU

    Динамическую валидацию кеша тоже конечно сделать нетрудно,
    в кеш нужно размешать тогда не сами стринги, а их индексы в словаре
    и далее check mutable, но это наверное уже выходит за рамки задачи
    --------------------------------------------------------------------------------------------------
    */

    private Map<Long, Set<String>> cache;
    private String[] prevDictionary;
    private int counter = 0;

    private Set<String> smartCollect(String[] dictionary, String search) {
        final int BUILD_AFTER_COUNT = 3;
        counter++;
        if (dictionary != prevDictionary) {
            counter = 0;
            prevDictionary = dictionary;
        }
        if (counter > BUILD_AFTER_COUNT) {
            counter = BUILD_AFTER_COUNT;
            return cache.get(getId(search));
        } else if (counter == BUILD_AFTER_COUNT) {
            cache = new HashMap<>(dictionary.length * 3 / 2);
            return smartCollectAnagramsAndCache(dictionary, search);
        } else {
            return null;
        }
    }


    private Set<String> smartCollectAnagramsAndCache(
            String[] dictionary, String search) {
        long id = getId(search);
        return Arrays
                .stream(dictionary)
                .filter(word -> getIdAndCache(word) == id)
                .collect(Collectors.toSet());
    }

    private long getIdAndCache(String word) {
        long id = getId(word);
        cache.computeIfAbsent(id, k -> new HashSet<>()).add(word);
        return id;
    }

}

