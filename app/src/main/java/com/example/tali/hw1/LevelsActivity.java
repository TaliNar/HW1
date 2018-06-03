package com.example.tali.hw1;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LevelsActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String NUM_OF_CUBES = "NUM_OF_CUBES";
    public static final String TIME = "TIME";
    public static final int EASY_NUM_OF_CUBES = 2, EASY_TIME = 30;
    public static final int MEDIUM_NUM_OF_CUBES = 4, MEDIUM_TIME = 45;
    public static final int HARD_NUM_OF_CUBES = 6, HARD_TIME = 60;
    public static final String RESULT = "RESULT";
    public static final int REQUEST_CODE = 1;
    private String name, age, headerText;
    private Button btn_easy, btn_medium, btn_hard, btn_scores;
    private TextView txt_view_header;
    int num_of_cubes, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        Bundle data = getIntent().getExtras();
        name = data.getString(EntryActivity.NAME);
        age = data.getString(EntryActivity.AGE);
        boundUI();
    }

    private void boundUI()
    {
        btn_easy = findViewById(R.id.buttonEasy);
        btn_easy.setOnClickListener(this);
        btn_medium = findViewById(R.id.buttonMedium);
        btn_medium.setOnClickListener(this);
        btn_hard = findViewById(R.id.buttonHard);
        btn_hard.setOnClickListener(this);
        btn_scores = findViewById(R.id.buttonScores);
        btn_scores.setOnClickListener(this);
        txt_view_header = findViewById(R.id.textViewHeader);

        // set textView text with user input
        Resources res = getResources();
        headerText = String.format(res.getString(R.string.ac_levels_header), name, age);
        txt_view_header.setText(headerText);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.buttonEasy:
                num_of_cubes = EASY_NUM_OF_CUBES;
                time = EASY_TIME;
                break;
            case R.id.buttonMedium:
                num_of_cubes = MEDIUM_NUM_OF_CUBES;
                time = MEDIUM_TIME;
                break;
            case R.id.buttonHard:
                num_of_cubes = HARD_NUM_OF_CUBES;
                time = HARD_TIME;
                break;
            case R.id.buttonScores:
                goToScoreActivity();
                return;
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EntryActivity.NAME, name);
        intent.putExtra(NUM_OF_CUBES, num_of_cubes);
        intent.putExtra(TIME, time);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode != RESULT_CANCELED){
            String result = data.getStringExtra(LevelsActivity.RESULT);
            if(!result.equals("")){
                txt_view_header.setText(getString(R.string.ac_level_win_message));
            }
            else
                txt_view_header.setText(headerText);
        }
    }

    private void goToScoreActivity()
    {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra(EntryActivity.NAME, name);
        startActivity(intent);
    }

}
