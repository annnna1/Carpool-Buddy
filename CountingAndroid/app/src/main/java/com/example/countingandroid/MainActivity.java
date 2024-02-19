package com.example.countingandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    TextInputEditText txtIn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.mcwTxt);
        txt.setVisibility(View.INVISIBLE);
        txtIn = findViewById(R.id.fileInput);
    }

    protected String fil2str(String name) {
        BufferedReader f = null;
        String fil = "";
        try {
            f = new BufferedReader(new InputStreamReader(getAssets().open(name)));
            String ch;
            while ((ch = f.readLine()) != null) {
                fil += (ch.toLowerCase() + " ");
            }
        } catch (IOException e) {
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                }
            }
        }
        return fil;
    }

    public void mostCommonWord(View v) throws FileNotFoundException {

            Counter c = new Counter (fil2str("commonWords.txt"), fil2str(txtIn.getText().toString()));
            txt.setTextSize(25);
            txt.setText
            ("The most common word in the text file \"" + txtIn.getText() + "\" is \"" + (c.getWords())[0] + "\" with " + (c.getWordCount()[0]) + " occurrences.");
            txt.setVisibility(View.VISIBLE);
    }

    public void top5(View v) throws FileNotFoundException {
        Counter c = new Counter (fil2str("commonWords.txt"), fil2str(txtIn.getText().toString()));
        String s = ("The most common five words in the text file \"" + txtIn.getText() + "\" are ");
        for(int i = 0 ; i < 5 ; i ++)
        {
            s += ("\n" + (i+1) + ". \"" + c.getWords()[i] + "\" with " + c.getWordCount()[i] + " occurences.");
        }
        txt.setTextSize(25);
        txt.setText(s);
        txt.setVisibility(View.VISIBLE);
    }
}