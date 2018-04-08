package com.example.tali.hw1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import java.util.*;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {
    private int num_of_cubes, time;
    private String name;
    private int numClick = 0;
    private int firstClick = -1;
    private int secondClick= -1;
    private GridView gridview;
    private ImageAdapter imageAdapter;

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
        if (num_of_cubes == LevelsActivity.EASY_NUM_OF_CUBES){
            gridview.setNumColumns(num_of_cubes);
            gridview.requestLayout();
        }
        final Integer[] images = getImages();
        imageAdapter = new ImageAdapter(this, images);
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(!((MemoryImageView)v).getState()) { // the image is defaultImage
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
            }
        }, 1000);
    }

    private Integer[] getImages()
    {
        int size;
        if(num_of_cubes == LevelsActivity.HARD_NUM_OF_CUBES)
            size = num_of_cubes * LevelsActivity.MEDIUM_NUM_OF_CUBES;
        else
            size = num_of_cubes * num_of_cubes;

        int half_of_images = size/LevelsActivity.EASY_NUM_OF_CUBES;
        Integer[] images = new Integer[size];
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
