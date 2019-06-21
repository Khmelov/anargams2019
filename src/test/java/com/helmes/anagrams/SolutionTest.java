package com.helmes.anagrams;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class SolutionTest {

    private String[] dict;
    private Solution solution;

    @Before
    public void setUp() throws Exception{
        URI uriDictionary = Objects.requireNonNull(Solution
                .class
                .getClassLoader()
                .getResource("pldf-win.txt")).toURI();
        dict = Files
                .lines(Paths.get(uriDictionary), Charset.forName("CP1251"))
                .toArray(String[]::new);
        solution = new Solution();
    }

    @Test
    public void checkGetIdMethod() {
        Set<Long> idStrings = new HashSet<>();
        Set<String> sortedChars = Arrays.stream(dict)
                .map(w -> {
                    idStrings.add(solution.getId(w));
                    char[] array = w.toCharArray();
                    Arrays.sort(array);
                    return new String(array);
                }).collect(Collectors.toSet());
        Assert.assertEquals(
                "Попал! Редко но метко, на таком словаре метод getId() " +
                        "потребует устранения коллизий (перепроверки строк)",
                sortedChars.size(), idStrings.size()
        );
        System.out.println("Ok. sortedChars.size() = " + sortedChars.size());
    }
}
