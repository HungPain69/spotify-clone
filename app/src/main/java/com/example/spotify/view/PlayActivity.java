package com.example.spotify.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.example.spotify.R;
import com.example.spotify.databinding.ActivityPlayBinding;
import com.example.spotify.service.PlayService;

public class PlayActivity extends AppCompatActivity {
    private ActivityPlayBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        getSupportActionBar().hide();

    }
}