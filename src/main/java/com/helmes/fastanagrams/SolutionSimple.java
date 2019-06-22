package com.helmes.fastanagrams;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class SolutionSimple {
    Collection collectAnagrams(String[] dictionary, String word) {
        if (word.indexOf(' ')>=0)
            return buildPhrase(dictionary,word);
        long pw = 31;
        long sw = 0;
        long p;
        long s;
        Set<String> res = new HashSet<>();

        char[] array = word.toCharArray();
        for (char c : array) {
            pw *= c; //ok, but 8*2=4*4
            sw += c; //fix: 8+2!=4+4
        }
        int length = word.length();

        for (String w : dictionary) {
            if (w.length() == length) {
                p = 31;
                s = 0;
                array = w.toCharArray();
                for (char c : array) {
                    p *= c;
                    s += c;
                }
                //detect anargam
                if (p == pw && s == sw) {
                    res.add(w);
                }
            }
        }
        return res;
    }

    @SuppressWarnings("all")
    private Collection buildPhrase(String[] dictionary, String phrase) {
        String[] words = phrase.split(" ", 2);
        Collection first = collectAnagrams(dictionary, words[0]);
        Collection second = collectAnagrams(dictionary, words[1]); //тут двойная рекурсия
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

}
