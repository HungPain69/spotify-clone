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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.spotify.R;
import com.example.spotify.databinding.ActivityMainBinding;
import com.example.spotify.model.Song;
import com.example.spotify.model.SongManager;
import com.example.spotify.service.PlayService;
import com.example.spotify.utils.Constant;
import com.example.spotify.viewmodel.SongViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SongViewModel mViewModel;
    private Adapter mAdapter;
    private Context mContext;
    private List<Song> mListSongFromStorage;
    private List<Song> mListAllSong;                //from database
    private ActivityMainBinding mBinding;
    PlayService binder;

    IsetOnClickListener mOnClick = new IsetOnClickListener() {
        @Override
        public void onClickListener(int index) {
            Song song = mListSongFromStorage.get(index);
            Toast.makeText(mContext, song.getPath() + "", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, PlayActivity.class);

            Bundle bundle = new Bundle();
            bundle.putInt(Constant.positionSongPicked , index);
            bundle.putParcelableArrayList(Constant.listAllSong, (ArrayList<? extends Parcelable>) mListAllSong);
            intent.putExtras(bundle);

            binder.play(index);
            startActivity(intent);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mContext = this;

        requestPermission();

        mListSongFromStorage = new ArrayList<>();
        mListAllSong = new ArrayList<>();
        mListSongFromStorage = SongManager.getFileFromStorage(mContext);

        initView();
        registerViewModel();
        bindPlayService();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (Song song : mListSongFromStorage) {
                    mViewModel.insert(song);
                }
                break;
        }
    }

    private void registerViewModel() {
        mViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        mViewModel.getmAllSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mListAllSong = songs;
                mAdapter.setData(mListAllSong);
                mBinding.recylceView.setAdapter(mAdapter);
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this
                , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }


    private void initView() {
        mAdapter = new Adapter(mContext, mOnClick);
        mBinding.recylceView.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mBinding.recylceView.addItemDecoration(divider);

    }

    private void bindPlayService(){
         ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = ((PlayService.PlayServiceBinder) service).getService();
                binder.setListSong(mListAllSong);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent playIntent = new Intent(this, PlayService.class);
        bindService(playIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
}