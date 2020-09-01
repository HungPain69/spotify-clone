package com.example.spotify.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.spotify.R;

import com.example.spotify.model.Song;
import com.example.spotify.utils.Constant;
import com.example.spotify.view.PlayActivity;

import java.util.List;

public class PlayService extends Service implements  MediaPlayer.OnPreparedListener{

    private MediaPlayer mediaPlayer;
    private List<Song> listSong;
    private Constant.SongState songState = Constant.SongState.IDLE;
    private final IBinder mBinder = new PlayServiceBinder();



    public class PlayServiceBinder extends Binder{
        public PlayService getService(){
            return PlayService.this;
        }
    }

    private final String CHANNEL_ID = "spotify";
    public PlayService() {
    }

    @Override
    public void onCreate() {
        Log.d("thiendieu", "onCreateService");
        super.onCreate();
        createNotification();
        initMediaPlayer(this);
        mediaPlayer.setOnPreparedListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaPlayer(Context context){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        try {
//            mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setAudioAttributes(new AudioAttributes
                    .Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
        }catch (Exception e){

        }
    }

    private void createNotification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Spotify";
            String description = "Spotify service is running";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Intent notificationIntent = new Intent(this, PlayActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Notification notification =
                    new Notification.Builder(this, CHANNEL_ID)
                            .setContentTitle(name)
                            .setContentText(description)
                            .setSmallIcon(R.drawable.ic_play)
                            .setContentIntent(pendingIntent)
                            .build();
            notificationManager.notify(1, notification);
        }
    }

    public void play(int songIndex){
        Log.d("thiendieu", "song play");
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        Song songSelect = listSong.get(songIndex);
        Uri uri = Uri.parse(songSelect.getPath());
        Log.d("thiendieu", uri+"");

        try{
            mediaPlayer.setDataSource(getApplicationContext(), uri);
        }
        catch(Exception e){
            Log.d("thiendieu", "Error setting data source", e);
        }
        mediaPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        songState = Constant.SongState.PLAYING;
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void pause(){
        mediaPlayer.pause();
        songState = Constant.SongState.PAUSED;
    }

    public void resume(){
        mediaPlayer.start();
        songState = Constant.SongState.PLAYING;
    }

    public void setListSong(List<Song> listSong){
        this.listSong = listSong;
        Log.d("thiendieu","set list songs to service");
    }

    public List<Song> getSong(){
        return listSong;
    }

    public Constant.SongState getSongState(){
        return songState;
    }
}
