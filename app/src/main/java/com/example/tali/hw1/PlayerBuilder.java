package com.example.tali.hw1;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.tali.hw1.db.Player;
import com.example.tali.hw1.viewmodel.PlayerViewModel;

import java.io.IOException;
import java.util.List;

public class PlayerBuilder implements Runnable{
    PlayerViewModel mPlayerViewModel;
    Context mContext;
    String name;
    Location location;
    double score;

    public PlayerBuilder(Context context, String name, double score, Location location){
        this.mContext = context;
        this.name = name;
        this.score = score;
        this.location = location;
    }

    public Address convertToAddress(Location location){
        Geocoder geocoder = new Geocoder(mContext);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        mPlayerViewModel = ViewModelProviders.of((FragmentActivity)mContext).get(PlayerViewModel.class);
        Address address = convertToAddress(location);
        if(mPlayerViewModel.getNumOfPlayers() == 10){
            Player lowPlayer = mPlayerViewModel.getPlayerWithLowestScore();
            if(lowPlayer.getScore() < score) {
                mPlayerViewModel.delete(lowPlayer);
                mPlayerViewModel.insert(new Player(name, score, address));
            }
        }
        else{
            mPlayerViewModel.insert(new Player(name, score, address));
        }
    }
}
