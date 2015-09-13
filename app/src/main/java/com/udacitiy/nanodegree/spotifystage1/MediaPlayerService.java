package com.udacitiy.nanodegree.spotifystage1;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MediaPlayerService extends Service implements MediaPlayer.OnErrorListener {
    private final String TAG = "MediaPlayerService";
    String nowPlaying;
    MediaPlayer mediaPlayer;

    public MediaPlayerService(){
        mediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onCreate(){
        mediaPlayer=new MediaPlayer();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        String previewUrl = intent.getStringExtra("PreviewUrl");
        String function = intent.getStringExtra("Function");
        if(nowPlaying!=null && previewUrl.equals(nowPlaying)){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        } else {
            mediaPlayer.pause();
            nowPlaying=previewUrl;
            startMediaPlayer();
        }
        return 0;
    }

    public void startMediaPlayer(){
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(nowPlaying);
        } catch (IOException e) {
            Log.e(TAG, "Error in setDataSource");
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
        startMediaPlayer();
        return false;
    }
}
