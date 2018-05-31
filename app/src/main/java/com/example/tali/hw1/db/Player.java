package com.example.tali.hw1.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Address;


/**
 * Created by Tali on 25/05/2018.
 */
@Entity
public class Player{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "score")
    private Double score;
    @ColumnInfo(name = "address")
    private Address address;

    public Player(String name, double score, Address address) {
        this.name = name;
        this.score = score;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
