package com.example.spotify.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Song.class}, version = 1, exportSchema = false)
public abstract class SongRoomDatabase extends RoomDatabase {

    public abstract SongDAO songDAO();
    private static volatile  SongRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(5);

    public static SongRoomDatabase getINSTANCE(final Context context){
        if(INSTANCE == null){
            synchronized (SongRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SongRoomDatabase.class, "song_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
