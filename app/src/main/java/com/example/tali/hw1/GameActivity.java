package com.example.tali.hw1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    private int num_of_cubes, time;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle data = getIntent().getExtras();
        num_of_cubes = data.getInt(LevelsActivity.NUM_OF_CUBES);
        time = data.getInt(LevelsActivity.TIME);
        name = data.getString(EntryActivity.NAME);
    }
}
