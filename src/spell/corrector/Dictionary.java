/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spell.corrector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary {

    public boolean contains(String input) {//check if word is in corpus
        return wordMap.containsKey(input);
    }

    HashMap<String, Integer> wordMap;

    public void tokenize(String input) {
        String temp;
        wordMap = new HashMap<>();
        Pattern p = Pattern.compile("\\b[a-zA-Z0-9\\-\\'\\*]+\\b|[\\.\\?\\!]");
        Matcher m = p.matcher(input);
        while (m.find()) {
            temp = m.group().toLowerCase();
            if (wordMap.containsKey(temp)) {
                wordMap.put(temp, wordMap.get(temp) + 1);
            } else {
                wordMap.put(temp, 1);
            }
        }
    }

    public void build(String filePath) { //starts the tokenizing
        String input = "";
        File dir = new File(filePath);
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                try {
                    input = input + " " + new String(Files.readAllBytes(file.toPath()));
                } catch (IOException ex) {
                    System.out.println("no such directory");
                }
            }
        }
        tokenize(input);

    }

    public int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    System.out.println(c1 + " " + c2);
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 2;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        int min = dp[len1][len2];
        return min;
    }
}
