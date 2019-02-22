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
    private int M = 1319; //prime number
    public Dictionary() {
        this.M = M;


    }

    private int hash(String key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }
 
    public boolean contains(String input) {
          return wordMap.containsKey(input);
    }
    
    HashMap<String, Integer> wordMap;
            
    public  void tokenize(String input){
        String temp;
        wordMap = new HashMap<>();
        Pattern p = Pattern.compile("\\b[a-zA-Z0-9\\-\\'\\*]+\\b|[\\.\\?\\!]");   
        Matcher m = p.matcher(input);
        while(m.find()){
            temp = m.group().toLowerCase();
            if(wordMap.containsKey(temp)){
                wordMap.put(temp, wordMap.get(temp) + 1);
            }
            else{
                wordMap.put(temp, 1);
            }
        }
    }
    
    public void printSuggestion(String input){
        for(Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            if(entry.getKey()==input){
            double percentage = entry.getValue()/wordMap.size();
            System.out.println(entry.getKey()+" : "+percentage);
            }
        }
    }

    public void build(String filePath) {
        String input = "";
        File dir = new File(filePath);
        for(File file : dir.listFiles()) {
            if(file.isFile()) {
                try {
                    input = input +" "+ new String(Files.readAllBytes(file.toPath()));
                } catch (IOException ex) {
                    System.out.println("no such directory");
                }
            }
         }
         tokenize(input);

    }
}