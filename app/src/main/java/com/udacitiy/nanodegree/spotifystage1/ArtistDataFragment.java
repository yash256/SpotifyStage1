package com.udacitiy.nanodegree.spotifystage1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;

public class ArtistDataFragment extends Fragment {
    ArtistDataAdapter adapter;
    private final String TAG="ArtistDataFragment";
    public ArtistDataFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_artists, container, false);
        //artistId=getActivity().getIntent().getStringExtra("artistId");
        List<Artist> artistsList=new ArrayList<Artist>();
        adapter=new ArtistDataAdapter(getActivity(), artistsList);
        ListView artistListView = (ListView) rootView.findViewById(R.id.artist_listview);
        artistListView.setAdapter(adapter);
        EditText artistEditTest=(EditText) rootView.findViewById(R.id.search_text);
        artistEditTest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchQuery=v.getText().toString();
                new SpotifyData().execute(searchQuery, "Artist");
                return true;
            }
        });
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist curAsrtist = (Artist) parent.getItemAtPosition(position);
                String artistId = curAsrtist.id;
                Intent intent = new Intent(getActivity(), TopTracks.class);
                intent.putExtra("artistId", artistId);
                startActivity(intent);
            }
        });
        return rootView;
    }
    class SpotifyData extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String... params) {
            String artistName=params[0];
            String type=params[1];
            Log.d(TAG, "second param: " + type);
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
}
