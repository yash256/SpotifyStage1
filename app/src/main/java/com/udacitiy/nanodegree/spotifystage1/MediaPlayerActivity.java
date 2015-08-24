package com.udacitiy.nanodegree.spotifystage1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.appcompat.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MediaPlayerActivity extends Activity {

    String artistName="Cold Play", albumName="Ghost Stories", track_name="A Sky Full of Stars", albumImage="";
    TextView artistNameTv, albumNameTv, trackNameTv;
    SeekBar seekBar;
    ImageView albumImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        Intent intent=getIntent();
        track_name=intent.getStringExtra("TrackName");
        albumName=intent.getStringExtra("AlbumName");
        albumImage=intent.getStringExtra("Image");
        artistNameTv=(TextView) findViewById(R.id.artist_name);
        albumNameTv=(TextView) findViewById(R.id.album_name);
        trackNameTv=(TextView) findViewById(R.id.track_name);
        albumImageView=(ImageView) findViewById(R.id.album_image);
        artistNameTv.setText(artistName);
        albumNameTv.setText(albumName);
        trackNameTv.setText(track_name);
        if(!albumImage.equals("")){
            Picasso.with(this).load(albumImage).into(albumImageView);
        }
        seekBar=(SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(550);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
