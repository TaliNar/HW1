package com.example.tali.hw1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LevelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        Bundle data = getIntent().getExtras();
        String name = data.getString(EntryActivity.NAME);
        String age = data.getString(EntryActivity.AGE);


    }
}
