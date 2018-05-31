package com.example.tali.hw1.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.os.AsyncTask;
import android.os.TokenWatcher;
import android.support.annotation.NonNull;

@Database(entities = {Player.class}, version = 1)
@TypeConverters({AddressTypeConverter.class})
public abstract class PlayerRoomDatabase extends RoomDatabase {

    public abstract PlayerDao playerDao();

    private static PlayerRoomDatabase INSTANCE;

    public static PlayerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlayerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlayerRoomDatabase.class, "player_database")
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PlayerDao mDao;

        PopulateDbAsync(PlayerRoomDatabase db) {
            mDao = db.playerDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //mDao.deleteAll();
            ToDo: // Add player that won game
            //Player player = new Player()
            //mDao.insert(player);
            return null;
        }
    }
}
