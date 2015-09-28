package com.udacitiy.nanodegree.spotifystage1;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;


public class TopTracks extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);
        TopTracksFragment topTracksFragment = new TopTracksFragment();
        Intent intent = getIntent();
        String artistId = intent.getStringExtra("artistId");
        Bundle args = new Bundle();
        args.putString("artistId", artistId);
        topTracksFragment.setArguments(args);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_tracks_container, topTracksFragment).commit();
        }


    }

}
