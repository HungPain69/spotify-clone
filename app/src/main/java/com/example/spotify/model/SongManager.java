package com.example.spotify.model;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongManager {
    public static List<Song> getFileFromStorage(Context context){

        List<Song> listSong = new ArrayList<>();
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] proj = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID
        };
        Cursor audioCursor = context.getContentResolver().query(uri, proj, null, null,null);
        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    int audioTitle = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                    int audioartist = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                    int audioduration = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                    int audiodata = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    int audioalbum = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                    int audioalbumid = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
                    int song_id = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                    Song info = new Song();
                    info.setPath(audioCursor.getString(audiodata));
                    info.setName(audioCursor.getString(audioTitle));
                    info.setDuration((audioCursor.getString(audioduration)));
                    info.setArtist(audioCursor.getString(audioartist));
                    info.setAlbum(audioCursor.getString(audioalbum));
//                    info.setId(audioCursor.getLong(song_id));
//                    info.setAlbum_art((ContentUris.withAppendedId(albumArtUri, audioCursor.getLong(audioalbumid))).toString());
                    listSong.add(info);
                } while (audioCursor.moveToNext());
            }
        }
        audioCursor.close();

        return listSong;
    };
}
