package com.example.tali.hw1;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tali.hw1.adapter.PlayerListAdapter;
import com.example.tali.hw1.db.Player;
import com.example.tali.hw1.viewmodel.PlayerViewModel;

import java.util.List;

public class FragmentScoreTable extends Fragment {

    PlayerViewModel mPlayerViewModel;
    PlayerListAdapter adapter;

    View view;
    public FragmentScoreTable() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.scores_table, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new PlayerListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPlayerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        mPlayerViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(@Nullable final List<Player> players) {
                // Update the cached copy of the players in the adapter.
                adapter.setPlayers(players);
            }
        });
        return view;
    }
}



