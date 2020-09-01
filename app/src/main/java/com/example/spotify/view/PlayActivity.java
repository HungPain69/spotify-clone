package com.example.spotify.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.example.spotify.R;
import com.example.spotify.databinding.ActivityPlayBinding;
import com.example.spotify.model.Song;
import com.example.spotify.service.PlayService;
import com.example.spotify.utils.Constant;
import com.example.spotify.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityPlayBinding mBinding;
    private ArrayList<Song > mListSong;
    private int positionSongPicked;
    private PlayService playService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        getSupportActionBar().hide();



        Bundle bundle = getIntent().getExtras();
        positionSongPicked = bundle.getInt(Constant.positionSongPicked, -1);
        mListSong = bundle.getParcelableArrayList(Constant.listAllSong);

        bindPlayService();
        initView();

    }

    private void initView(){
        Song song = mListSong.get(positionSongPicked);
        mBinding.ivPlay.setOnClickListener(this);
        mBinding.tvDuration.setText(Utils.convertDuration(song.getDuration()));
        mBinding.tvSongName.setText(song.getName());
        mBinding.tvArtistName.setText(song.getArtist());
    }

    private void bindPlayService(){
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                playService = ((PlayService.PlayServiceBinder) service).getService();
                playService.setListSong(mListSong);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent playIntent = new Intent(this, PlayService.class);
        bindService(playIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play:
                if(playService.getSongState() == Constant.SongState.PLAYING && playService.isPlaying()){
                    mBinding.ivPlay.setImageResource(R.drawable.ic_play);
                    playService.pause();
                }else{
                    if(playService.getSongState() == Constant.SongState.PAUSED) {
                        mBinding.ivPlay.setImageResource(R.drawable.ic_pause);
                        playService.resume();
                    }
                }
                break;
        }
    }
}