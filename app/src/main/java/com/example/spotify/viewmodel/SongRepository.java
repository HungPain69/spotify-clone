package com.example.spotify.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.spotify.model.Song;
import com.example.spotify.model.SongDAO;
import com.example.spotify.model.SongRoomDatabase;

import java.util.List;

public class SongRepository {
    private SongDAO mSongDAO;
    private LiveData<List<Song>> mAllSongs;

    SongRepository(Application application){
        SongRoomDatabase db= SongRoomDatabase.getINSTANCE(application);
        mSongDAO = db.songDAO();
        mAllSongs = mSongDAO.getAlphabetizeSongs();
    }

    LiveData<List<Song>> getmAllSongs(){
        return mAllSongs;
    }

    public void insert(Song song){
        SongRoomDatabase.databaseWriterExecutor.execute(()->{
            mSongDAO.insert(song);
        });
    }
}
