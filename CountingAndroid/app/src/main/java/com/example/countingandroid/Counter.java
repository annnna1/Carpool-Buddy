package com.example.countingandroid;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Counter

{
    private String[] words;
    private int[] wordCount;
    public Counter (String common , String file) throws FileNotFoundException {
        StringAnalyser s = new StringAnalyser(common, file);
        ArrayList<String> fin = new ArrayList<>();
        ArrayList<String> clean = s.getClean();
        ArrayList<Integer> counter = new ArrayList<>();

        for(int i = 0 ; i < clean.size() ; i ++)
        {
            boolean b = true;
            for(int j = 0 ; j < i ; j ++)
            {
                if (clean.get(i).equals(clean.get(j)))
                {
                    b = false;
                }
            }
            if(b)
                fin.add(clean.get(i));

        }

        for(int k = 0 ; k < fin.size() ; k++)
        {
            int c = 0;
            for(int l = 0 ; l < clean.size() ; l ++)
            {
                if(fin.get(k).equals(clean.get(l)))
                {
                    c++;
                }
            }
            counter.add(c);
        }

        words = new String[5];
        wordCount = new int[5];
        for(int i = 0 ; i < 5 ; i ++)
        {
            int h = 0;
            int index = 0;
            for(int j = i ; j < counter.size() ; j ++)
            {
                if(counter.get(j)>=h)
                {
                    h = counter.get(j);
                    index = j;
                }
            }
            counter.remove(index);
            counter.add(i,h);
            fin.add(i,fin.get(index));
            fin.remove(index+1);
            wordCount[i] = counter.get(i);
            words[i] = fin.get(i);
        }
    }

    public String[] getWords()
    {
        return words;
    }

    public int[] getWordCount()
    {
        return wordCount;
    }

}