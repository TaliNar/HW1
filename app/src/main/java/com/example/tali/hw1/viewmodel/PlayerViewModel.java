package com.example.tali.hw1.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.tali.hw1.PlayerRepository;
import com.example.tali.hw1.db.Player;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private PlayerRepository mPlayerRepository;
    private LiveData<List<Player>> mAllPlayers;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        mPlayerRepository = new PlayerRepository(application);
        mAllPlayers = mPlayerRepository.getAllPlayers();
    }

    public void delete(Player player){
        mPlayerRepository.delete(player);
    }

    public Player getPlayerWithLowestScore(){
        return mPlayerRepository.getPlayerWithLowestScore();
    }

    public int getNumOfPlayers(){
        return mPlayerRepository.getNumOfPlayers();
    }

    public List<Player> getListOfPlayers(){
        return mPlayerRepository.getListOfPlayers();
    }

    public void deleteAll(){
        mPlayerRepository.deleteAll();
    }

    public Player getPlayer(int id){
        return mPlayerRepository.getPlayer(id);
    }

    public LiveData<List<Player>> getAllPlayers() { return mAllPlayers; }

    public void insert(Player player) { mPlayerRepository.insert(player); }
}
