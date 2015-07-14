package com.udacitiy.nanodegree.spotifystage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static ArtistDataAdapter adapter;
    private static final String TAG = "MaiActivityFragment";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        List<Artist> artistsList = new ArrayList<Artist>();
        adapter = new ArtistDataAdapter(getActivity(), artistsList);
        ListView artistListView = (ListView) rootView.findViewById(R.id.artist_listview);
        artistListView.setAdapter(adapter);
        EditText artistEditText = (EditText) rootView.findViewById(R.id.search_text);
        artistEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchQuery = v.getText().toString();
                if (isNetworkConnected()) {
                    if (!searchQuery.equals("")) {
                        new SpotifyData().execute(searchQuery, "Artist");
                    } else {
                        Toast.makeText(getActivity(), "Please enter a search query", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet connection available", Toast.LENGTH_SHORT).show();
                }
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
            String artistName = params[0];
            String type = params[1];
            Log.d(TAG, "second param: " + type);
            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();
            ArtistsPager results = service.searchArtists(artistName);
            List<Artist> artists = results.artists.items;
            return artists;

        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            if (artists != null && artists.size() > 0) {
                adapter.clear();
                adapter.addAll(artists);
            } else {
                Toast.makeText(getActivity(), "No artists found", Toast.LENGTH_LONG).show();
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
