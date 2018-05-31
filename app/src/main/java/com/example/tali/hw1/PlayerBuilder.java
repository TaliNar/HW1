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

public class PlayerBuilder {
    PlayerViewModel mPlayerViewModel;
    Context mContext;


    public PlayerBuilder(Context context){
        this.mContext = context;
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

    public void addPlayer(String name, double score, Location location){
        mPlayerViewModel = ViewModelProviders.of((FragmentActivity)mContext).get(PlayerViewModel.class);
        Address address = convertToAddress(location);
        mPlayerViewModel.insert(new Player(name, score, address));
    }

}
