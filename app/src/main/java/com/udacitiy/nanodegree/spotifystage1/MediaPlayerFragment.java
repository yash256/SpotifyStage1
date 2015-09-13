package com.udacitiy.nanodegree.spotifystage1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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

    String artistName="Cold Play", albumName="Ghost Stories", track_name="A Sky Full of Stars", albumImage="", previewUrl;
    TextView artistNameTv, albumNameTv, trackNameTv, timeStart, timeRem;
    SeekBar seekBar;
    ImageView albumImageView;
    MediaPlayer mediaPlayer;
    ImageButton prev, playPause, next;
    private final String TAG=MediaPlayerFragment.class.getSimpleName();
    ArrayList<Track> topTracks;
    int trackPosition;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_media_player, container, false);
        Intent intent=getActivity().getIntent();
        topTracks=intent.getParcelableArrayListExtra("TopTracks");
        trackPosition=intent.getIntExtra("Position", 0);
        artistNameTv=(TextView) view.findViewById(R.id.artist_name);
        albumNameTv=(TextView) view.findViewById(R.id.album_name);
        trackNameTv=(TextView) view.findViewById(R.id.track_name);
        albumImageView=(ImageView) view.findViewById(R.id.album_image);
        updateStrings(trackPosition);
        updateUI();
        timeStart=(TextView) view.findViewById(R.id.time_start);
        timeRem=(TextView) view.findViewById(R.id.time_rem);
        timeStart.setText("0:00");
        timeRem.setText("5:00");
        prev=(ImageButton) view.findViewById(R.id.previous);
        playPause=(ImageButton) view.findViewById(R.id.playpause);
        next=(ImageButton) view.findViewById(R.id.next);
        prev.setOnClickListener((View.OnClickListener) this);
        playPause.setOnClickListener((View.OnClickListener) this);
        next.setOnClickListener((View.OnClickListener) this);
        seekBar=(SeekBar) view.findViewById(R.id.seekbar);
        seekBar.setMax(550);
        seekBar.setMax(550);
        callMediaPlayerService();
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
            callMediaPlayerService();
        }
        if(v.getId()==R.id.playpause){
            callMediaPlayerService();
        }
        if(v.getId()==R.id.next){
            if(trackPosition==9){
                trackPosition=0;
            } else {
                trackPosition++;
            }
            updateStrings(trackPosition);
            updateUI();
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
    }
    public void callMediaPlayerService(){
        Intent mediaPlayerService=new Intent(getActivity(), MediaPlayerService.class);
        mediaPlayerService.putExtra("PreviewUrl", previewUrl);
        getActivity().startService(mediaPlayerService);
    }
}
