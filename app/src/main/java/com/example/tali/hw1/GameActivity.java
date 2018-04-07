package com.example.tali.hw1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

public class GameActivity extends AppCompatActivity {
    private int num_of_cubes, time;
    private String name;
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle data = getIntent().getExtras();
        num_of_cubes = data.getInt(LevelsActivity.NUM_OF_CUBES);
        time = data.getInt(LevelsActivity.TIME);
        name = data.getString(EntryActivity.NAME);

        bindUI();
    }

    private void bindUI()
    {
        gridview = findViewById(R.id.gridView);
        int num_columns = 0;
        switch (num_of_cubes){
            case 4:
                num_columns = 2;
                break;
            case 16:
                num_columns = 4;
                break;
            case 24:
                num_columns = 4;
                break;
        }
        gridview.setNumColumns(num_columns);
        gridview.requestLayout();
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(GameActivity.this, "Selected Position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
