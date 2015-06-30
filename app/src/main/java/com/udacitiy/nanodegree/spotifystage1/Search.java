package com.udacitiy.nanodegree.spotifystage1;

import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

public class Search extends ActionBarActivity {
    ArtistDataAdapter adapter;
    private final String TAG="Search";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        List<String> artists=new ArrayList<String>();
        List<Artist> artistsList=new ArrayList<Artist>();
        artists.add("Artist1");
        adapter=new ArtistDataAdapter(this, artistsList);
        ListView artistListView = (ListView) findViewById(R.id.artist_listview);
        artistListView.setAdapter(adapter);
        EditText artistEditTest=(EditText) findViewById(R.id.search_text);
        artistEditTest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchQuery=v.getText().toString();
                Toast.makeText(getApplicationContext(), "Search for string " + searchQuery, Toast.LENGTH_LONG).show();
                new SpotifyData().execute(searchQuery);
                return true;
            }
        });
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist curItem= (Artist) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Item clicked: "+curItem.name, Toast.LENGTH_LONG).show();
            }
        });
    }
    class SpotifyData extends AsyncTask<String, Void, List<Artist>>{

        @Override
        protected List<Artist> doInBackground(String... params) {
            String artistName=params[0];
            SpotifyApi api=new SpotifyApi();
            SpotifyService service=api.getService();
            ArtistsPager results=service.searchArtists(artistName);
            List<Artist> artists=results.artists.items;
            return artists;
        }
        @Override
        protected void onPostExecute(List<Artist> artists){
            if(artists!=null){
                adapter.clear();
                adapter.addAll(artists);
            }else{
                Log.d(TAG, "No artists found");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
