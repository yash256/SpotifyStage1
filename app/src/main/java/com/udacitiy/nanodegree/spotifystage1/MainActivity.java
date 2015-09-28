package com.udacitiy.nanodegree.spotifystage1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


public class MainActivity extends ActionBarActivity implements MainActivityFragment.Callback {
    boolean mPane;
    private final static String TAG="DFTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.top_tracks_container)!=null){
            mPane=true;
            if(savedInstanceState==null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.top_tracks_container, new TopTracksFragment(), TAG)
                        .commit();
            } else {
                mPane=false;
            }
        }
    }


    @Override
    public void onItemSelected(String artistId) {
        if(mPane){
            Bundle bundleValues = new Bundle();
            bundleValues.putString("artistId", artistId);
            TopTracksFragment topTracksFragment = new TopTracksFragment();
            topTracksFragment.setArguments(bundleValues);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.top_tracks_container, topTracksFragment, TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, TopTracks.class);
            intent.putExtra("artistId", artistId);
            startActivity(intent);
        }
    }
}
