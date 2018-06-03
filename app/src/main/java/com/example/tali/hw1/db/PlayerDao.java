package com.example.tali.hw1.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;


@Dao
public interface PlayerDao {
    @Insert
    void insert(Player player);

    @Delete
    void delete(Player player);

    @Update
    void update(Player player);

    @Query("DELETE FROM Player")
    void deleteAll();

    @Query("SELECT * from Player")
    LiveData<List<Player>> getAllPlayers();

    @Query("SELECT * from Player")
    List<Player> getListOfPlayers();

    @Query("SELECT * from Player WHERE score = (SELECT min(score) from Player)")
    Player getPlayerWithLowestScore();

    @Query("SELECT * FROM player where id = :id")
    Player getPlayer(int id);

    @Query("SELECT count(name) FROM player")
    int getNumOfPlayers();
}
