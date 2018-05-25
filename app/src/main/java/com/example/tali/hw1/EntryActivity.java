package com.example.tali.hw1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EntryActivity extends AppCompatActivity{
    public static final String NAME = "NAME";
    public static final String AGE = "AGE";
    private EditText editTextName, editTextAge;
    private Button btnStart;
    private boolean isValidInputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        bindUI();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               boolean isContinue = checkInputs();
                if(isContinue)
                    goToLevelsActivity();
            }
        });
    }

    private void bindUI()
    {
        editTextName = findViewById(R.id.editTextName);
        editTextName.requestFocus();
        editTextAge = findViewById(R.id.editTextAge);
        btnStart = findViewById(R.id.buttonStart);
    }

    private boolean checkInputs()
    {
        isValidInputs = true;
        if(editTextName.getText().length() == 0) {
            editTextName.setError("Please enter name");
            isValidInputs = false;
        }
        if(editTextAge.getText().length() == 0) {
            editTextAge.setError("Please enter age");
            isValidInputs = false;
        }
        return isValidInputs;
    }

    private void goToLevelsActivity()
    {
        String name = editTextName.getText().toString();
        String age = editTextAge.getText().toString();

        Intent intent = new Intent(this, LevelsActivity.class);
        intent.putExtra(NAME, name);
        intent.putExtra(AGE, age);
        startActivity(intent);
    }

}
