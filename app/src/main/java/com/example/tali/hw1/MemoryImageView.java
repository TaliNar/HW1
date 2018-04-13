package com.example.tali.hw1;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

/**
 * Created by Tali on 08/04/2018.
 */

public class MemoryImageView extends AppCompatImageView{
    private int imageId;
    final static int DEFAULT_IMAGE_ID = R.drawable.pink_button;
    private boolean state = false; // MemoryImageView is with default image

    public MemoryImageView(Context context){
        super(context);
    }

    public void setImageId (int img_id){
        this.imageId = img_id;
    }

    public int getImageId(){
        return imageId;
    }

    public boolean getState(){
        return state;
    }
    public void setState(boolean flag){
        this.state = flag;
    }
}
