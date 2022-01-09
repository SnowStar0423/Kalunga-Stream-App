package com.colombia.kalunga.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

public class StreamService extends Service {
    MediaPlayer myPlayer;
    int length;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        String  myUri = "https://kalunga.puertorrostereo.vip/stream";

        myPlayer = MediaPlayer.create(this, Uri.parse(myUri));
//        myPlayer.setLooping(false); // Set looping

    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startid) {
        String action = intent.getStringExtra("action");

        if (action.equalsIgnoreCase("play")) {
//            myPlayer.setVolume(0.8f, 0.8f);
            myPlayer.start();
        } else if (action.equalsIgnoreCase("resume")) {
            myPlayer.start();
        } else if (action.equalsIgnoreCase("pause")) {
            myPlayer.pause();
            length = myPlayer.getCurrentPosition();
        } else if (action.equalsIgnoreCase("setVolume")) {
            int vol = intent.getIntExtra("volume", 1);
            double volume = ((double) vol / 100);
            myPlayer.setVolume(Float.parseFloat(String.valueOf(volume)),Float.parseFloat(String.valueOf(volume)));
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onLowMemory(){
        myPlayer.release();
    }

    @Override
    public void onDestroy() {
        myPlayer.stop();
        myPlayer.release();
        stopSelf();
        super.onDestroy();
    }
}

