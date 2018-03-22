package com.example.tali.hw1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "HW1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG,"e-onCreate");
    }

    //onclickListener
    @Override
    public void onClick(View view) {
        Log.e(TAG,"e-onClick");
        String thisTag = view.getTag().toString();
        view.setBackgroundColor(Color.YELLOW);
        // find all view associated with the Activity.
        ViewGroup viewgroup =(ViewGroup)view.getParent();
        for(int  i = 0; i < viewgroup.getChildCount(); i++)
        {
           String tag = viewgroup.getChildAt(i).getTag().toString();
            if(tag.equals(thisTag))
            {
                //TODO: Change thisView and currentView to stars
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"e-onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"e-onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"e-onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"e-onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"e-onDestroy");
    }
}
