package com.example.tali.hw1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tali.hw1.adapter.PlayerListAdapter;
import com.example.tali.hw1.db.Player;
import com.example.tali.hw1.viewmodel.PlayerViewModel;

import java.util.List;

public class ScoreTable extends AppCompatActivity {

    private PlayerViewModel mPlayerViewModel;
    private String name;
    private double score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_table);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final PlayerListAdapter adapter = new PlayerListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPlayerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        mPlayerViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(@Nullable final List<Player> players) {
                // Update the cached copy of the players in the adapter.
                adapter.setPlayers(players);
            }
        });

    }
}
