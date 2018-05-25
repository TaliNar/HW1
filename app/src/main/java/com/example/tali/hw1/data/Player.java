package com.example.tali.hw1.data;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.util.List;

/**
 * Created by Tali on 25/05/2018.
 */

public class Player{


    private String name;
    private Double score;
    private Address address;

    public Player(String name, double score, Address address) {
        this.name = name;
        this.score = score;
        this.address = address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


    public Address getAddress() {
        return address;
    }


    @Override
    public String toString() {
/*        String street = address.getFeatureName();
        String city = address.getLocality();
        String country = address.getCountryName();*/
        return "name: " + getName() + ", score: " + getScore().toString() + ", Address: " + address.getAddressLine(0);
    }
}
