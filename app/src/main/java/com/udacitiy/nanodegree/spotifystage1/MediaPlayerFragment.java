package com.udacitiy.nanodegree.spotifystage1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;


public class MediaPlayerFragment extends Fragment implements View.OnClickListener{

    String artistName, albumName, track_name, albumImage="", previewUrl;
    TextView artistNameTv, albumNameTv, trackNameTv, timeStart, timeRem;
    SeekBar seekBar;
    ImageView albumImageView;
    MediaPlayer mediaPlayer;
    ImageButton prev, playPause, next;
    private final String TAG=MediaPlayerFragment.class.getSimpleName();
    ArrayList<Track> topTracks;
    int trackPosition;
    Handler seekBarHandler;
    int seekPosition;
    boolean isPlaying;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_media_player, container, false);
        Intent intent=getActivity().getIntent();
        artistName=intent.getStringExtra("ArtistName");
        topTracks=intent.getParcelableArrayListExtra("TopTracks");
        trackPosition=intent.getIntExtra("Position", 0);
        artistNameTv=(TextView) view.findViewById(R.id.artist_name);
        albumNameTv=(TextView) view.findViewById(R.id.album_name);
        trackNameTv=(TextView) view.findViewById(R.id.track_name);
        albumImageView=(ImageView) view.findViewById(R.id.album_image);
        updateStrings(trackPosition);
        timeStart=(TextView) view.findViewById(R.id.time_start);
        timeRem=(TextView) view.findViewById(R.id.time_rem);
        prev=(ImageButton) view.findViewById(R.id.previous);
        playPause=(ImageButton) view.findViewById(R.id.playpause);
        next=(ImageButton) view.findViewById(R.id.next);
        prev.setOnClickListener((View.OnClickListener) this);
        playPause.setOnClickListener((View.OnClickListener) this);
        next.setOnClickListener((View.OnClickListener) this);
        seekBarHandler=new Handler();
        seekBar=(SeekBar) view.findViewById(R.id.seekbar);
        seekBar.setMax(30000);
        seekPosition=-1;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    seekPosition = progress;
                    callMediaPlayerService();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        isPlaying=true;
        updateUI();
        callMediaPlayerService();
        updateSeekBar();
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.previous){
            if(trackPosition==0){
                trackPosition=9;
            } else {
                trackPosition--;
            }
            updateStrings(trackPosition);
            updateUI();
            seekPosition=-1;
            callMediaPlayerService();
        }
        if(v.getId()==R.id.playpause){
            callMediaPlayerService();
            if(isPlaying){
                isPlaying=false;
                playPause.setImageResource(android.R.drawable.ic_media_play);
            } else {
                isPlaying=true;
                playPause.setImageResource(android.R.drawable.ic_media_pause);
            }
        }
        if(v.getId()==R.id.next){
            if(trackPosition==9){
                trackPosition=0;
            } else {
                trackPosition++;
            }
            updateStrings(trackPosition);
            updateUI();
            seekPosition=-1;
            callMediaPlayerService();
        }
    }

    public void updateStrings(int position){
        Track track=topTracks.get(position);
        albumName=track.album.name;
        track_name=track.name;
        previewUrl=track.preview_url;
        if(track.album.images!=null && track.album.images.size()>0) {
            albumImage = track.album.images.get(0).url;
        }
    }
    public void updateUI(){
        artistNameTv.setText(artistName);
        albumNameTv.setText(albumName);
        trackNameTv.setText(track_name);
        if(!albumImage.equals("")){
            Picasso.with(getActivity()).load(albumImage).into(albumImageView);
        }
        seekBar.setProgress(0);
        timeStart.setText("0:00");
        timeRem.setText("0:30");
    }
    public void callMediaPlayerService(){
        Intent mediaPlayerService=new Intent(getActivity(), MediaPlayerService.class);
        mediaPlayerService.putExtra("PreviewUrl", previewUrl);
        mediaPlayerService.putExtra("SeekPosition", seekPosition);
        if(seekPosition>-1){
            mediaPlayerService.putExtra("Seek", true);
            seekBar.setProgress(seekPosition);
        } else {
            mediaPlayerService.putExtra("Seek", false);

        }
        seekPosition=-1;
        getActivity().startService(mediaPlayerService);
    }
    public void updateSeekBar(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int duration = 30000;
                int amountToUpdate = 30;
                if (!(seekBar.getProgress() >= duration) && isPlaying) {
                    int p = seekBar.getProgress();
                    p += amountToUpdate;
                    int sec=p/1000;
                    if(sec<=9){
                        timeStart.setText("0:0"+sec);
                    } else {
                        timeStart.setText("0:"+sec);
                    }
                    seekBar.setProgress(p);

                }
                seekBarHandler.postDelayed(this, amountToUpdate);
            }
        });
    }

}
