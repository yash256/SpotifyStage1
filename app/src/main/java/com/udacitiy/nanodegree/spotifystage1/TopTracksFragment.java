package com.udacitiy.nanodegree.spotifystage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksFragment extends Fragment {
    String artistId;
    String artistName;
    private final String TAG="TopTracksFragment";
    TrackAdapter adapter;
    public TopTracksFragment() {

    }
    Tracks topTracks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_top_tracks, container, false);
        artistId=getActivity().getIntent().getStringExtra("artistId");
        List<Track> tracks=new ArrayList<Track>();
        adapter=new TrackAdapter(getActivity(), tracks);
        ListView trackListview=(ListView) rootView.findViewById(R.id.top_tracks_listview);
        trackListview.setAdapter(adapter);
        trackListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Track track=(Track) parent.getItemAtPosition(position);
                String trackName=track.name;
                String albumName=track.album.name;
                AlbumSimple albumSimple=track.album;
                String previewUrl=track.preview_url;
                String imageUrl="";
                if(albumSimple.images!=null && albumSimple.images.size()>0) {
                    imageUrl = albumSimple.images.get(0).url;
                }
                Intent intent=new Intent(getActivity(), MediaPlayerActivity.class);
                intent.putExtra("Position", position);
                intent.putExtra("ArtistName", artistName);
                intent.putParcelableArrayListExtra("TopTracks", (ArrayList) topTracks.tracks);
                startActivity(intent);
            }
        });
        if(isNetworkConnected()) {
            new SpotifyData().execute(artistId);
        }else{
            Toast.makeText(getActivity(), "No Internet available", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }
    class SpotifyData extends AsyncTask<String, Void, List<Track>> {

        @Override
        protected List<Track> doInBackground(String... params) {
            String id=params[0];
            SpotifyApi api=new SpotifyApi();
            SpotifyService service = api.getService();
            Log.d(TAG, "Searching for artistId: " + artistId);
            topTracks=service.getArtistTopTrack(artistId, "US");
            Artist ar=service.getArtist(artistId);
            artistName=ar.name;
            Log.d(TAG, "Artist name="+artistName);
            for(int i=0;i<topTracks.tracks.size();i++){
                Track track=topTracks.tracks.get(i);
            }
            return topTracks.tracks;
        }
        @Override
        public void onPostExecute(List<Track> tracks){
            Log.d(TAG,"onpostexecute");
            if(tracks!=null && tracks.size()>0){
                Log.d(TAG,"tracks not null");
                adapter.clear();
                adapter.addAll(tracks);
            } else {
                Log.d(TAG, "No tracks");
            }
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
}
