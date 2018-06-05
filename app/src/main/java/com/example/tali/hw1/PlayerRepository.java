package com.example.tali.hw1;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.tali.hw1.db.PlayerDao;
import com.example.tali.hw1.db.Player;
import com.example.tali.hw1.db.PlayerRoomDatabase;

import java.util.List;

public class PlayerRepository {

    private PlayerDao mplayerDao;
    private LiveData<List<Player>> mAllPlayers;

    public PlayerRepository(Application application) {
        PlayerRoomDatabase db = PlayerRoomDatabase.getDatabase(application);
        mplayerDao = db.playerDao();
        mAllPlayers = mplayerDao.getAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return mAllPlayers;
    }


    public void delete(Player player){
        mplayerDao.delete(player);
    }

    public Player getPlayerWithLowestScore(){
        return mplayerDao.getPlayerWithLowestScore();
    }

    public int getNumOfPlayers(){
        return mplayerDao.getNumOfPlayers();
    }

    public List<Player> getListOfPlayers(){
        return mplayerDao.getListOfPlayers();
    }

    public void deleteAll(){
        mplayerDao.deleteAll();
    }

    public Player getPlayer(int id){
        return mplayerDao.getPlayer(id);
    }

    public void insert (Player player) {
        new insertAsyncTask(mplayerDao).execute(player);
    }

    private static class insertAsyncTask extends AsyncTask<Player, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        insertAsyncTask(PlayerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Player... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
