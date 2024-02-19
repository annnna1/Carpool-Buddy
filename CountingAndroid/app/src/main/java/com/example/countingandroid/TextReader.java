package com.example.countingandroid;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TextReader {
    private ArrayList<String> dirty;

    public TextReader(String n) throws FileNotFoundException {
        dirty = new ArrayList<>();
        Scanner scan = new Scanner(n);
        while (scan.hasNext()) {
            String s = scan.next().toLowerCase();
            String w = "";
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if ((c >= 97) && (c <= 122))
                    w += c;
            }
            dirty.add(w);
        }
    }

    public ArrayList<String> getDirty() {
        return dirty;
    }
}