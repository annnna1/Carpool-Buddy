package com.example.countingandroid;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class StringAnalyser {

    private ArrayList<String> common;
    private ArrayList<String> clean;

    public StringAnalyser(String c, String n) throws FileNotFoundException {
        TextReader t = new TextReader(n);
        ArrayList<String> dirty = t.getDirty();

        common = new ArrayList<>();
        Scanner scan = new Scanner(c);
        while (scan.hasNext())
        {
            String s = scan.next().toLowerCase();
            common.add(s);
        }

        clean = new ArrayList<>();
        for (int i = 0; i < dirty.size(); i++)
        {
            boolean b = false;
            for (int j = 0; j < common.size(); j++)
            {
                if (dirty.get(i).equals(common.get(j)))
                {
                    b = true;
                }
            }
            if (!b)
                clean.add(dirty.get(i));
        }

    }

    public ArrayList<String> getClean()
    {
        return clean;
    }

    public ArrayList<String> getCommon()
    {
        return common;
    }
}