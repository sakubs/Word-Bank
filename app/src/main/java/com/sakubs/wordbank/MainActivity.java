package com.sakubs.wordbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /* The draggable words in the word bank. */
    protected String[] wordBankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = getResources();
        wordBankList = resources.getStringArray(R.array.nouns);


    }
}
