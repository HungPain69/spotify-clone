package com.example.spotify.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String convertDuration(String duration){
        long milis = Long.parseLong(duration);
        SimpleDateFormat simple;
        if(milis >= 60*60*1000 ) {
            simple = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        }else {
            simple = new SimpleDateFormat("mm:ss", Locale.getDefault());
        }
        Date result = new Date(milis);
        return simple.format(result);
    }
}
