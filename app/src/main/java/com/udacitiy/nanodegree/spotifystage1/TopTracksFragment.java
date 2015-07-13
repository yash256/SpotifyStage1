package com.udacitiy.nanodegree.spotifystage1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksFragment extends Fragment {
    String artistId;
    private final String TAG="TopTracksFragment";
    TrackAdapter adapter;
    public TopTracksFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_top_tracks, container, false);
        artistId=getActivity().getIntent().getStringExtra("artistId");
        List<Track> tracks=new ArrayList<Track>();
        adapter=new TrackAdapter(getActivity(), tracks);
        ListView trackListview=(ListView) rootView.findViewById(R.id.top_tracks_listview);
        trackListview.setAdapter(adapter);
        new SpotifyData().execute(artistId);
        return rootView;
    }
    class SpotifyData extends AsyncTask<String, Void, List<Track>> {

        @Override
        protected List<Track> doInBackground(String... params) {
            String id=params[0];
            SpotifyApi api=new SpotifyApi();
            SpotifyService service = api.getService();
            Log.d(TAG, "Searching for artistId: "+artistId);
            Map<String, Object> options=new HashMap<String, Object>();
            options.put("country","US");
            Tracks topTracks=service.getArtistTopTrack(artistId, options);
            for(int i=0;i<topTracks.tracks.size();i++){
                Track track=topTracks.tracks.get(i);
                Log.d(TAG, "Track name: " + track.name);
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
}
