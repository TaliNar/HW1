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

    @Query("SELECT * from Player ORDER BY score ASC")
    LiveData<List<Player>> getAllPlayers();

}
