package com.example.spotify.viewmodel;

import android.app.Application;
import android.app.ListActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.spotify.model.Song;

import java.util.List;

public class SongViewModel extends AndroidViewModel {

    private SongRepository mRepository;
    private LiveData<List<Song>> mAllSongs;

    public SongViewModel(@NonNull Application application) {
        super(application);
        mRepository = new SongRepository(application);
        mAllSongs = mRepository.getmAllSongs();
    }

    public LiveData<List<Song>> getmAllSongs(){
        return mAllSongs;
    }

    public void insert(Song song){
        mRepository.insert(song);
    }
}
