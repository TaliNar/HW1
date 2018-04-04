package com.example.tali.hw1;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LevelsActivity extends AppCompatActivity implements View.OnClickListener {
    private String name, age;
    private Button btn_easy, btn_medium, btn_hard;
    private TextView txt_view_header;

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
        txt_view_header = findViewById(R.id.textViewHeader);

        // set textView text with user input
        Resources res = getResources();
        String text = String.format(res.getString(R.string.ac_levels_header), name, age);
        txt_view_header.setText(text);
    }

    @Override
    public void onClick(View view) {

    }
}
