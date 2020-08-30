package com.example.spotify.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.spotify.R;
import com.example.spotify.databinding.ActivityMainBinding;
import com.example.spotify.model.Song;
import com.example.spotify.model.SongManager;
import com.example.spotify.viewmodel.SongViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SongViewModel mViewModel;
    private Adapter mAdapter;
    private Context mContext;
    List<Song> mListAllSong;
    private ActivityMainBinding mBinding;

    IsetOnClickListener mOnClick = new IsetOnClickListener() {
        @Override
        public void onClickListener(int index) {
            Song song = mListAllSong.get(index);
            Toast.makeText(mContext, song.getPath()+"",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mContext = this;

        requestPermission();

        mListAllSong = new ArrayList<>();
        mListAllSong = SongManager.getFileFromStorage(mContext);

        initView();
        registerViewModel();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                for(Song song: mListAllSong){
                    mViewModel.insert(song);
                }
                break;
        }
    }

    private void registerViewModel() {
        mViewModel =  new ViewModelProvider(this).get(SongViewModel.class);
        mViewModel.getmAllSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mAdapter.setData(songs);
                mBinding.recylceView.setAdapter(mAdapter);
            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this
                ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                ,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }



    private void initView(){
        mAdapter = new Adapter(mContext, mOnClick);
        mBinding.recylceView.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mBinding.recylceView.addItemDecoration(divider);

    }
}