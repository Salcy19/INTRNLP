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
import spell.corrector.Dictionary.Bucket.Node;

public class Dictionary {
    private int M = 1319; //prime number
    final private Bucket[] array;
    public Dictionary() {
        this.M = M;

        array = new Bucket[M];
        for (int i = 0; i < M; i++) {
            array[i] = new Bucket();
        }
    }

    private int hash(String key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    //call hash() to decide which bucket to put it in, do it.
    public void add(String key) {
        array[hash(key)].put(key);
    }

    //call hash() to find what bucket it's in, get it from that bucket. 
    public boolean contains(String input) {
        input = input.toLowerCase();
        return array[hash(input)].get(input);
    }
   public  void tokenize(String input){
        String temp;

        Pattern p = Pattern.compile("\\b[a-zA-Z0-9\\-\\'\\*]+\\b|[\\.\\?\\!]");   
        Matcher m = p.matcher(input);
        System.out.print("[");
        ArrayList<String> wordArray = new ArrayList<>();
        HashMap<String, Integer> wordMap;
        wordMap = new HashMap<>();
        int vocab = 0;
        while(m.find()){
            temp = m.group().toLowerCase();
            add(temp);
            System.out.println("temp = " + temp);
        }
        for(Map.Entry<String, Integer> entry : wordMap.entrySet()) {
                System.out.print(entry.getKey()+", ");
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
    //this method is used in my unit tests
    public String[] getRandomEntries(int num){
        String[] toRet = new String[num];
        for (int i = 0; i < num; i++){
            //pick a random bucket, go out a random number 
            Node n = array[(int)Math.random()*M].first;
            int rand = (int)Math.random()*(int)Math.sqrt(num);

            for(int j = 0; j<rand && n.next!= null; j++) n = n.next;
            toRet[i]=n.word;


        }
        return toRet;
    }

    class Bucket {

        private Node first;

        public boolean get(String in) {         //return key true if key exists
            Node next = first;
            while (next != null) {
                if (next.word.equals(in)) {
                    return true;
                }
                next = next.next;
            }
            return false;
        }

        public void put(String key) {
            for (Node curr = first; curr != null; curr = curr.next) {
                if (key.equals(curr.word)) {
                    return;                     //search hit: return
                }
            }
            first = new Node(key, first); //search miss: add new node
        }

        class Node {

            String word;
            Node next;

            public Node(String key, Node next) {
                this.word = key;
                this.next = next;
            }

        }
 
    }
}