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

import java.lang.reflect.Array;
import java.util.*;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {
    private int numOfCubes, maxTime, numOfMatches = 0, sizeOfMatrix;
    private String name;
    private int numClick = 0;
    private int firstClick = -1, secondClick= -1;
    private int borderColor;
    private GridView gridview;
    private TextView textViewName, textViewTimer;
    private ImageAdapter imageAdapter;
    private boolean timeIsUp = false;
    private CountDownTimer countDownTimer;
    private Runnable matchRunnable;
    private Handler handler;
    private Random rnd = new Random();

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

        // In easy level, choose the number of columns to be 2
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
                // The image is defaultImage and Timer is still running
                if(!((MemoryImageView)v).getState() && !timeIsUp) {
                    numClick++;
                    MemoryImageView img_view = (MemoryImageView) v;
                    // Show imageView animal picture
                    img_view.setImageResource(img_view.getImageId());
                    img_view.setState(true);

                    if(numClick % 2 != 0) {
                        firstClick = position;
                        // Mark card, each two cards marked with the same color
                        borderColor = rnd.nextInt();
                        setBorderColor(img_view, borderColor);
                    }
                    else {
                        secondClick = position;
                        setBorderColor(img_view, borderColor);
                    }
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
        Intent resIntent = new Intent();
        resIntent.putExtra(LevelsActivity.RESULT, result);
        setResult(RESULT_OK, resIntent);
        finish();
    }

    private void startTimer()
    {
        countDownTimer = new CountDownTimer(maxTime * 1000, 1000) {
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
                // Show game end message
                Toast.makeText(getApplicationContext(), R.string.ac_game_end_message, Toast.LENGTH_LONG).show();
                gameEnd();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent resIntent = new Intent();
        setResult(RESULT_CANCELED, resIntent);
        if(countDownTimer != null)
            countDownTimer.cancel();
        if(handler != null && matchRunnable != null)
            handler.removeCallbacks(matchRunnable);
        finish();
    }

    /** Mark or remove marking**/
    private void setBorderColor(MemoryImageView imageView, int borderColor){
        imageView.setCropToPadding(true);
        imageView.setBackgroundColor(borderColor);
    }

    private void checkForCouple(){
        final MemoryImageView imgView1 = (MemoryImageView)gridview.getChildAt(firstClick);
        final MemoryImageView imgView2 = (MemoryImageView)gridview.getChildAt(secondClick);
        firstClick = secondClick = -1;
        handler = new Handler();
        matchRunnable = new Runnable() {
            @Override
            public void run() {
                // If images are not equal return to default pictures
                if (imgView1.getImageId() != imgView2.getImageId()) {
                    imgView1.setImageResource(MemoryImageView.DEFAULT_IMAGE_ID);
                    imgView1.setState(false);
                    imgView2.setImageResource(MemoryImageView.DEFAULT_IMAGE_ID);
                    imgView2.setState(false);
                } else
                    numOfMatches++;
                if(numOfMatches == sizeOfMatrix / 2){ // User Won
                    gameEnd();
                }
                // Remove marking
                setBorderColor(imgView1, getResources().getColor(R.color.colorBackGroundWhite));
                setBorderColor(imgView2, getResources().getColor(R.color.colorBackGroundWhite));
            }
        };
       handler.postDelayed(matchRunnable, 1000);
    }

    private Integer[] getImages()
    {
        // Decide size of matrix according to numOfCubes(from LevelsActivity)
        if(numOfCubes == LevelsActivity.HARD_NUM_OF_CUBES)
            sizeOfMatrix = numOfCubes * LevelsActivity.MEDIUM_NUM_OF_CUBES;
        else
            sizeOfMatrix = numOfCubes * numOfCubes;

        // Shuffle all animals pictures
        List shuffledList = Arrays.asList(all_animals);
        Collections.shuffle(shuffledList);
        Integer[] arrayFrom = (Integer[])shuffledList.toArray();

        int half_of_images = sizeOfMatrix/LevelsActivity.EASY_NUM_OF_CUBES;
        Integer[] images = new Integer[sizeOfMatrix];
        // Copy half_of_images from all_animals array to images array
        System.arraycopy(arrayFrom, 0, images, 0, half_of_images);
        // Copy the same images, to get pairs of animals
        System.arraycopy(arrayFrom, 0, images, half_of_images, half_of_images);

        // Shuffle images array
        List list = Arrays.asList( images );
        Collections.shuffle(list);
        return (Integer[]) list.toArray();
    }

    // References to animal images
    Integer[] all_animals = {
            R.drawable.awl, R.drawable.bear, R.drawable.bird,
            R.drawable.cat, R.drawable.cow, R.drawable.deer,
            R.drawable.fox, R.drawable.lion, R.drawable.monkey,
            R.drawable.pig, R.drawable.rabbit, R.drawable.squirrel,
    };
}
