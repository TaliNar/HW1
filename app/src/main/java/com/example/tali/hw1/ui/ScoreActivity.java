package com.example.tali.hw1.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.example.tali.hw1.R;
import com.example.tali.hw1.adapters.ViewPagerAdapter;

public class ScoreActivity extends FragmentActivity {
    FragmentScoreMap fragmentScoreMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        bindUI();
    }

    private void bindUI() {
        TabLayout tabLayout = findViewById(R.id.tableLayout_id);
        ViewPager viewPager = findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.addFragment(new FragmentScoreTable(), "Table");
        fragmentScoreMap = new FragmentScoreMap();
        adapter.addFragment(fragmentScoreMap, "Map");
        //Adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
