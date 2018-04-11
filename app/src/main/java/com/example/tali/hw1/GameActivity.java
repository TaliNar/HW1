package com.example.tali.hw1;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {
    private int numOfCubes, maxTime, numOfMatches = 0, sizeOfMatrix;
    private String name;
    private int numClick = 0;
    private int firstClick = -1;
    private int secondClick= -1;
    private GridView gridview;
    private TextView textViewName, textViewTimer;
    private ImageAdapter imageAdapter;
    private boolean timeIsUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle data = getIntent().getExtras();
        numOfCubes = data.getInt(LevelsActivity.NUM_OF_CUBES);
        maxTime = data.getInt(LevelsActivity.TIME);
        name = data.getString(EntryActivity.NAME);

        bindUI();
        startTimer();
    }

    private void bindUI()
    {
        textViewName = findViewById(R.id.textViewName);
        textViewName.setText(name);
        textViewTimer = findViewById(R.id.textViewTimer);
        gridview = findViewById(R.id.gridView);
        if (numOfCubes == LevelsActivity.EASY_NUM_OF_CUBES){
            gridview.setNumColumns(numOfCubes);
            gridview.requestLayout();
        }
        final Integer[] images = getImages();
        imageAdapter = new ImageAdapter(this, images);
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(!((MemoryImageView)v).getState() && !timeIsUp) { // the image is defaultImage
                    numClick++;
                    MemoryImageView img_view = (MemoryImageView) v;
                    img_view.setImageResource(img_view.getImageId());
                    img_view.setState(true);

                    if(numClick % 2 != 0)
                        firstClick = position;
                    else
                        secondClick = position;
                    if(firstClick != -1 && secondClick != -1){
                        checkForCouple();
                    }
                }
            }
        });

    }

    private void gameEnd(){
        String result = "";
        if(numOfMatches == sizeOfMatrix / 2)
            result = getString(R.string.ac_level_win_message);
        // Show game end message
        Toast.makeText(getApplicationContext(), R.string.ac_game_end_message, Toast.LENGTH_LONG).show();
        Intent resIntent = new Intent();
        resIntent.putExtra(LevelsActivity.RESULT, result);
        setResult(RESULT_OK, resIntent);
        finish();
    }

    private void startTimer()
    {
        new CountDownTimer(maxTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutesLeft = (int)(millisUntilFinished/ 1000) / 60;
                int secondsLeft = (int)millisUntilFinished/ 1000;
                textViewTimer.setText(String.format("%d:%02d", minutesLeft , secondsLeft));
            }

            @Override
            public void onFinish() {
                timeIsUp = true;
                textViewTimer.setText(String.format("%d:%02d", 0 , 0));
                gameEnd();
            }
        }.start();
    }

    private void checkForCouple(){
        final MemoryImageView imgView1 = (MemoryImageView)gridview.getChildAt(firstClick);
        final MemoryImageView imgView2 = (MemoryImageView)gridview.getChildAt(secondClick);
        firstClick = secondClick = -1;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(imgView1.getImageId() != imgView2.getImageId()) {
                    imgView1.setImageResource(MemoryImageView.DEFAULT_IMAGE_ID);
                    imgView1.setState(false);
                    imgView2.setImageResource(MemoryImageView.DEFAULT_IMAGE_ID);
                    imgView2.setState(false);
                }
                else
                    numOfMatches++;
            }
        }, 1000);
    }

    private Integer[] getImages()
    {
        if(numOfCubes == LevelsActivity.HARD_NUM_OF_CUBES)
            sizeOfMatrix = numOfCubes * LevelsActivity.MEDIUM_NUM_OF_CUBES;
        else
            sizeOfMatrix = numOfCubes * numOfCubes;

        int half_of_images = sizeOfMatrix/LevelsActivity.EASY_NUM_OF_CUBES;
        Integer[] images = new Integer[sizeOfMatrix];
        System.arraycopy(all_animals, 0, images, 0, half_of_images);
        System.arraycopy(all_animals, 0, images, half_of_images, half_of_images);
        List list = Arrays.asList( images );
        Collections.shuffle(list);
        return (Integer[]) list.toArray();
    }

    // references to our images
    Integer[] all_animals = {
            R.drawable.awl, R.drawable.bear, R.drawable.bird,
            R.drawable.cat, R.drawable.cow, R.drawable.deer,
            R.drawable.fox, R.drawable.lion, R.drawable.monkey,
            R.drawable.pig, R.drawable.rabbit, R.drawable.squirrel,
    };
}
